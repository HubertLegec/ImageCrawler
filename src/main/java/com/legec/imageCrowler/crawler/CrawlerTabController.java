package com.legec.imageCrowler.crawler;

import com.legec.imageCrowler.utils.DialogService;
import com.legec.imageCrowler.utils.GlobalConfig;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hubert.legec on 2016-03-12.
 */
public class CrawlerTabController {
    private ObservableList<String> urlObservableList = FXCollections.observableArrayList();
    private ObservableList<String> tagObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> numberOfCrowlersList = FXCollections.observableArrayList();
    private ObservableList<String> crawlingDepth = FXCollections.observableArrayList();
    private BooleanProperty urlListEmpty = new SimpleBooleanProperty(true);
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
    private ProgressIndicator progressIndicator;
    @FXML
    private TextField maxNumberOfImages;

    private CrawlersController crawlersController = new CrawlersController();
    private boolean crawlerWorks = false;
    private Stage primaryStage;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
    }


    @FXML
    private void onAddSeedURL() {
        Optional<String> value = DialogService.showAddURLDialog(primaryStage.getScene().getWindow());
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
        Optional<String> value = DialogService.showAddTagDialog(primaryStage.getScene().getWindow());
        value.ifPresent(v -> tagObservableList.add(v));
    }

    @FXML
    private void onRemoveTag() {
        tagObservableList.remove(tagList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void onStorageFolderButton() {
        File dir = DialogService.showStorageDirectoryDialog(primaryStage.getScene().getWindow());
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


    public void saveValues() {
        CrawlerConfig config = GlobalConfig.getInstance().getCrawlerConfig();
        config.setTags(tagObservableList.stream().collect(Collectors.toList()));
        config.setSeedURLs(urlObservableList.stream().collect(Collectors.toList()));
        config.setTagsActive(tagsCheckBox.isSelected());
        if (crawlingDepthCB.getValue().equals("INFINITY")) {
            config.setCrawlDepth(-1);
        } else {
            config.setCrawlDepth(Integer.valueOf(crawlingDepthCB.getValue()));
        }
        if (imageNamePrefixTF.getText() != null && imageNamePrefixTF.getText().length() > 0) {
            config.setFileNamePrefix(imageNamePrefixTF.getText());
        } else {
            config.setFileNamePrefix(null);
        }
        if (maxNumberOfImages.getText() != null && maxNumberOfImages.getText().length() > 0) {
            config.setMaxNumberOfImages(Integer.parseInt(maxNumberOfImages.getText()));
        } else {
            config.setMaxNumberOfImages(-1);
        }
        config.setStorageFolder(storageFolderTF.getText());
        config.setNumberOfThreads(numberOfCrowlersCB.getValue());
    }

    public void loadValues() {
        CrawlerConfig config = GlobalConfig.getInstance().getCrawlerConfig();
        tagObservableList.setAll(config.getTags());
        urlObservableList.setAll(config.getSeedURLs());
        if (urlObservableList.size() > 0) {
            urlListEmpty.setValue(false);
        }
        tagsCheckBox.setSelected(config.isTagsActive());
        if (config.getCrawlDepth() == -1) {
            crawlingDepthCB.setValue("INFINITY");
        } else {
            crawlingDepthCB.setValue(String.valueOf(config.getCrawlDepth()));
        }
        if (config.getMaxNumberOfImages() > 0) {
            maxNumberOfImages.setText(String.valueOf(config.getMaxNumberOfImages()));
        } else {
            maxNumberOfImages.clear();
        }
        imageNamePrefixTF.setText(config.getFileNamePrefix());
        storageFolderTF.setText(config.getStorageFolder());
        numberOfCrowlersCB.setValue(config.getNumberOfThreads());
    }
}
