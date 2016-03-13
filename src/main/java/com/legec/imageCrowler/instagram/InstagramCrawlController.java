package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.BaseCrawlController;
import com.legec.imageCrowler.utils.*;
import java.io.IOException;
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
                if (config.getFileNamePrefix() != null && config.getFileNamePrefix().length() > 0) {
                    FileService.saveImageFilesWithCustomName(config.getStorageFolder(), urls, config.getFileNamePrefix());
                } else {
                    FileService.saveImageFiles(config.getStorageFolder(), urls);
                }
            } catch (IOException e) {
                e.printStackTrace();
                completableFuture.completeExceptionally(e);
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
