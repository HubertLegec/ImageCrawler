package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.GlobalConfig;
import com.legec.imageCrowler.utils.ConcurentExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Hubert on 02.03.2016.
 */
public class InstagramCrawlController {
    private static final Logger logger = LoggerFactory.getLogger(InstagramCrawlController.class);
    private boolean ready = false;
    private Set<String> urls;

    InstagramApi instagramApi;

    public InstagramCrawlController(){
        urls = new HashSet<>();
    }

    public boolean init(){
        instagramApi = new InstagramApi(GlobalConfig.getInstaToken());
        ready = true;
        return true;
    }

    public boolean start(){
        if(ready){
            GlobalConfig.getTags().forEach( tag -> {
                List<String> result = instagramApi.getImageURLs(tag, GlobalConfig.getMaxElementsMatchTag());
                urls.addAll(result);
            });

            saveImagesFromUrls();
            return true;
        }
        return false;
    }


    private boolean saveImagesFromUrls(){
        logger.debug("Saving instagram images start");
        try {
            ConcurentExecutionService.saveImagesFromURLS(urls, 4, GlobalConfig.getStorageFolder() + "\\instagram");
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Saving instagram images finish. Error!!!");
            return false;
        }

        logger.debug("Saving instagram images finish. Success!!!");
        return true;
    }

}
