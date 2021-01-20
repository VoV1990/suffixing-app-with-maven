package com.epam.tools;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLWriter {
    static final Logger logger = LogManager.getLogger(XMLWriter.class);
    private final String path;
    private String configLocation;
    private List<File> oldFilesList = new ArrayList<>();
    private List<File> newFilesList = new ArrayList<>();
    private long renamingTime;

    public XMLWriter(String path) {
        this.path = path;
    }

    public void reportWriter() {
        logger.info("Creating report...");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
            Element root = document.createElement("report");
            document.appendChild(root);
            Element config = document.createElement("config");
            config.appendChild(document.createTextNode(configLocation));
            root.appendChild(config);
            Element executionTime = document.createElement("execution_time");
            executionTime.appendChild(document.createTextNode(renamingTime + " milliseconds"));
            root.appendChild(executionTime);
            Element files = document.createElement("files");
            root.appendChild(files);
            Element oldFiles = document.createElement("oldFiles");
            files.appendChild(oldFiles);
            createFileElement(oldFilesList, document, oldFiles);
            Element newFiles = document.createElement("new_files");
            files.appendChild(newFiles);
            createFileElement(newFilesList, document, newFiles);
            Transformer transformer = getTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(path + "report.xml"));
            transformer.transform(source, result);
            logger.info("Report created");
        } catch (ParserConfigurationException | TransformerException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        }
    }

    private Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        return transformer;
    }

    private void createFileElement(List<File> filesList, Document doc, Element parent) {
        Element file;
        for(File f : filesList) {
            file = doc.createElement("file");
            file.appendChild(doc.createTextNode(f.getPath()));
            parent.appendChild(file);
        }
    }

    public void setOldFilesList(List<File> oldFilesList) {
        this.oldFilesList = oldFilesList;
    }

    public void setNewFilesList(List<File> newFilesList) {
        this.newFilesList = newFilesList;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public void setRenamingTime(long renamingTime) {
        this.renamingTime = renamingTime;
    }
}
