package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class MediaCollection {
    @SerializedName("data")
    @Expose
    public List<Media> data = new ArrayList<>();
}
