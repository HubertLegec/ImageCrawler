package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class Caption {
    @SerializedName("created_time")
    @Expose
    public String createdTime;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("from")
    @Expose
    public From from;
    @SerializedName("id")
    @Expose
    public String id;
}
