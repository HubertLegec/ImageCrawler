package com.legec.imageCrowler;

import com.legec.imageCrowler.crawler.CrawlersController;
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
import javafx.stage.Stage;

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

    //GUI
    private ObservableList<String> urlObservableList = FXCollections.observableArrayList();
    private ObservableList<String> tagObservableList = FXCollections.observableArrayList();
    private ObservableList<String> instaTagObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> numberOfCrowlersList = FXCollections.observableArrayList();
    private ObservableList<String> crawlingDepth = FXCollections.observableArrayList();
    private BooleanProperty urlListEmpty = new SimpleBooleanProperty(true);
    private BooleanProperty instaTagListEmpty = new SimpleBooleanProperty(true);
    @FXML
    private ListView<String> urlList;
    @FXML
    private ListView<String> tagList;
    @FXML
    private CheckBox tagsCheckBox;
    @FXML
    private Button removeURLButton;
    @FXML
    private Button removeTagButton;
    @FXML
    private TextField storageFolderTF;
    @FXML
    private TextField imageNamePrefixTF;
    @FXML
    private ChoiceBox<Integer> numberOfCrowlersCB;
    @FXML
    private ChoiceBox<String> crawlingDepthCB;
    @FXML
    private Button runButton;
    @FXML
    private Button instaRunButton;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private ProgressIndicator instaProgressIndicator;
    @FXML
    private TextField instaToken;
    @FXML
    private TextField instaStorageFolderTF;
    @FXML
    private TextField instaNamePrefix;
    @FXML
    private TextField instaNumberOfFetchedImages;
    @FXML
    private ListView<String> instaTagList;
    //-----------------------------
    private CrawlersController crawlersController = new CrawlersController();
    private InstagramCrawlController instagramCrawlController = new InstagramCrawlController();
    private boolean crawlerWorks = false;
    private boolean instaWorks = false;


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

            initControls();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initControls() {
        urlList.setItems(urlObservableList);
        tagList.setItems(tagObservableList);
        removeTagButton.disableProperty().bind(tagList.getSelectionModel().selectedItemProperty().isNull());
        removeURLButton.disableProperty().bind(urlList.getSelectionModel().selectedItemProperty().isNull());

        for (int i = 1; i <= 10; i++) {
            numberOfCrowlersList.add(i);
        }
        numberOfCrowlersCB.setItems(numberOfCrowlersList);
        numberOfCrowlersCB.setValue(5);

        crawlingDepth.add("INFINITY");
        for (int i = 1; i <= 10; i++) {
            crawlingDepth.add(String.valueOf(i));
        }
        crawlingDepthCB.setItems(crawlingDepth);
        crawlingDepthCB.setValue("INFINITY");

        runButton.disableProperty().bind(storageFolderTF.textProperty().isEmpty().or(urlListEmpty));

        instaRunButton.disableProperty().bind(instaStorageFolderTF.textProperty().isEmpty().or(instaTagListEmpty).or(instaToken.textProperty().isEmpty()));

        instaTagList.setItems(instaTagObservableList);
    }

    @FXML
    private void onAddSeedURL() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Seed URL");
        dialog.setHeaderText("Add seed URL:");
        Optional<String> value = dialog.showAndWait();
        value.ifPresent(v -> {
            urlObservableList.add(v);
            urlListEmpty.setValue(false);
        });
    }

    @FXML
    private void onRemoveSeedURL() {
        urlObservableList.remove(urlList.getSelectionModel().getSelectedIndex());
        if (urlList.getItems().size() == 0) {
            urlListEmpty.setValue(true);
        }
    }

    @FXML
    private void onAddTag() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New tag");
        dialog.setHeaderText("Add new tag:");
        Optional<String> value = dialog.showAndWait();
        value.ifPresent(v -> tagObservableList.add(v));
    }

    @FXML
    private void onRemoveTag() {
        tagObservableList.remove(tagList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void onStorageFolderButton() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Storage folder");
        File dir = chooser.showDialog(primaryStage.getScene().getWindow());
        if (dir != null) {
            storageFolderTF.setText(dir.getPath());
        }
    }

    @FXML
    private void onCrawlerRun() {
        if (!crawlerWorks) {
            saveValues();
            crawlersController.init();
            crawlersController.startWithCallback(() -> {
                Platform.runLater(() -> runButton.setText("Run"));
                crawlerWorks = false;
                progressIndicator.setVisible(false);
            });
            crawlerWorks = true;
            runButton.setText("Stop");
            progressIndicator.setVisible(true);
        } else {
            crawlersController.stop();
        }
    }

    @FXML
    private void onAddInstaTag() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New tag");
        dialog.setHeaderText("Add new tag:");
        Optional<String> value = dialog.showAndWait();
        value.ifPresent(v -> {
            instaTagObservableList.add(v);
            instaTagListEmpty.setValue(false);
        });
    }

    @FXML
    private void onRemoveInstaTag() {
        instaTagObservableList.remove(instaTagList.getSelectionModel().getSelectedIndex());
        if (instaTagList.getItems().size() == 0) {
            instaTagListEmpty.setValue(true);
        }
    }

    @FXML
    private void onInstaRun() {
        if (!instaWorks) {
            saveInstaValues();
            instagramCrawlController.init();
            instagramCrawlController.startWithCallback(() -> {
                Platform.runLater(() -> instaRunButton.setText("Run"));
                instaWorks = false;
                instaProgressIndicator.setVisible(false);
            });
            instaWorks = true;
            instaProgressIndicator.setVisible(true);
            instaRunButton.setText("Cancel");
        } else {
            instagramCrawlController.stop();
        }
    }

    @FXML
    private void onInstaStorageFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Storage folder");
        File dir = chooser.showDialog(primaryStage.getScene().getWindow());
        if (dir != null) {
            instaStorageFolderTF.setText(dir.getPath());
        }
    }

    @FXML
    private void onLoadConfig(){
        try {
            GlobalConfig.setConfigInstance(PreferenceService.loadFromXMLFile(null));
            GlobalConfig config = GlobalConfig.getInstance();
            //----
            tagObservableList.setAll(config.getTags());
            urlObservableList.setAll(config.getSeedURLs());
            if(urlObservableList.size() > 0){
                urlListEmpty.setValue(false);
            }
            tagsCheckBox.setSelected(config.areTagsActive());
            if(config.getCrawlDepth() == -1) {
                crawlingDepthCB.setValue("INFINITY");
            } else {
                crawlingDepthCB.setValue(String.valueOf(config.getCrawlDepth()));
            }
            imageNamePrefixTF.setText(config.getImageFilePrefix());
            storageFolderTF.setText(config.getStorageFolder());
            numberOfCrowlersCB.setValue(config.getNumberOfThreads());
            //---
            instaTagObservableList.setAll(config.getInstaTags());
            if(instaTagObservableList.size() > 0){
                instaTagListEmpty.setValue(false);
            }
            instaToken.setText(config.getInstaToken());
            instaNamePrefix.setText(config.getInstaImageFilePrefix());
            instaStorageFolderTF.setText(config.getInstaStorageFolder());
            if(config.getMaxElementsMatchTag() > 0) {
                instaNumberOfFetchedImages.setText(String.valueOf(config.getMaxElementsMatchTag()));
            } else {
                instaNumberOfFetchedImages.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveConfig(){
        saveValues();
        saveInstaValues();
        try {
            PreferenceService.saveToXMLFile(GlobalConfig.getInstance(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveValues(){
        GlobalConfig config = GlobalConfig.getInstance();
        config.setTags(tagObservableList.stream().collect(Collectors.toList()));
        config.setSeedURLs(urlObservableList.stream().collect(Collectors.toList()));
        config.setTagsActive(tagsCheckBox.isSelected());
        if (crawlingDepthCB.getValue().equals("INFINITY")) {
            config.setCrawlDepth(-1);
        } else {
            config.setCrawlDepth(Integer.valueOf(crawlingDepthCB.getValue()));
        }
        if (imageNamePrefixTF.getText() != null && imageNamePrefixTF.getText().length() > 0) {
            config.setImageFilePrefix(imageNamePrefixTF.getText());
        } else {
            config.setImageFilePrefix(null);
        }
        config.setStorageFolder(storageFolderTF.getText());
        config.setNumberOfThreads(numberOfCrowlersCB.getValue());
    }

    private void saveInstaValues(){
        GlobalConfig config = GlobalConfig.getInstance();
        config.setInstaTags(instaTagObservableList.stream().collect(Collectors.toList()));
        config.setInstaToken(instaToken.getText());
        config.setInstaImageFilePrefix(instaNamePrefix.getText());
        config.setInstaStorageFolder(instaStorageFolderTF.getText());
        if (instaNumberOfFetchedImages.getText() != null && instaNumberOfFetchedImages.getText().length() > 0) {
            config.setMaxElementsMatchTag(Integer.valueOf(instaNumberOfFetchedImages.getText()));
        } else {
            config.setMaxElementsMatchTag(-1);
        }
    }
}
