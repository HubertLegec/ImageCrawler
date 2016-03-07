package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hubert on 02.03.2016.
 */
public class InstagramCrawlController {
    private static String INSTA_TOKEN = "1935229135.1677ed0.43af0f66a71d4653857c8a6e5e8866e3";
    private static final Logger logger = LoggerFactory.getLogger(InstagramCrawlController.class);
    private RestClient restClient;

    public InstagramCrawlController(){
        this.restClient = new RestClient("https://api.instagram.com/v1");
    }

}
