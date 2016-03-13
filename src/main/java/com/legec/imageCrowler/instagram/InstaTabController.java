package com.legec.imageCrowler.instagram;

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
public class InstaTabController {
    private ObservableList<String> instaTagObservableList = FXCollections.observableArrayList();
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
    @FXML
    private Button instaRunButton;

    private InstagramCrawlController instagramCrawlController = new InstagramCrawlController();
    private BooleanProperty instaTagListEmpty = new SimpleBooleanProperty(true);
    private boolean instaWorks = false;
    private Stage primaryStage;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        instaRunButton.disableProperty().bind(instaStorageFolderTF.textProperty().isEmpty().or(instaTagListEmpty).or(instaToken.textProperty().isEmpty()));
        instaTagList.setItems(instaTagObservableList);
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
            saveValues();
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

    public void saveValues() {
        InstagramConfig config = GlobalConfig.getInstance().getInstagramConfig();
        config.setTags(instaTagObservableList.stream().collect(Collectors.toList()));
        config.setToken(instaToken.getText());
        config.setFileNamePrefix(instaNamePrefix.getText());
        config.setStorageFolder(instaStorageFolderTF.getText());
        if (instaNumberOfFetchedImages.getText() != null && instaNumberOfFetchedImages.getText().length() > 0) {
            config.setMaxNumberOfImages(Integer.valueOf(instaNumberOfFetchedImages.getText()));
        } else {
            config.setMaxNumberOfImages(-1);
        }
    }

    public void loadValues() {
        InstagramConfig config = GlobalConfig.getInstance().getInstagramConfig();
        instaTagObservableList.setAll(config.getTags());
        if (instaTagObservableList.size() > 0) {
            instaTagListEmpty.setValue(false);
        }
        instaToken.setText(config.getToken());
        instaNamePrefix.setText(config.getFileNamePrefix());
        instaStorageFolderTF.setText(config.getStorageFolder());
        if (config.getMaxNumberOfImages() > 0) {
            instaNumberOfFetchedImages.setText(String.valueOf(config.getMaxNumberOfImages()));
        } else {
            instaNumberOfFetchedImages.clear();
        }
    }
}
