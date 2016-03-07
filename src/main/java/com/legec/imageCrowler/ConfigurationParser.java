package com.legec.imageCrowler;

import com.sun.istack.internal.NotNull;
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
    private int numberOfThreads;
    private String imageNamePrefix;

    public ConfigurationParser() {
        this.seedURLs = new LinkedList<>();
        this.storageFolder = null;
        this.imageNamePrefix = null;
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
                            seedURLs = parseURLsLine(getParameterStringValue(line));
                            logger.debug("Seed URLs set to: " + seedURLs.toString());
                        } else if (line.length() > 0){
                            logger.debug("Incorrect config line:\n" + line);
                        }
                    }
            );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public List<String> getSeedURLs() {
        return seedURLs;
    }

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public ConfigurationDTO getConfiguration() {
        return new ConfigurationDTO(storageFolder, seedURLs, numberOfThreads, imageNamePrefix);
    }

    private static int getParameterIntValue(@NotNull String line) {
        return Integer.parseInt(line.substring(line.indexOf('=')).trim());
    }

    private static String getParameterStringValue(@NotNull String line) {
        return line.substring(line.indexOf('=')).trim();
    }

    private static List<String> parseURLsLine(@NotNull String line){
        List<String> list = Arrays.asList(line.split(";"));
        list.forEach(el -> el.trim());
        return list;
    }
}
