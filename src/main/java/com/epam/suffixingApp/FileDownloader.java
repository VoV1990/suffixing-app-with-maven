package com.epam.suffixingApp;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class FileDownloader {
    private String resource;
    private List<File> files;
    private String suffix;
    private String fileName;

    public FileDownloader(String resource) {
        this.resource = resource;
        fileDownloading();
    }

    private void fileDownloading() {
        SuffixingApp.getLogger().fine("Download completed");
        Properties property = new Properties();
        Set<Object> set;
        files = new ArrayList<>();
        try(FileInputStream input = new FileInputStream(resource)) {
            SuffixingApp.getLogger().info("Reading the configuration file...");
            property.load(input);
            set = property.keySet();
            SuffixingApp.getLogger().fine("The configuration file has been read");
            for (Object s : set) {
                String key = (String) s;
                if (key.equals("suffix")) {
                    if(suffix == null)
                    suffix = property.getProperty(key);
                } else {
                    fileName = property.getProperty(key);
                    isExist();
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read config");
            SuffixingApp.getLogger().log(Level.WARNING, "Exception: ", e);
        }
    }

    private void isExist() {
        File file = new File(fileName);
        if (file.exists()) {
            files.add(file);
        } else {
            SuffixingApp.getLogger().info("File " + fileName + " doesn't exist");
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public String getSuffix() {
        return suffix;
    }
}
