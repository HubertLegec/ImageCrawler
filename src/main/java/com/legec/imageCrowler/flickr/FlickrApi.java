package com.legec.imageCrowler.flickr;

import com.legec.imageCrowler.flickr.dto.Photo;
import com.legec.imageCrowler.flickr.dto.Photos;
import com.legec.imageCrowler.flickr.dto.ResultSet;
import com.legec.imageCrowler.utils.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public class FlickrApi {
    private RestClient restClient;
    private String token;


    public FlickrApi() {
        this.restClient = new RestClient("https://api.flickr.com/services");
    }

    public FlickrApi(String token) {
        this.token = token;
        this.restClient = new RestClient("https://api.flickr.com/services");
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getListOfUrlsByText(String text, int howMuch){
        Photos photos = getPhotosByText(text, howMuch);
        return mapPhotosToUrls(photos);
    }

    public List<String> getListOfUrlsByTags(List<String> tags, int howMuch){
        Photos photos = getPhotosByTags(tags, howMuch);
        return mapPhotosToUrls(photos);
    }

    private Photos getPhotosByText(String text, int howMuch) {
        Map<String, String> params = new HashMap<>();
        params.put("format", "json");
        params.put("method", "flickr.photos.search");
        params.put("api_key", token);
        params.put("text", text);
        if(howMuch > 0 && howMuch <= 500) {
            params.put("per_page", String.valueOf(howMuch));
        }

        ResultSet resultSet = (ResultSet) restClient.executeGetToJSONWithParams("rest", ResultSet.class, params);
        return resultSet.photos;
    }

    private Photos getPhotosByTags(List<String> tags, int howMuch) {
        Map<String, String> params = new HashMap<>();
        params.put("format", "json");
        params.put("method", "flickr.photos.search");
        params.put("api_key", token);

        StringBuilder builder = new StringBuilder();
        tags.forEach(tag -> builder.append(tag).append(","));
        if(builder.length() > 0) {
            builder.deleteCharAt(builder.length()-1);
        }

        params.put("tags", builder.toString());

        if(howMuch > 0 && howMuch <= 500) {
            params.put("per_page", String.valueOf(howMuch));
        }

        ResultSet resultSet = (ResultSet) restClient.executeGetToJSONWithParams("rest", ResultSet.class, params);
        return resultSet.photos;
    }

    private List<String> mapPhotosToUrls(Photos photos){
        return photos.photo.stream().map( photo ->  buildUrlForPhoto(photo)).collect(Collectors.toList());
    }

    private String buildUrlForPhoto(Photo photo){
        StringBuilder builder = new StringBuilder();
        builder.append("https://farm");
        builder.append(photo.farm);
        builder.append(".staticflickr.com/");
        builder.append(photo.server);
        builder.append("/");
        builder.append(photo.id);
        builder.append("_");
        builder.append(photo.secret);
        builder.append(".jpg");

        return builder.toString();
    }
}
