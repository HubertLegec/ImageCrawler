package com.legec.imageCrowler.flickr;

import com.legec.imageCrowler.utils.DialogService;
import com.legec.imageCrowler.utils.GlobalConfig;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FlickrTabController {
    @FXML
    private Button runButton;
    @FXML
    private ListView<String> tagList;
    @FXML
    private ToggleSwitch searchSwitch;
    @FXML
    private TextField searchText;
    @FXML
    private Button addTag;
    @FXML
    private Button removeTag;
    @FXML
    private TextField tokenTF;
    @FXML
    private TextField storageFolderTF;
    @FXML
    private TextField fileNamePrefixTF;
    @FXML
    private TextField maxNumberOfImagesTF;
    @FXML
    private ProgressIndicator progressIndicator;

    private FlickrCrawlController flickrCrawlController = new FlickrCrawlController();
    private Stage primaryStage;
    private ObservableList<String> tagObservableList = FXCollections.observableArrayList();
    private BooleanProperty tagListEmpty = new SimpleBooleanProperty(true);
    private boolean flickrWorks = false;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        runButton.disableProperty().bind(storageFolderTF.textProperty().isEmpty().or(tokenTF.textProperty().isEmpty())
                .or(searchSwitch.selectedProperty().and(searchText.textProperty().isEmpty())).or(tagListEmpty.and(searchSwitch.selectedProperty().not())));
        tagList.setItems(tagObservableList);
        removeTag.disableProperty().bind(tagList.getSelectionModel().selectedItemProperty().isNull().or(searchSwitch.selectedProperty()));
        addTag.disableProperty().bind(searchSwitch.selectedProperty());
        tagList.disableProperty().bind(searchSwitch.selectedProperty());
        searchText.disableProperty().bind(searchSwitch.selectedProperty().not());
    }


    @FXML
    private void onRun() {
        if (!flickrWorks) {
            saveValues();
            flickrCrawlController.init();
            flickrCrawlController.startWithCallback(() -> {
                Platform.runLater(() -> runButton.setText("Run"));
                flickrWorks = false;
                progressIndicator.setVisible(false);
            });
            flickrWorks = true;
            progressIndicator.setVisible(true);
            runButton.setText("Cancel");
        } else {
            flickrCrawlController.stop();
        }
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
    private void onStorageFolder() {
        File dir = DialogService.showStorageDirectoryDialog(primaryStage.getScene().getWindow());
        if (dir != null) {
            storageFolderTF.setText(dir.getPath());
        }
    }

    public void loadValues() {
        FlickrConfig config = GlobalConfig.getInstance().getFlickrConfig();
        tagObservableList.setAll(config.getTags());
        if (tagObservableList.size() > 0) {
            tagListEmpty.setValue(false);
        }
        tokenTF.setText(config.getToken());
        searchSwitch.setSelected(!config.isByTag());
        fileNamePrefixTF.setText(config.getFileNamePrefix());
        storageFolderTF.setText(config.getStorageFolder());
        if (config.getMaxNumberOfImages() > 0) {
            maxNumberOfImagesTF.setText(String.valueOf(config.getMaxNumberOfImages()));
        } else {
            maxNumberOfImagesTF.clear();
        }
    }

    public void saveValues() {
        FlickrConfig config = GlobalConfig.getInstance().getFlickrConfig();
        config.setTags(tagObservableList.stream().collect(Collectors.toList()));
        config.setToken(tokenTF.getText());
        config.setText(searchText.getText());
        config.setFileNamePrefix(fileNamePrefixTF.getText());
        config.setStorageFolder(storageFolderTF.getText());
        config.setByTag(!searchSwitch.isSelected());
        if (maxNumberOfImagesTF.getText() != null && maxNumberOfImagesTF.getText().length() > 0) {
            config.setMaxNumberOfImages(Integer.valueOf(maxNumberOfImagesTF.getText()));
        } else {
            config.setMaxNumberOfImages(-1);
        }
    }
}
