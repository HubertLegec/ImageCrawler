package com.legec.imageCrowler.flickr.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by hubert.legec on 2016-03-13.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "owner",
        "secret",
        "server",
        "farm",
        "title",
        "ispublic",
        "isfriend",
        "isfamily"
})
public class Photo {

    @JsonProperty("id")
    public String id;
    @JsonProperty("owner")
    public String owner;
    @JsonProperty("secret")
    public String secret;
    @JsonProperty("server")
    public String server;
    @JsonProperty("farm")
    public Integer farm;
    @JsonProperty("title")
    public String title;
    @JsonProperty("ispublic")
    public Integer ispublic;
    @JsonProperty("isfriend")
    public Integer isfriend;
    @JsonProperty("isfamily")
    public Integer isfamily;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
