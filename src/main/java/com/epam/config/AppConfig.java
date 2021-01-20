package com.epam.config;

import java.io.File;
import java.util.List;

public class AppConfig {
    private String suffix;
    private List<File> files;

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
