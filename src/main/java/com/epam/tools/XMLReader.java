package com.epam.tools;

import com.epam.config.AppConfig;
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
    private final String configPath;
    private AppConfig config;
    private final List<File> files = new ArrayList<>();
    private String appLocation;

    public XMLReader(String configPath) {
        this.configPath = configPath;
        configReader();
    }

    private void configReader() {
        logger.info("Download completed");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        config = new AppConfig();
        URL path = getClass().getProtectionDomain().getCodeSource().getLocation();
        int lastSlash = path.getFile().lastIndexOf("/");
        appLocation = path.getFile().substring(1, lastSlash + 1);
        try {
            logger.info("Reading the configuration file...");
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(configPath));
            document.getDocumentElement().normalize();
            getElements(document, "suffix");
            getElements(document, "file");
            config.setFiles(files);
            logger.info("The configuration file has been read");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        }
    }

    private void getElements(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        switch (tagName) {
            case "suffix":
                if(nodeList.getLength() > 0 && nodeList.item(0).getNodeType() == Node.ELEMENT_NODE)
                    config.setSuffix(nodeList.item(0).getTextContent());
                break;
            case "file":
                if(nodeList.getLength() > 0)
                    for(int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if(node.getNodeType() == Node.ELEMENT_NODE) {
                            String fileName = node.getTextContent();
                            isExist(fileName);
                        }
                    }
                break;
            default:
                throw new IllegalArgumentException("Invalid config properties");
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

    public AppConfig getConfig() {
        return config;
    }

    public String getAppLocation() {
        return appLocation;
    }
}
