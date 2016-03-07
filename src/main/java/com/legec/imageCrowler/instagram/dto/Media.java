package com.legec.imageCrowler.instagram.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class Media {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("users_in_photo")
    @Expose
    public Object usersInPhoto;
    @SerializedName("filter")
    @Expose
    public String filter;
    @SerializedName("tags")
    @Expose
    public List<String> tags = new ArrayList<>();
    @SerializedName("comments")
    @Expose
    public Comments comments;
    @SerializedName("caption")
    @Expose
    public Caption caption;
    @SerializedName("likes")
    @Expose
    public Likes likes;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("created_time")
    @Expose
    public String createdTime;
    @SerializedName("images")
    @Expose
    public Images images;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("location")
    @Expose
    public Object location;
    @SerializedName("videos")
    @Expose
    public Videos videos;

}