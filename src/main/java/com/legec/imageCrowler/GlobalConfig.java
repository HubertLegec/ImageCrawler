package com.legec.imageCrowler;

import com.legec.imageCrowler.utils.RunMode;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class GlobalConfig {
    private static String storageFolder;
    private static List<String> seedURLs = new LinkedList<>();
    private static List<String> tags = new LinkedList<>();
    private static int numberOfThreads;
    private static String imageFilePrefix;
    private static int crawlDepth;
    private static int maxNumberOfImages;
    private static int maxElementsMatchTag;
    private static RunMode runMode;
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

    public static RunMode getRunMode() { return runMode; }

    public static void setRunMode(RunMode runMode) { GlobalConfig.runMode = runMode; }

    public static String getInstaToken() { return instaToken; }

    public static void setInstaToken(String instaToken) { GlobalConfig.instaToken = instaToken; }
}
