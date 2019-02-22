package com.adamdabrowski.server;

import java.io.Serializable;

public class Position implements Serializable {
    public float x;
    public float y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
