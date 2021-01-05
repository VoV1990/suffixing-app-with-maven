package com.epam.suffixingApp;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class FileDownloader {
    private List<File> files;
    private String suffix;

    public FileDownloader() {
        fileDownloading();
    }

    private void fileDownloading() {
        Properties property = new Properties();
        Set<Object> set;
        String resource = "/config.properties";
        suffix = null;
        files = new ArrayList<>();
        try(InputStream input = getClass().getResourceAsStream(resource)) {
            property.load(input);
            set = property.keySet();
            for (Object s : set) {
                String key = (String) s;
                if (key.equals("suffix")) {
                    if(suffix == null)
                    suffix = property.getProperty(key);
                } else {
                    String filePath = property.getProperty(key);
                    isExist(filePath);
                }
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Resource wasn't found.");
            e.printStackTrace();
        }
    }

    private void isExist(String filePath) throws URISyntaxException {
        File file = null;
        URL res = getClass().getResource(filePath);
        if(res == null) {
            res = getClass().getResource(filePathChanging(filePath));
            suffix = "old";
        }
        if (res.getProtocol().equals("jar")) {
            try {
                InputStream input = getClass().getResourceAsStream(filePath);
                file = new File(filePath.substring(1));
                boolean created = file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                int read;
                byte[] bytes = new byte[1024];

                while ((read = input.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                files.add(file);
                out.close();
                file.deleteOnExit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            file = new File(res.getFile());
            files.add(file);
        }

        if (file != null && !file.exists()) {
            throw new RuntimeException("Error: File " + file + " not found!");
        }
    }

    private String filePathChanging(String filePath) {
        String[]path = filePath.split("_");
        path[0] = "/new";
        return path[0] + "_" + path[1];
    }

    public List<File> getFiles() {
        return files;
    }

    public String getSuffix() {
        return suffix;
    }
}
