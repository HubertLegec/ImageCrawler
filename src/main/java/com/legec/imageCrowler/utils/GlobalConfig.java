package com.legec.imageCrowler.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class GlobalConfig {
    private static GlobalConfig config;

    private String storageFolder;
    private String instaStorageFolder;
    private List<String> seedURLs = new LinkedList<>();
    private List<String> tags = new LinkedList<>();
    private boolean tagsActive = false;
    private List<String> instaTags = new LinkedList<>();
    private List<String> flickrTags = new LinkedList<>();
    private String flickrText;
    private boolean flickrByTag = true;
    private int flicrMaxNumberOfImages;
    private int numberOfThreads;
    private String imageFilePrefix;
    private String instaImageFilePrefix;
    private int crawlDepth;
    private int maxNumberOfImages;
    private int maxElementsMatchTag;
    private String instaToken;
    private String flickrToken;

    public GlobalConfig() {

    }

    public static GlobalConfig getInstance() {
        if (config == null) {
            config = new GlobalConfig();
        }
        return config;
    }

    public static void setConfigInstance(GlobalConfig config) {
        GlobalConfig.config = config;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageLocation) {
        this.storageFolder = storageLocation;
    }

    public List<String> getSeedURLs() {
        return seedURLs;
    }

    public void setSeedURLs(List<String> seedURLList) {
        this.seedURLs = seedURLList;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThr) {
        this.numberOfThreads = numberOfThr;
    }

    public String getImageFilePrefix() {
        return imageFilePrefix;
    }

    public void setImageFilePrefix(String imageFilePref) {
        this.imageFilePrefix = imageFilePref;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tagList) {
        this.tags = tagList;
    }

    public int getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(int crawlDepthVal) {
        this.crawlDepth = crawlDepthVal;
    }

    public int getMaxNumberOfImages() {
        return maxNumberOfImages;
    }

    public void setMaxNumberOfImages(int maxNumbOfImg) {
        this.maxNumberOfImages = maxNumbOfImg;
    }

    public int getMaxElementsMatchTag() {
        return maxElementsMatchTag;
    }

    public void setMaxElementsMatchTag(int maxElementsMatchTag) {
        this.maxElementsMatchTag = maxElementsMatchTag;
    }

    public String getInstaToken() {
        return instaToken;
    }

    public void setInstaToken(String instaToken) {
        this.instaToken = instaToken;
    }

    public String getInstaStorageFolder() {
        return instaStorageFolder;
    }

    public void setInstaStorageFolder(String instaStorageFolder) {
        this.instaStorageFolder = instaStorageFolder;
    }

    public String getInstaImageFilePrefix() {
        return instaImageFilePrefix;
    }

    public void setInstaImageFilePrefix(String instaImageFilePrefix) {
        this.instaImageFilePrefix = instaImageFilePrefix;
    }

    public List<String> getInstaTags() {
        return instaTags;
    }

    public void setInstaTags(List<String> instaTags) {
        this.instaTags = instaTags;
    }

    public boolean areTagsActive() {
        return tagsActive;
    }

    public void setTagsActive(boolean tagsActive) {
        this.tagsActive = tagsActive;
    }

    public String getFlickrToken() {
        return flickrToken;
    }

    public void setFlickrToken(String flickrToken) {
        this.flickrToken = flickrToken;
    }

    public List<String> getFlickrTags() {
        return flickrTags;
    }

    public void setFlickrTags(List<String> flickrTags) {
        this.flickrTags = flickrTags;
    }

    public boolean isFlickrByTag() {
        return flickrByTag;
    }

    public void setFlickrByTag(boolean flickrByTag) {
        this.flickrByTag = flickrByTag;
    }

    public int getFlicrMaxNumberOfImages() {
        return flicrMaxNumberOfImages;
    }

    public void setFlicrMaxNumberOfImages(int flicrMaxNumberOfImages) {
        this.flicrMaxNumberOfImages = flicrMaxNumberOfImages;
    }

    public String getFlickrText() {
        return flickrText;
    }

    public void setFlickrText(String flickrText) {
        this.flickrText = flickrText;
    }
}
