package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.utils.RestClient;
import com.legec.imageCrowler.instagram.dto.MediaCollection;
import com.legec.imageCrowler.instagram.dto.SimilarTags;
import com.legec.imageCrowler.instagram.dto.Tag;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class InstagramApi {
    private RestClient restClient;
    private String token;

    public InstagramApi(String token) {
        this.token = token;
        this.restClient = new RestClient("https://api.instagram.com/v1");
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Tag> getSimilarTags(String tag){
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        params.put("q", tag);

        SimilarTags response = (SimilarTags) restClient.executeGetToJSONWithParams("tags/search", SimilarTags.class, params);

        return response.data;
    }

    public List<String> getImageURLs(String tag, int howMuch){
        List<String> result = new LinkedList<>();
        Map<String, String> params = new HashMap<>();
        params.put("access_token", token);
        if(howMuch > 0){
            params.put("count", Integer.toString(howMuch));
        }

        MediaCollection response = (MediaCollection) restClient.executeGetToJSONWithParams("tags/" + tag + "/media/recent", MediaCollection.class, params);

        response.data.stream().filter( el -> el.type.equals("image")).forEach(el -> result.add(el.images.standardResolution.url));

        return result;
    }
}
