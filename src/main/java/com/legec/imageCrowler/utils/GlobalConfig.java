package com.legec.imageCrowler.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class GlobalConfig {
    private static String storageFolder;
    private static String instaStorageFolder;
    private static List<String> seedURLs = new LinkedList<>();
    private static List<String> tags = new LinkedList<>();
    private static List<String> instaTags = new LinkedList<>();
    private static int numberOfThreads;
    private static String imageFilePrefix;
    private static String instaImageFilePrefix;
    private static int crawlDepth;
    private static int maxNumberOfImages;
    private static int maxElementsMatchTag;
    private static String instaToken;

    public static String getStorageFolder() {
        return storageFolder;
    }

    public static void setStorageFolder(String storageLocation) {
        storageFolder = storageLocation;
    }

    public static List<String> getSeedURLs() {
        return seedURLs;
    }

    public static void setSeedURLs(List<String> seedURLList) {
        seedURLs = seedURLList;
    }

    public static int getNumberOfThreads() {
        return numberOfThreads;
    }

    public static void setNumberOfThreads(int numberOfThr) { numberOfThreads = numberOfThr; }

    public static String getImageFilePrefix() {
        return imageFilePrefix;
    }

    public static void setImageFilePrefix(String imageFilePref) {
        imageFilePrefix = imageFilePref;
    }

    public static List<String> getTags() {
        return tags;
    }

    public static void setTags(List<String> tagList) {
        tags = tagList;
    }

    public static int getCrawlDepth() {
        return crawlDepth;
    }

    public static void setCrawlDepth(int crawlDepthVal) {
        crawlDepth = crawlDepthVal;
    }

    public static int getMaxNumberOfImages() {
        return maxNumberOfImages;
    }

    public static void setMaxNumberOfImages(int maxNumbOfImg) {
        maxNumberOfImages = maxNumbOfImg;
    }

    public static int getMaxElementsMatchTag() { return maxElementsMatchTag; }

    public static void setMaxElementsMatchTag(int maxElementsMatchTag) { GlobalConfig.maxElementsMatchTag = maxElementsMatchTag; }

    public static String getInstaToken() { return instaToken; }

    public static void setInstaToken(String instaToken) { GlobalConfig.instaToken = instaToken; }

    public static String getInstaStorageFolder() { return instaStorageFolder; }

    public static void setInstaStorageFolder(String instaStorageFolder) { GlobalConfig.instaStorageFolder = instaStorageFolder; }

    public static String getInstaImageFilePrefix() { return instaImageFilePrefix; }

    public static void setInstaImageFilePrefix(String instaImageFilePrefix) { GlobalConfig.instaImageFilePrefix = instaImageFilePrefix; }

    public static List<String> getInstaTags() { return instaTags; }

    public static void setInstaTags(List<String> instaTags) { GlobalConfig.instaTags = instaTags; }
}
