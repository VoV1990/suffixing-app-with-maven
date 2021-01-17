package com.epam.tools;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XMLReader {
    static final Logger logger = LogManager.getLogger(XMLReader.class);
    private final String config;
    private final List<File> files = new ArrayList<>();
    private String suffix;
    private String appLocation;

    public XMLReader(String config) {
        this.config = config;
        configReader();
    }

    private void configReader() {
        logger.info("Download completed");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        URL path = getClass().getProtectionDomain().getCodeSource().getLocation();
        int lastSlash = path.getFile().lastIndexOf("/");
        appLocation = path.getFile().substring(1, lastSlash + 1);
        try {
            logger.info("Reading the configuration file...");
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(config));
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("suffix");
            if(nodeList.getLength() > 0 && nodeList.item(0).getNodeType() == Node.ELEMENT_NODE) {
                suffix = nodeList.item(0).getTextContent();
            }
            nodeList = document.getElementsByTagName("file");
            if(nodeList.getLength() > 1)
                for(int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if(node.getNodeType() == Node.ELEMENT_NODE) {
                        String fileName = node.getTextContent();
                        isExist(fileName);
                    }
                }
            logger.info("The configuration file has been read");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        }
    }

    private void isExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            files.add(file);
        } else {
            String message = String.format("File %s doesn't exist", fileName);
            logger.info(message);
        }
    }

    public List<File> getFiles() {
        return files;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getAppLocation() {
        return appLocation;
    }
}
