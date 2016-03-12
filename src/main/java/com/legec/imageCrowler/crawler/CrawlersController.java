package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.utils.Callback;
import com.legec.imageCrowler.utils.GlobalConfig;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hubert on 02.03.2016.
 */
public class CrawlersController {
    private static final Logger logger = LoggerFactory.getLogger(CrawlersController.class);
    private CrawlController crawlController;
    private boolean ready = false;

    public boolean init() {
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(GlobalConfig.getInstance().getStorageFolder());
        crawlConfig.setIncludeBinaryContentInCrawling(true);
        if (GlobalConfig.getInstance().getCrawlDepth() > 0) {
            crawlConfig.setMaxDepthOfCrawling(GlobalConfig.getInstance().getCrawlDepth());
        }
        logger.debug("Storage folder set to: " + GlobalConfig.getInstance().getStorageFolder());
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            logger.debug("Seed URLs set to:");
            GlobalConfig.getInstance().getSeedURLs().forEach(url -> {
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
            GlobalConfig config = GlobalConfig.getInstance();
            logger.debug("Crawling started. Number of threads: " + config.getNumberOfThreads());
            ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getImageFilePrefix(), config.getTags());
            crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
            return true;
        }
        return false;
    }

    public void startWithCallback(Callback callback){
        GlobalConfig config = GlobalConfig.getInstance();
        logger.debug("Crawling started. Number of threads: " + config.getNumberOfThreads());
        ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getImageFilePrefix(), config.getTags());
        crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
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
