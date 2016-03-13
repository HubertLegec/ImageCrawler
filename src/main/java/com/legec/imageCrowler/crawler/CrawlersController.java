package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.BaseCrawlController;
import com.legec.imageCrowler.utils.Callback;
import com.legec.imageCrowler.utils.GlobalConfig;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Created by Hubert on 02.03.2016.
 */
public class CrawlersController implements BaseCrawlController {
    private CrawlController crawlController;
    private boolean ready = false;

    @Override
    public void init() {
        CrawlConfig crawlConfig = new CrawlConfig();
        crawlConfig.setCrawlStorageFolder(GlobalConfig.getInstance().getStorageFolder());
        crawlConfig.setIncludeBinaryContentInCrawling(true);
        if (GlobalConfig.getInstance().getCrawlDepth() > 0) {
            crawlConfig.setMaxDepthOfCrawling(GlobalConfig.getInstance().getCrawlDepth());
        }
        PageFetcher pageFetcher = new PageFetcher(crawlConfig);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        try {
            crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
            GlobalConfig.getInstance().getSeedURLs().forEach(url -> crawlController.addSeed(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ready = true;
    }

    @Override
    public boolean start() {
        if (ready) {
            GlobalConfig config = GlobalConfig.getInstance();
            ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getImageFilePrefix(),
                    config.getMaxNumberOfImages(), config.getTags(), config.areTagsActive(), () -> crawlController.shutdown());
            crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
            return true;
        }
        return false;
    }

    @Override
    public void startWithCallback(Callback callback) {
        GlobalConfig config = GlobalConfig.getInstance();
        ImageCrawler.configure(config.getSeedURLs(), config.getStorageFolder(), config.getImageFilePrefix(),
                config.getMaxNumberOfImages(), config.getTags(), config.areTagsActive(), () -> crawlController.shutdown());
        crawlController.startNonBlocking(ImageCrawler.class, config.getNumberOfThreads());
        Thread thread = new Thread(() -> {
            crawlController.waitUntilFinish();
            callback.execute();
        });
        thread.start();
    }

    @Override
    public void stop() {
        crawlController.shutdown();
    }
}
