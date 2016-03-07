package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class Tag {
    @SerializedName("media_count")
    @Expose
    public Integer mediaCount;
    @SerializedName("name")
    @Expose
    public String name;
}
