package com.adamdabrowski.server;

public class ClassConfig implements IConfig {
    @Override
    public int GetPort() {
        return 10000;
    }

    @Override
    public String GetHost() {
        return "127.0.0.1";
    }
}
