package com.legec.imageCrowler.crawler;

import com.google.common.io.Files;
import com.legec.imageCrowler.utils.Callback;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Hubert on 02.03.2016.
 */
public class ImageCrawler extends WebCrawler{

    private static final Pattern filters = Pattern
            .compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

    private static File storageFolder;
    private static List<String> crawlDomains;
    private static String imageNamePrefix;
    private static int nameCounter = 0;
    private static boolean tagsActive;
    private static int maxNumberOfImages;
    private static Callback stopCallback;
    private static boolean callbackSent = false;

    public static void configure(List<String> domain, String storageFolderName, String imageNamePref, int maxNumOfImg, List<String> tags, boolean areTagsActive, Callback callback) {
        crawlDomains = new LinkedList<>();
        tagsActive = areTagsActive;
        maxNumberOfImages = maxNumOfImg;
        stopCallback = callback;
        domain.forEach( el -> {
            crawlDomains.add(el);
            if(el.contains("www.")){
                crawlDomains.add(el.replace("www.", ""));
            }
        });
        imageNamePrefix = imageNamePref;

        storageFolder = new File(storageFolderName);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        if(maxNumberOfImages > 0 && nameCounter >= maxNumberOfImages){
            if(!callbackSent){
                callbackSent = true;
                stopCallback.execute();
            }
            return false;
        }

        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
            return false;
        }

        if (imgPatterns.matcher(href).matches()) {
            return true;
        }

        for (String domain : crawlDomains) {
            if (href.startsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        if(maxNumberOfImages > 0 && nameCounter >= maxNumberOfImages){
            if(!callbackSent){
                callbackSent = true;
                stopCallback.execute();
            }
            return;
        }
        String url = page.getWebURL().getURL();

        // We are only interested in processing images which are bigger than 10k
        if (!imgPatterns.matcher(url).matches() ||
                !((page.getParseData() instanceof BinaryParseData) || (page.getContentData().length < (10 * 1024)))) {
            return;
        }

        // get a unique name for storing this image
        String extension = url.substring(url.lastIndexOf('.'));

        // store image
        String filename = storageFolder.getAbsolutePath() + "/" + getFileName(extension);
        try {
            Files.write(page.getContentData(), new File(filename));
            logger.info("Stored: {}", url);
        } catch (IOException iox) {
            logger.error("Failed to write file: " + filename, iox);
        }
    }

    private static String getFileName(String extension){
        if(imageNamePrefix != null){
            return imageNamePrefix + (nameCounter++) + extension;
        }

        nameCounter++;
        return UUID.randomUUID() + extension;
    }

    public static int getNameCounter(){
        return nameCounter;
    }
}
