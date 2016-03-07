package com.legec.imageCrowler;

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

                CrawlersController crawlersController = new CrawlersController(configurationParser.getConfiguration());
                crawlersController.init();

                crawlersController.start();
                System.out.println(" *** Processing finished. ***");
            }
        }

    }
}
