package com.epam.suffixing_app;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.epam.tools.XMLReader;
import com.epam.tools.XMLWriter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * App Specification
 *
 * It is a Suffixing App - a small java application that refers to a config file and renames a set of files and renames them adding a suffix specified in the same config.
 *
 * Changes: config file now should be an XML file.
 *
 * Details:
 *
 * Application should read a config file on the startup
 * Then it should ensure that all of files from the config exist
 * Then it should rename each file adding a suffix from the config to its name
 *
 * Logging Specification:
 *
 * Application should log startup information.
 * Application should log information on config read.
 * Application should log renaming process information.
 * Application should log summary information.
 * Application should log shutdown information.
 * Application should handle and log possible errors.
 * Use different logging level. All log entries should contain a date and time information as well.
 *
 *  Changes:
 *
 *  When renaming is finished the application should print a document of completed actions.
 *  Document should be XML-based. It should contain:
 *  - config file name
 *  - execution time
 *  - list of files with old and new names
 *  All the logging entries from previous exercise should become JSON document of some structure. They should contain:
 *  - date and time
 *  - message
 *  - severity label
 *  - error info, if its error
*/

public class SuffixingApp {
    private static final Logger logger = LogManager.getLogger(SuffixingApp.class);
    private static final List<File> newFiles = new ArrayList<>();
    private static boolean successful = false;
    private static long startAppTime;
    private static long executionTime;

    public static void main( String[] args ) {
        startAppTime = System.currentTimeMillis();
        logger.info("The app is running...");
        XMLReader xmlReader = null;
        XMLWriter xmlWriter;
        try {
            logger.info("Downloading the configuration file...");
            xmlReader = new XMLReader(args[0]);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.log(Level.ERROR, "Exception: ", ex);
        }
        assert xmlReader != null;
        if(!xmlReader.getFiles().isEmpty() && xmlReader.getSuffix() != null) {
            logger.info("Renaming the files...");
            fileRenaming(xmlReader.getFiles(), xmlReader.getSuffix());
        } else {
            try {
                throw new FileNotFoundException("No files found");
            } catch (Exception ex) {
                System.err.println("No files found");
                logger.log(Level.ERROR, "Exception: ", ex);
            }
        }
        if(successful) {
            xmlWriter = new XMLWriter(xmlReader.getAppLocation());
            xmlWriter.setOldFilesList(xmlReader.getFiles());
            xmlWriter.setNewFilesList(newFiles);
            xmlWriter.setConfigLocation(args[0]);
            xmlWriter.setRenamingTime(executionTime);
            xmlWriter.reportWriter();
        }
        logger.info("The app has shut down");
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
                    Path newFilePath = Files.move(oldFile.toPath(), oldFile.toPath().resolveSibling(newFileName));
                    executionTime = System.currentTimeMillis() - startAppTime;
                    System.out.println(simpleName + "->" + newFileName);
                    String message = String.format("File %s renamed", simpleName);
                    successful = true;
                    newFiles.add(new File(newFilePath.toString()));
                    logger.info(message);
                } catch (IOException e) {
                    System.err.println("Files are not renamed");
                    logger.log(Level.ERROR, "Exception: ", e);
                }
            }
        }
    }
}
