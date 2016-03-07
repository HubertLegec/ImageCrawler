package com.legec.imageCrowler.instagram;

import com.legec.imageCrowler.instagram.dto.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by hubert.legec on 2016-03-07.
 */
public class InstagramApiTest {

    @Test
    public void getSimilarTagsTest() {
        InstagramApi api = new InstagramApi("1935229135.1677ed0.43af0f66a71d4653857c8a6e5e8866e3");

        List<Tag> response = api.getSimilarTags("polish");

        assertTrue(response.size() > 0);
        response.forEach( el -> assertTrue(el.name.contains("polish")));
    }

    @Test
    public void getTaggedImageUrlsTest(){
        InstagramApi api = new InstagramApi("1935229135.1677ed0.43af0f66a71d4653857c8a6e5e8866e3");

        List<String> response = api.getImageURLs("warsaw", 100);

        assertTrue(response.size() > 0);
        response.forEach( el -> System.out.println(el) );


    }
}
