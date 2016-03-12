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

    public void init(Stage primaryStage){
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

    public void saveValues(){
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

    public void loadValues(){
        GlobalConfig config = GlobalConfig.getInstance();
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
    }
}
