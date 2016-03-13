package com.legec.imageCrowler.utils;

import com.legec.imageCrowler.crawler.CrawlerConfig;
import com.legec.imageCrowler.flickr.FlickrConfig;
import com.legec.imageCrowler.instagram.InstagramConfig;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class GlobalConfig {
    private static GlobalConfig config;

    private CrawlerConfig crawlerConfig;
    private InstagramConfig instagramConfig;
    private FlickrConfig flickrConfig;


    public GlobalConfig() {
        this.flickrConfig = new FlickrConfig();
        this.instagramConfig = new InstagramConfig();
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

    public CrawlerConfig getCrawlerConfig() {
        return crawlerConfig;
    }

    public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }

    public FlickrConfig getFlickrConfig() {
        return flickrConfig;
    }

    public void setFlickrConfig(FlickrConfig flickrConfig) {
        this.flickrConfig = flickrConfig;
    }

    public InstagramConfig getInstagramConfig() {
        return instagramConfig;
    }

    public void setInstagramConfig(InstagramConfig instagramConfig) {
        this.instagramConfig = instagramConfig;
    }
}
