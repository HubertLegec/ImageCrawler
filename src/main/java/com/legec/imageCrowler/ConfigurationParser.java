package com.legec.imageCrowler;

import com.legec.imageCrowler.utils.RunMode;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-06.
 */
public class ConfigurationParser {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParser.class);

    private String storageFolder;
    private List<String> seedURLs;
    private List<String> tags;
    private int numberOfThreads;
    private int maxNumberOfImages;
    private int crawlDepth;
    private String imageNamePrefix;
    private int maxElementsMatchTag;
    private RunMode runMode;
    private String instaToken;

    public ConfigurationParser() {
        BasicConfigurator.configure();
        this.seedURLs = new LinkedList<>();
        this.tags = new LinkedList<>();
        this.storageFolder = null;
        this.imageNamePrefix = null;
        this.maxNumberOfImages = -1;
        this.crawlDepth = -1;
    }

    public boolean loadConfigurationFromFile(@NotNull String pathToConfigFile) {

        Path path = Paths.get(pathToConfigFile);

        try {
            Files.lines(path).forEach(line -> {
                        if (line.startsWith("storage_folder")) {
                            storageFolder = getParameterStringValue(line);
                            logger.debug("Storage folder set to: " + storageFolder);
                        } else if (line.startsWith("number_of_threads")) {
                            numberOfThreads = getParameterIntValue(line);
                            logger.debug("Number of threads set to: " + numberOfThreads);
                        } else if (line.startsWith("name_prefix")) {
                            imageNamePrefix = getParameterStringValue(line);
                            logger.debug("Image name prefix set to: " + imageNamePrefix);
                        } else if (line.startsWith("seed_urls")) {
                            seedURLs = parseMultiValueLine(getParameterStringValue(line));
                            logger.debug("Fetched Seed URLs: " + seedURLs.toString());
                        } else if (line.startsWith("tags")) {
                            tags = parseMultiValueLine(getParameterStringValue(line));
                            logger.debug("Fetched tags: " + tags.toString());
                        } else if (line.startsWith("max_number_of_images")) {
                            maxNumberOfImages = getParameterIntValue(line);
                            logger.debug("Max number of images set to: " + maxNumberOfImages);
                        } else if (line.startsWith("crawl_depth")) {
                            crawlDepth = getParameterIntValue(line);
                            logger.debug("Crawl depth set to: " + crawlDepth);
                        } else if(line.startsWith("max_el_match_tag")){
                            maxElementsMatchTag = getParameterIntValue(line);
                            logger.debug("Max elements match tag set to: " + maxElementsMatchTag);
                        } else if(line.startsWith("instagram_token")){
                            instaToken = getParameterStringValue(line);
                            logger.debug("Instagram token set to: " + instaToken);
                        } else if(line.startsWith("run_mode")){
                            runMode = RunMode.valueOf(getParameterStringValue(line));
                            logger.debug("Run mode set to: " + runMode.toString());
                        } else if (line.length() > 0 && !line.startsWith("#")) {
                            logger.debug("Incorrect config line:\n" + line);
                        }
                    }
            );

            GlobalConfig.setStorageFolder(storageFolder);
            GlobalConfig.setNumberOfThreads(numberOfThreads);
            GlobalConfig.setImageFilePrefix(imageNamePrefix);
            GlobalConfig.setSeedURLs(seedURLs);
            GlobalConfig.setTags(tags);
            GlobalConfig.setMaxNumberOfImages(maxNumberOfImages);
            GlobalConfig.setCrawlDepth(crawlDepth);
            GlobalConfig.setMaxElementsMatchTag(maxElementsMatchTag);
            GlobalConfig.setInstaToken(instaToken);
            GlobalConfig.setRunMode(runMode);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static int getParameterIntValue(@NotNull String line) {
        return Integer.parseInt(line.substring(line.indexOf('=') + 1).trim());
    }

    private static String getParameterStringValue(@NotNull String line) {
        return line.substring(line.indexOf('=') + 1).trim();
    }

    private static List<String> parseMultiValueLine(@NotNull String line) {
        List<String> list = Arrays.asList(line.split(";"));
        list.forEach(el -> el.trim());
        return list;
    }
}
