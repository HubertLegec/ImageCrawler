package com.legec.imageCrowler.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class DownloadImageFromURLTask implements Callable<File> {

    private URL url;
    private String path;

    public DownloadImageFromURLTask(URL url, String path) {
        this.url = url;
        this.path = path;
    }

    @Override
    public File call() throws Exception {

        BufferedImage img = ImageIO.read(url);
        String name = url.getPath();
        name = name.substring(name.lastIndexOf("/"));
        String extension = name.substring(name.lastIndexOf("."));
        if(extension == null || extension.length() == 0){
            extension = "jpg";
        }

        File output = new File(path, name);
        ImageIO.write(img, extension, output);

        return output;
    }

}
