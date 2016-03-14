package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.BaseCrawlController;
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
public class CrawlersController implements BaseCrawlController {
    private static final Logger logger = LoggerFactory.getLogger(CrawlersController.class);
    private CrawlController crawlController;
    private boolean ready = false;

    @Override
    public void init() {
        CrawlerConfig config = GlobalConfig.getInstance().getCrawlerConfig();
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(config.getStorageFolder());
        crawlConfig.setIncludeBinaryContentInCrawling(true);
        if (config.getCrawlDepth() > 0) {
            crawlConfig.setMaxDepthOfCrawling(config.getCrawlDepth());
        }
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            config.getSeedURLs().forEach(url -> crawlController.addSeed(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready = true;
    }

    @Override
    public boolean start() {
        if (ready) {
            logger.debug("Crawler - start");
            CrawlerConfig config = GlobalConfig.getInstance().getCrawlerConfig();
            ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getFileNamePrefix(),
                    config.getMaxNumberOfImages(), config.getTags(), config.isTagsActive(), () -> crawlController.shutdown());
            crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
            return true;
        }
        return false;
    }

    @Override
    public void startWithCallback(Callback callback) {
        logger.debug("Crawler - start with callback");
        CrawlerConfig config = GlobalConfig.getInstance().getCrawlerConfig();
        ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getFileNamePrefix(),
                config.getMaxNumberOfImages(), config.getTags(), config.isTagsActive(), () -> crawlController.shutdown());
        crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
        Thread thread = new Thread(() -> {
            crawlController.waitUntilFinish();
            logger.debug("Crawler - finish");
            callback.execute();
        });
        thread.start();
    }

    @Override
    public void stop() {
        logger.debug("Crawler - shutdown");
        crawlController.shutdown();
    }
}
