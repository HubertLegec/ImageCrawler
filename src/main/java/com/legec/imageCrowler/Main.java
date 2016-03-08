package com.legec.imageCrowler;

import com.legec.imageCrowler.instagram.InstagramCrawlController;
import com.legec.imageCrowler.utils.RunMode;

/**
 * Created by Hubert on 02.03.2016.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("--- IMAGE CRAWLER by Hubert Legec ---");
        if(args.length != 1){
            System.out.println("\n-> Incorrect number of arguments !!!");
            System.out.println("You should only put one argument - path to configuration file.");
            return ;
        } else{
            String arg = args[0];
            if(arg.length() > 0 && arg.endsWith(".conf")){
                ConfigurationParser configurationParser = new ConfigurationParser();
                boolean validConf = configurationParser.loadConfigurationFromFile(arg);

                if(!validConf){
                    System.out.println("Incorrect structure of configuration file or file doesn't exist!!!");
                    return;
                }

                RunMode mode = GlobalConfig.getRunMode();
                if(mode == RunMode.DEFAULT || mode == RunMode.ALL || mode == RunMode.WEB_ONLY) {
                    CrawlersController crawlersController = new CrawlersController();
                    crawlersController.init();
                    crawlersController.start();
                }
                if(mode == RunMode.DEFAULT || mode == RunMode.ALL || mode == RunMode.INSTA_ONLY){
                    InstagramCrawlController crawlController = new InstagramCrawlController();
                    crawlController.init();
                    crawlController.start();
                }
                System.out.println(" *** Processing finished. ***");
            }
        }

    }
}
