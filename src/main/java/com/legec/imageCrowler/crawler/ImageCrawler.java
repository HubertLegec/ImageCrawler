package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.utils.Callback;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Hubert on 02.03.2016.
 */
public class ImageCrawler extends WebCrawler {

    private static final Pattern filters = Pattern
            .compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

    private static File storageFolder;
    private static List<String> crawlDomains;
    private static List<String> tagList;
    private static String imageNamePrefix;
    private static int nameCounter;
    private static boolean tagsActive;
    private static int maxNumberOfImages;
    private static int minImageHeight;
    private static int minImageWidth;
    private static Callback stopCallback;
    private static boolean callbackSent = false;

    public static void configure(CrawlerConfig config, Callback callback) {
        crawlDomains = new LinkedList<>();
        tagsActive = config.isTagsActive();
        tagList = config.getTags();
        maxNumberOfImages = config.getMaxNumberOfImages();
        minImageHeight = config.getMinImageHeight();
        minImageWidth = config.getMinImageWidth();
        stopCallback = callback;
        nameCounter = 0;
        config.getSeedURLs().forEach(el -> {
            String url = el.replace("http://", "").replace("www.", "");
            crawlDomains.add(url);
        });
        imageNamePrefix = config.getFileNamePrefix();

        storageFolder = new File(config.getStorageFolder());
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        if (maxNumberOfImages > 0 && nameCounter >= maxNumberOfImages) {
            if (!callbackSent) {
                callbackSent = true;
                stopCallback.execute();
            }
            return false;
        }

        String href = url.getURL().toLowerCase();
        if (filters.matcher(href).matches()) {
            return false;
        }

        if (imgPatterns.matcher(href).matches() && matchTags(referringPage, url)) {
            return true;
        }

        for (String domain : crawlDomains) {
            if (href.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        if (maxNumberOfImages > 0 && nameCounter >= maxNumberOfImages) {
            if (!callbackSent) {
                callbackSent = true;
                stopCallback.execute();
            }
            return;
        }

        String stringUrl = page.getWebURL().getURL();

        // We are only interested in processing images which are bigger than 10k
        if (!imgPatterns.matcher(stringUrl).matches() ||
                !((page.getParseData() instanceof BinaryParseData) || (page.getContentData().length < (10 * 1024)))) {
            return;
        }

        String filename = "";
        try {
            URL url = new URL(stringUrl);
            BufferedImage image = ImageIO.read(url);

            if(!checkImageSize(image.getWidth(), image.getHeight())){
                return;
            }

            // get a unique name for storing this image
            String extension = stringUrl.substring(stringUrl.lastIndexOf('.'));

            // store image
            filename = storageFolder.getAbsolutePath() + "/" + getFileName(extension);

            ImageIO.write(image, extension.substring(1) ,new File(filename));
            logger.info("Stored: {}", url);
        } catch (IOException iox) {
            logger.error("Failed to write file: " + filename, iox);
        }

    }

    private static String getFileName(String extension) {
        if (imageNamePrefix != null) {
            return imageNamePrefix + (nameCounter++) + extension;
        }

        nameCounter++;
        return UUID.randomUUID() + extension;
    }

    private boolean matchTags(Page page, WebURL url) {
        if (!tagsActive) {
            return true;
        }
        HtmlParseData parseData = (HtmlParseData) page.getParseData();
        if (parseData == null) {
            return false;
        }
        Map<String, String> metaTags = parseData.getMetaTags();
        if (metaTags == null || metaTags.size() == 0) {
            return false;
        }

        for (String tag : tagList) {
            for (String meta : metaTags.values()) {
                if (contains(meta, tag)) {
                    return true;
                }
            }

            if (contains(url.toString(), tag)) {
                return true;
            }
        }

        return false;
    }

    private static boolean contains(String container, String content) {
        if (container.toLowerCase().contains(content.toLowerCase())) {
            return true;
        }
        return false;
    }

    private boolean checkImageSize(int width, int height) {
        if (minImageWidth > 0 && minImageWidth > width) {
            return false;
        }
        if (minImageHeight > 0 && minImageHeight > height) {
            return false;
        }
        return true;
    }
}
