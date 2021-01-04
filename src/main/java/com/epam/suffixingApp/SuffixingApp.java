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
        FileDownloader downloader = new FileDownloader(args[0]);
        if(!downloader.getFilePaths().isEmpty() && downloader.getSuffix() != null)
            fileRenaming(downloader.getFilePaths(), downloader.getSuffix());
        else
            System.out.println("Invalid data");
    }

    private static void fileRenaming(List<String> filePaths, String suffix) {
        File oldFile;
        String simpleName;
        String[]partsOfName;
        String newFileName;
        for (String filePath : filePaths) {
            oldFile = new File(filePath);
            simpleName = oldFile.getName();
            partsOfName = simpleName.split("_");
            if (partsOfName[0] != null) {
                partsOfName[0] = suffix;
                newFileName = partsOfName[0] + "_" + partsOfName[1];
                try {
                    Files.move(oldFile.toPath(), oldFile.toPath().resolveSibling(newFileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                oldFile = new File(oldFile.getParent(), newFileName);
                System.out.println(simpleName + "->" + newFileName);
            }
        }
    }
}
