package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.utils.DialogService;
import com.legec.imageCrowler.utils.GlobalConfig;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hubert.legec on 2016-03-12.
 */
public class InstaTabController {
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private TextField tokenTF;
    @FXML
    private TextField storageFolderTF;
    @FXML
    private TextField fileNamePrefixTF;
    @FXML
    private TextField maxNumberOfImagesTF;
    @FXML
    private ListView<String> tagList;
    @FXML
    private Button runButton;
    @FXML
    private Button removeTag;

    private ObservableList<String> tagObservableList = FXCollections.observableArrayList();
    private InstagramCrawlController instagramCrawlController = new InstagramCrawlController();
    private BooleanProperty tagListEmpty = new SimpleBooleanProperty(true);
    private boolean instaWorks = false;
    private Stage primaryStage;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        runButton.disableProperty().bind(storageFolderTF.textProperty().isEmpty().or(tagListEmpty).or(tokenTF.textProperty().isEmpty()));
        tagList.setItems(tagObservableList);
        removeTag.disableProperty().bind(tagList.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    private void onAddTag() {
        Optional<String> value = DialogService.showAddTagDialog(primaryStage.getScene().getWindow());
        value.ifPresent(v -> {
            tagObservableList.add(v);
            tagListEmpty.setValue(false);
        });
    }

    @FXML
    private void onRemoveTag() {
        tagObservableList.remove(tagList.getSelectionModel().getSelectedIndex());
        if (tagList.getItems().size() == 0) {
            tagListEmpty.setValue(true);
        }
    }

    @FXML
    private void onInstaRun() {
        if (!instaWorks) {
            saveValues();
            instagramCrawlController.init();
            instagramCrawlController.startWithCallback(() -> {
                Platform.runLater(() -> runButton.setText("Run"));
                instaWorks = false;
                progressIndicator.setVisible(false);
            });
            instaWorks = true;
            progressIndicator.setVisible(true);
            runButton.setText("Cancel");
        } else {
            instagramCrawlController.stop();
        }
    }

    @FXML
    private void onStorageFolder() {
        File dir = DialogService.showStorageDirectoryDialog(primaryStage.getScene().getWindow());
        if (dir != null) {
            storageFolderTF.setText(dir.getPath());
        }
    }

    public void saveValues() {
        InstagramConfig config = GlobalConfig.getInstance().getInstagramConfig();
        config.setTags(tagObservableList.stream().collect(Collectors.toList()));
        config.setToken(tokenTF.getText());
        config.setFileNamePrefix(fileNamePrefixTF.getText());
        config.setStorageFolder(storageFolderTF.getText());
        if (maxNumberOfImagesTF.getText() != null && maxNumberOfImagesTF.getText().length() > 0) {
            config.setMaxNumberOfImages(Integer.valueOf(maxNumberOfImagesTF.getText()));
        } else {
            config.setMaxNumberOfImages(-1);
        }
    }

    public void loadValues() {
        InstagramConfig config = GlobalConfig.getInstance().getInstagramConfig();
        tagObservableList.setAll(config.getTags());
        if (tagObservableList.size() > 0) {
            tagListEmpty.setValue(false);
        }
        tokenTF.setText(config.getToken());
        fileNamePrefixTF.setText(config.getFileNamePrefix());
        storageFolderTF.setText(config.getStorageFolder());
        if (config.getMaxNumberOfImages() > 0) {
            maxNumberOfImagesTF.setText(String.valueOf(config.getMaxNumberOfImages()));
        } else {
            maxNumberOfImagesTF.clear();
        }
    }
}
