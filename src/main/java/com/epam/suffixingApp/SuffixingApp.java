package com.epam.suffixingApp;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.*;

/*Details:

Application should read a config file on the startup
Then it should ensure that all of files from the config exist
Then it should rename each file adding a suffix from the config to its name
It should print the results of the renaming like:
old_name -> new_name

Logging Specification:

Application should log startup information.
Application should log information on config read.
Application should log renaming process information.
Application should log summary information.
Application should log shutdown information.
Application should handle and log possible errors.
Use different logging level. All log entries should contain a date and time information as well.
*/

public class SuffixingApp {
    private static Logger logger = Logger.getLogger(SuffixingApp.class.getName());

    public static void main( String[] args ) {
        try {
            LogManager.getLogManager().readConfiguration(
                    SuffixingApp.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
        logger.setLevel(Level.ALL);
        logger.info("The app is running...");
        FileDownloader downloader = null;
        try {
            logger.info("Downloading the configuration file...");
            downloader = new FileDownloader(args[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.log(Level.SEVERE, "Exception: ", ex);
        }
        assert downloader != null;
        if(!downloader.getFiles().isEmpty() && downloader.getSuffix() != null) {
            logger.info("Renaming the files...");
            fileRenaming(downloader.getFiles(), downloader.getSuffix());
        } else {
            try {
                throw new Exception("No files found");
            } catch (Exception ex) {
                System.out.println("No files found");
                logger.log(Level.WARNING, "Exception: ", ex);
            }
        }
        logger.fine("The app has shut down");
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
                    logger.fine("File " + simpleName + " renamed");
                } catch (IOException e) {
                    System.out.println("Files are not renamed");
                    logger.log(Level.WARNING, "Exception: ", e);
                }
            }
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
