package com.epam.suffixingApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FileDownloader {
    private List<String> filePaths;
    private String suffix;

    public FileDownloader() {
        fileDownloading();
    }

    private void fileDownloading() {
//        FileInputStream fileInputStream;
        Properties property = new Properties();
        Set<Object> set;
        String resource = "/config.properties";
        suffix = null;
        filePaths = new ArrayList<>();
        try(InputStream input = getClass().getResourceAsStream(resource)) {
//            fileInputStream = new FileInputStream("src/main/resources/config.properties");
//            property.load(fileInputStream);
            property.load(input);
            set = property.keySet();
            for (Object s : set) {
                String key = (String) s;
                if (key.equals("suffix")) {
                    suffix = property.getProperty(key);
                } else {
                    String filePath = property.getProperty(key);
                    if(isExist(filePath))
                        filePaths.add(filePath);
                }
            }
        } catch (IOException e) {
            System.out.println("Resource wasn't found.");
            e.printStackTrace();
        }
    }

    private static boolean isExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public String getSuffix() {
        return suffix;
    }
}
