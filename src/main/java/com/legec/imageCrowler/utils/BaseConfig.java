package com.legec.imageCrowler.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class BaseConfig {
    private String storageFolder;
    private String fileNamePrefix;
    private List<String> tags = new LinkedList<>();
    private int maxNumberOfImages;

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public void setFileNamePrefix(String fileNamePrefix) {
        this.fileNamePrefix = fileNamePrefix;
    }

    public int getMaxNumberOfImages() {
        return maxNumberOfImages;
    }

    public void setMaxNumberOfImages(int maxNumberOfImages) {
        this.maxNumberOfImages = maxNumberOfImages;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
