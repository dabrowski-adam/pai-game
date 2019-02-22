package com.adamdabrowski.server;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLConfig {
    private static XMLConfig ourInstance = new XMLConfig();

    public static XMLConfig getInstance() {
        return ourInstance;
    }

    public int port;
    public String host;
    public int cooldown;
    public float radius;
    public float speed;

    private XMLConfig() {
        File file = new File("config.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            String port = document.getElementsByTagName("port").item(0).getTextContent();
            String host = document.getElementsByTagName("host").item(0).getTextContent();
            String cooldown = document.getElementsByTagName("cooldown").item(0).getTextContent();
            String radius = document.getElementsByTagName("radius").item(0).getTextContent();
            String speed = document.getElementsByTagName("speed").item(0).getTextContent();

            this.port = Integer.parseInt(port);
            this.host = host;
            this.cooldown = Integer.parseInt(cooldown);
            this.radius = Float.parseFloat(radius);
            this.speed = Float.parseFloat(speed);
        } catch (ParserConfigurationException|SAXException|IOException e) {
            e.printStackTrace();
        }
    }
}
