package com.legec.imageCrowler;

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
    private int numberOfCrawlers = 1;
    private String storageFolder;
    private String imageNamePrefix;
    private List<String> seedURLs;
    private List<String> tags;
    private int crawlDepth;
    private int maxNumberOfImages;
    private CrawlController crawlController;
    private boolean ready = false;

    public CrawlersController(ConfigurationDTO config) {
        this.numberOfCrawlers = config.getNumberOfThreads();
        this.storageFolder = config.getStorageFolder();
        this.seedURLs = config.getSeedURLs();
        this.imageNamePrefix = config.getImageFilePrefix();
        this.tags = config.getTags();
        this.crawlDepth = config.getCrawlDepth();
        this.maxNumberOfImages = config.getMaxNumberOfImages();
    }

    public boolean init() {
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(storageFolder);
        crawlConfig.setIncludeBinaryContentInCrawling(true);
        if (crawlDepth > 0) {
            crawlConfig.setMaxDepthOfCrawling(crawlDepth);
        }
        logger.debug("Storage folder set to: " + storageFolder);
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            logger.debug("Seed URLs set to:");
            seedURLs.forEach(url -> {
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
            logger.debug("Crawling started. Number of threads: " + numberOfCrawlers);
            ImageCrawler.configure(seedURLs, storageFolder, imageNamePrefix, tags);
            crawlController.start(ImageCrawler.class, numberOfCrawlers);
            return true;
        }
        return false;
    }
}
