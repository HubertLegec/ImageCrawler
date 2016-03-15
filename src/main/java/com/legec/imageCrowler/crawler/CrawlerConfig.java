package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.utils.BaseConfig;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class CrawlerConfig extends BaseConfig {
    private List<String> seedURLs = new LinkedList<>();
    private boolean tagsActive = false;
    private int numberOfThreads;
    private int crawlDepth;
    private int minImageWidth;
    private int minImageHeight;

    public int getCrawlDepth() {
        return crawlDepth;
    }

    public void setCrawlDepth(int crawlDepth) {
        this.crawlDepth = crawlDepth;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public List<String> getSeedURLs() {
        return seedURLs;
    }

    public void setSeedURLs(List<String> seedURLs) {
        this.seedURLs = seedURLs;
    }

    public boolean isTagsActive() {
        return tagsActive;
    }

    public void setTagsActive(boolean tagsActive) {
        this.tagsActive = tagsActive;
    }

    public int getMinImageWidth() { return minImageWidth; }

    public void setMinImageWidth(int minImageWidth) { this.minImageWidth = minImageWidth; }

    public int getMinImageHeight() { return minImageHeight; }

    public void setMinImageHeight(int minImageHeight) { this.minImageHeight = minImageHeight; }
}
