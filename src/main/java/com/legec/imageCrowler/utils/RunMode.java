package com.legec.imageCrowler.utils;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public enum RunMode {
    DEFAULT("default"),
    ALL("all"),
    INSTA_ONLY("insta_only"),
    WEB_ONLY("web_only");

    private String modeName;

    RunMode(String modeName){
        this.modeName = modeName;
    }

    public String toString() {
        return this.modeName;
    }
}
