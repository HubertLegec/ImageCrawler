package com.legec.imageCrowler;

import com.legec.imageCrowler.crawler.CrawlerTabController;
import com.legec.imageCrowler.crawler.CrawlersController;
import com.legec.imageCrowler.flickr.FlickrTabController;
import com.legec.imageCrowler.instagram.InstaTabController;
import com.legec.imageCrowler.instagram.InstagramCrawlController;
import com.legec.imageCrowler.utils.GlobalConfig;
import com.legec.imageCrowler.utils.PreferenceService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Hubert on 02.03.2016.
 */
public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    @FXML
    private InstaTabController instaTabController;
    @FXML
    private CrawlerTabController crawlerTabController;
    @FXML
    private FlickrTabController flickrTabController;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("IMAGE CRAWLER by Hubert Legęć");

        initRootLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("MainWindow.fxml"));
            loader.setController(this);
            rootLayout = loader.load();

            // Show the scene containing the root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            instaTabController.init(primaryStage);
            crawlerTabController.init(primaryStage);
            flickrTabController.init(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoadConfig(){
        try {
            GlobalConfig.setConfigInstance(PreferenceService.loadFromXMLFile(null));
            crawlerTabController.loadValues();
            instaTabController.loadValues();
            flickrTabController.loadValues();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Can't load configuration from file");
            alert.setContentText("File doesn't exist or has incorrect structure.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onSaveConfig(){
        crawlerTabController.saveValues();
        instaTabController.saveValues();
        flickrTabController.saveValues();

        try {
            PreferenceService.saveToXMLFile(GlobalConfig.getInstance(), null);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error during saving configuration. Try again.");
            alert.showAndWait();
        }
    }
}
