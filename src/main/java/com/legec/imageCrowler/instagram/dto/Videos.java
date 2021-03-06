package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class Videos {
    @SerializedName("low_resolution")
    @Expose
    public Resolution lowResolution;
    @SerializedName("standard_resolution")
    @Expose
    public Resolution standardResolution;
}
