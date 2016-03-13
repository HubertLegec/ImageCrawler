package com.legec.imageCrowler.utils;

import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class DialogService {
    public static Optional<String> showAddTagDialog(Window window){
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(window);
        dialog.setTitle("New tag");
        dialog.setHeaderText("Add new tag:");
        return dialog.showAndWait();
    }

    public static Optional<String> showAddURLDialog(Window window){
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(window);
        dialog.setContentText("http://");
        dialog.setTitle("New seed URL");
        dialog.setHeaderText("Add new seed URL:");
        Optional<String> result = dialog.showAndWait();
        return result.map( val -> val.startsWith("http") ? val : "http://" + val);
    }

    public static File showStorageDirectoryDialog(Window window){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Storage folder");
        return chooser.showDialog(window);
    }
}
