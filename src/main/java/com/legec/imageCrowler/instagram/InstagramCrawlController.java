package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.BaseCrawlController;
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
public class InstagramCrawlController implements BaseCrawlController {
    private boolean ready = false;
    private Set<String> urls;
    private CompletableFuture completableFuture;

    private InstagramApi instagramApi;

    public InstagramCrawlController() {
        urls = new HashSet<>();
    }


    @Override
    public void init() {
        instagramApi = new InstagramApi(GlobalConfig.getInstance().getInstagramConfig().getToken());
        ready = true;
    }

    @Override
    public boolean start() {
        if (ready) {
            GlobalConfig.getInstance().getInstagramConfig().getTags().forEach(tag -> {
                List<String> result = instagramApi.getImageURLs(tag, GlobalConfig.getInstance().getInstagramConfig().getMaxNumberOfImages());
                urls.addAll(result);
            });

            saveImagesFromUrls();
            return true;
        }
        return false;
    }

    @Override
    public void startWithCallback(Callback callback) {
        completableFuture = CompletableFuture.runAsync(() -> {
            try {
                InstagramConfig config = GlobalConfig.getInstance().getInstagramConfig();
                Files.createDirectories(Paths.get(config.getStorageFolder()));
                config.getTags().forEach(tag -> {
                    List<String> result = instagramApi.getImageURLs(tag, config.getMaxNumberOfImages());
                    urls.addAll(result);
                });
                for (String u : urls) {
                    URL url = new URL(u);
                    BufferedImage img = ImageIO.read(url);
                    String name = url.getPath();
                    name = name.substring(name.lastIndexOf("/"));
                    File output = new File(config.getStorageFolder(), name);
                    ImageIO.write(img, "jpg", output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        completableFuture.thenRun(() -> callback.execute());
        completableFuture.exceptionally(e -> {
            callback.execute();
            return this;
        });
    }

    @Override
    public void stop() {
        completableFuture.cancel(false);
    }


    private boolean saveImagesFromUrls() {
        try {
            ConcurentExecutionService.saveImagesFromURLS(urls, 4, GlobalConfig.getInstance().getInstagramConfig().getStorageFolder());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
