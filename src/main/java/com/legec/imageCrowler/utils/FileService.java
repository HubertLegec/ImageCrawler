package com.legec.imageCrowler.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FileService {

    public static void saveImageFilesWithCustomName(String storageFolder, Collection<String> urls, String namePrefix) throws IOException {
        int i = 0;
        for (String u : urls) {
            i++;
            URL url = new URL(u);
            BufferedImage img = ImageIO.read(url);
            String name = url.getPath();
            name = name.substring(name.lastIndexOf("/"));
            String extension = name.substring(name.lastIndexOf(".") + 1);
            if (extension == null || extension.length() == 0) {
                extension = "jpg";
            }
            File output = new File(storageFolder, namePrefix + i + "." + extension);
            ImageIO.write(img, extension, output);
        }
    }

    public static void saveImageFiles(String storageFolder, Collection<String> urls) throws IOException {
        for (String u : urls) {
            URL url = new URL(u);
            BufferedImage img = ImageIO.read(url);
            String name = url.getPath();
            name = name.substring(name.lastIndexOf("/") + 1);
            String extension = name.substring(name.lastIndexOf(".") + 1);
            if (extension == null || extension.length() == 0) {
                extension = "jpg";
            }
            File output = new File(storageFolder, name);
            ImageIO.write(img, extension, output);
        }
    }
}
