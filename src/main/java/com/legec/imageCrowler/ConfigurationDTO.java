package com.legec.imageCrowler;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class ConfigurationDTO {
    private String storageFolder;
    private List<String> seedURLs = new LinkedList<>();
    private int numberOfThreads;
    private String imageFilePrefix;

    public ConfigurationDTO() {
    }

    public ConfigurationDTO(String storageFolder, List<String> seedURLs, int numberOfThreads, String imageFilePrefix) {
        this.storageFolder = storageFolder;
        this.seedURLs = seedURLs;
        this.numberOfThreads = numberOfThreads;
        this.imageFilePrefix = imageFilePrefix;
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

    public String getImageFilePrefix() {
        return imageFilePrefix;
    }

    public void setImageFilePrefix(String imageFilePrefix) {
        this.imageFilePrefix = imageFilePrefix;
    }
}
