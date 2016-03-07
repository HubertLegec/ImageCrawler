package com.legec.imageCrowler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class ConfigurationDTO {
    private String storageFolder;
    private List<String> seedURLs;
    private List<String> tags;
    private int numberOfThreads;
    private String imageFilePrefix;
    private int crawlDepth;
    private int maxNumberOfImages;

    public ConfigurationDTO() {
        this.seedURLs = new LinkedList<>();
        this.tags = new LinkedList<>();
    }

    public ConfigurationDTO(String storageFolder, List<String> seedURLs, int numberOfThreads, String imageFilePrefix,
                            List<String> tags, int crawlDepth, int maxNumberOfImages) {
        this.storageFolder = storageFolder;
        this.seedURLs = seedURLs;
        this.tags = tags;
        this.numberOfThreads = numberOfThreads;
        this.imageFilePrefix = imageFilePrefix;
        this.crawlDepth = crawlDepth;
        this.maxNumberOfImages = maxNumberOfImages;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    public List<String> getSeedURLs() {
        return seedURLs;
    }

    public void setSeedURLs(List<String> seedURLs) {
        this.seedURLs = seedURLs;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public String getImageFilePrefix() { return imageFilePrefix; }

    public void setImageFilePrefix(String imageFilePrefix) { this.imageFilePrefix = imageFilePrefix; }

    public List<String> getTags() { return tags; }

    public void setTags(List<String> tags) { this.tags = tags; }

    public int getCrawlDepth() { return crawlDepth; }

    public void setCrawlDepth(int crawlDepth) { this.crawlDepth = crawlDepth; }

    public int getMaxNumberOfImages() { return maxNumberOfImages; }

    public void setMaxNumberOfImages(int maxNumberOfImages) { this.maxNumberOfImages = maxNumberOfImages; }
}
