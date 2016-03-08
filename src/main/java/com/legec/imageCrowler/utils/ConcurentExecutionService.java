package com.legec.imageCrowler.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hubert.legec on 2016-03-08.
 */
public class ConcurentExecutionService {

    public static void saveImagesFromURLS(Collection<String> urls, int numberOfThread, String destinationFolder) throws MalformedURLException, InterruptedException {
        List<DownloadImageFromURLTask> listOfTasks = new ArrayList<>(urls.size());
        try {
            Files.createDirectories(Paths.get(destinationFolder));
            for (String url : urls) {
                listOfTasks.add(new DownloadImageFromURLTask(new URL(url), destinationFolder));
            }

            ExecutorService exector = Executors.newFixedThreadPool(numberOfThread);
            List<Future<File>> listOfFutures = exector.invokeAll(listOfTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
