package com.legec.imageCrowler.utils;

import com.sun.istack.internal.NotNull;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by hubert.legec on 2016-03-12.
 */
public class PreferenceService {

    private static String DEFAULT_CONFIG_FILE = "crawlerConfig.xml";

    public static void saveToXMLFile(@NotNull  GlobalConfig config, String fileName) throws Exception{
        XMLEncoder encoder =
                new XMLEncoder(
                        new BufferedOutputStream(
                                new FileOutputStream(checkFileName(fileName))));
        encoder.writeObject(config);
        encoder.close();
    }

    public static GlobalConfig loadFromXMLFile(String fileName) throws Exception{
        XMLDecoder decoder =
                new XMLDecoder(new BufferedInputStream(
                        new FileInputStream(checkFileName(fileName))));
        GlobalConfig config = (GlobalConfig) decoder.readObject();
        decoder.close();
        return config;
    }

    public static void setDefaultConfigFile(String defaultConfigFile) {
        DEFAULT_CONFIG_FILE = defaultConfigFile;
    }

    private static String checkFileName(String fileName){
        if(fileName != null && fileName.length() > 0){
            return fileName;
        }

        return DEFAULT_CONFIG_FILE;
    }
}
