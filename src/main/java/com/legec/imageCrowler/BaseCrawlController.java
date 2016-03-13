package com.legec.imageCrowler;

import com.legec.imageCrowler.utils.Callback;

/**
 * Created by hubert.legec on 2016-03-13.
 */
public interface BaseCrawlController {
    void init();
    boolean start();
    void startWithCallback(Callback callback);
    void stop();
}
