package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.utils.Callback;
import com.legec.imageCrowler.utils.DownloadImageFromURLTask;
import com.legec.imageCrowler.utils.GlobalConfig;
import com.legec.imageCrowler.utils.ConcurentExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Hubert on 02.03.2016.
 */
public class InstagramCrawlController {
    private boolean ready = false;
    private Set<String> urls;
    private CompletableFuture completableFuture;

    InstagramApi instagramApi;

    public InstagramCrawlController() {
        urls = new HashSet<>();
    }

    public boolean init() {
        instagramApi = new InstagramApi(GlobalConfig.getInstance().getInstaToken());
        ready = true;
        return true;
    }

    public boolean start() {
        if (ready) {
            GlobalConfig.getInstance().getInstaTags().forEach(tag -> {
                List<String> result = instagramApi.getImageURLs(tag, GlobalConfig.getInstance().getMaxElementsMatchTag());
                urls.addAll(result);
            });

            saveImagesFromUrls();
            return true;
        }
        return false;
    }

    public void startWithCallback(Callback callback) {
        completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Files.createDirectories(Paths.get(GlobalConfig.getInstance().getInstaStorageFolder()));
                GlobalConfig.getInstance().getInstaTags().forEach(tag -> {
                    List<String> result = instagramApi.getImageURLs(tag, GlobalConfig.getInstance().getMaxElementsMatchTag());
                    urls.addAll(result);
                });
                for (String u : urls) {
                    URL url = new URL(u);
                    BufferedImage img = ImageIO.read(url);
                    String name = url.getPath();
                    name = name.substring(name.lastIndexOf("/"));
                    File output = new File(GlobalConfig.getInstance().getInstaStorageFolder(), name);
                    ImageIO.write(img, "jpg", output);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        completableFuture.thenRun(() -> callback.execute());
        completableFuture.exceptionally( e -> {
            callback.execute();
            return this;
        });
    }

    public void stop(){
        completableFuture.cancel(false);
    }


    private boolean saveImagesFromUrls() {
        try {
            ConcurentExecutionService.saveImagesFromURLS(urls, 4, GlobalConfig.getInstance().getInstaStorageFolder());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
