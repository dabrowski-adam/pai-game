package com.adamdabrowski.server;

public class XMLConfig {
    private static XMLConfig ourInstance = new XMLConfig();

    public static XMLConfig getInstance() {
        return ourInstance;
    }

    private XMLConfig() {
    }
}
