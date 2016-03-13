package com.legec.imageCrowler.flickr;

import javafx.stage.Stage;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FlickrTabController {

    private FlickrCrawlController flickrCrawlController = new FlickrCrawlController();
    private Stage primaryStage;

    public void init(Stage primaryStage){
        this.primaryStage = primaryStage;

    }

}
