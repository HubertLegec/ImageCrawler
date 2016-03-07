package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class User {

    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("profile_picture")
    @Expose
    public String profilePicture;
    @SerializedName("id")
    @Expose
    public String id;

}