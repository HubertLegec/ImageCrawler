package com.legec.imageCrowler.flickr;

import com.legec.imageCrowler.BaseCrawlController;
import com.legec.imageCrowler.utils.Callback;
import com.legec.imageCrowler.utils.ConcurentExecutionService;
import com.legec.imageCrowler.utils.FileService;
import com.legec.imageCrowler.utils.GlobalConfig;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FlickrCrawlController implements BaseCrawlController {
    private boolean ready = false;
    private Set<String> urls;
    private CompletableFuture completableFuture;

    private FlickrApi flickrApi;

    public FlickrCrawlController() {
        urls = new HashSet<>();
    }

    @Override
    public void init() {
        flickrApi = new FlickrApi(GlobalConfig.getInstance().getFlickrConfig().getToken());
        ready = true;
    }

    @Override
    public boolean start() {
        if (ready) {
            FlickrConfig config = GlobalConfig.getInstance().getFlickrConfig();
            List<String> result;
            if (config.isByTag()) {
                result = flickrApi.getListOfUrlsByTags(config.getTags(), config.getMaxNumberOfImages());
            } else {
                result = flickrApi.getListOfUrlsByText(config.getText(), config.getMaxNumberOfImages());
            }
            urls.addAll(result);
            saveImagesFromUrlsAsync();
            return true;
        }
        return false;
    }

    @Override
    public void startWithCallback(Callback callback) {
        completableFuture = CompletableFuture.runAsync(() -> {
            try {
                FlickrConfig config = GlobalConfig.getInstance().getFlickrConfig();
                List<String> result;
                if (config.isByTag()) {
                    result = flickrApi.getListOfUrlsByTags(config.getTags(), config.getMaxNumberOfImages());
                } else {
                    result = flickrApi.getListOfUrlsByText(config.getText(), config.getMaxNumberOfImages());
                }
                urls.addAll(result);
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

    private boolean saveImagesFromUrlsAsync() {
        try {
            ConcurentExecutionService.saveImagesFromURLS(urls, 4, GlobalConfig.getInstance().getFlickrConfig().getStorageFolder());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
