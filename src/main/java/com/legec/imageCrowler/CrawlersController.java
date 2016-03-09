package com.legec.imageCrowler;

import com.legec.imageCrowler.utils.Callback;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Hubert on 02.03.2016.
 */
public class CrawlersController {
    private static final Logger logger = LoggerFactory.getLogger(CrawlersController.class);
    private CrawlController crawlController;
    private boolean ready = false;

    public boolean init() {
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(GlobalConfig.getStorageFolder());
        crawlConfig.setIncludeBinaryContentInCrawling(true);
        if (GlobalConfig.getCrawlDepth() > 0) {
            crawlConfig.setMaxDepthOfCrawling(GlobalConfig.getCrawlDepth());
        }
        logger.debug("Storage folder set to: " + GlobalConfig.getStorageFolder());
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            logger.debug("Seed URLs set to:");
            GlobalConfig.getSeedURLs().forEach(url -> {
                crawlController.addSeed(url);
                logger.debug(url);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ready = true;
        return true;
    }

    public boolean start() {
        if (ready) {
            logger.debug("Crawling started. Number of threads: " + GlobalConfig.getNumberOfThreads());
            ImageCrawler.configure(GlobalConfig.getSeedURLs(), GlobalConfig.getStorageFolder(), GlobalConfig.getImageFilePrefix(), GlobalConfig.getTags());
            crawlController.startNonBlocking(ImageCrawler.class, GlobalConfig.getNumberOfThreads());
            return true;
        }
        return false;
    }

    public void startWithCallback(Callback callback){
        logger.debug("Crawling started. Number of threads: " + GlobalConfig.getNumberOfThreads());
        ImageCrawler.configure(GlobalConfig.getSeedURLs(), GlobalConfig.getStorageFolder(), GlobalConfig.getImageFilePrefix(), GlobalConfig.getTags());
        crawlController.startNonBlocking(ImageCrawler.class, GlobalConfig.getNumberOfThreads());
        Thread thread = new Thread(() -> {
            crawlController.waitUntilFinish();
            callback.execute();
        });
        thread.start();
    }

    public void stop(){
        crawlController.shutdown();
    }
}
