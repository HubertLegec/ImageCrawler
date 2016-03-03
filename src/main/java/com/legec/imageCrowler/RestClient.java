package com.legec.imageCrowler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;

/**
 * Created by Hubert on 03.03.2016.
 */
public class RestClient {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;
    private String baseUrl;


    public RestClient() {
        this.client = new OkHttpClient();
    }

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = new OkHttpClient();
    }

    public String executeGet(String url) throws IOException {
        Request request = new Request.Builder().url(prepareUrl(url)).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String executePost(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(prepareUrl(url))
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public Object executeGetToJSON(String url, Class classToReturn) {
        try {
            String result = executeGet(url);
            return mapToObject(result, classToReturn);
        } catch (IOException e) {
            return null;
        }
    }

    private Object mapToObject(String json, Class classToReturn) {
        Gson gson = new GsonBuilder().create();
        Object result = gson.fromJson(json, classToReturn);
        return result;
    }

    private String prepareUrl(String url) {
        if (baseUrl == null || baseUrl.length() == 0) {
            return url;
        } else {
            return baseUrl + "/" + url;
        }
    }

}