package com.legec.imageCrowler.flickr;

import com.legec.imageCrowler.utils.BaseConfig;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FlickrConfig extends BaseConfig{
    private String token;
    private String text;
    private boolean byTag = true;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isByTag() {
        return byTag;
    }

    public void setByTag(boolean byTag) {
        this.byTag = byTag;
    }
}
