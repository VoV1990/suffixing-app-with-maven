package com.epam.suffixingApp;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/*Details:

Application should read a config file on the startup
Then it should ensure that all of files from the config exist
Then it should rename each file adding a suffix from the config to its name
It should print the results of the renaming like:
old_name -> new_name
*/

public class SuffixingApp {
    public static void main( String[] args ) {
        FileDownloader downloader = new FileDownloader();
        if(!downloader.getFiles().isEmpty() && downloader.getSuffix() != null)
            fileRenaming(downloader.getFiles(), downloader.getSuffix());
        else
            System.out.println("Invalid data");
    }

    private static void fileRenaming(List<File> files, String suffix) {
        String simpleName;
        String[]partsOfName;
        String newFileName;
        for (File oldFile : files) {
            simpleName = oldFile.getName();
            partsOfName = simpleName.split("_");
            if (partsOfName.length >= 2) {
                partsOfName[0] = suffix;
                newFileName = partsOfName[0] + "_" + partsOfName[1];
                try {
                    Files.move(oldFile.toPath(), oldFile.toPath().resolveSibling(newFileName));
                    System.out.println(simpleName + "->" + newFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
