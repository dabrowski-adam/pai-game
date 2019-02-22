package com.adamdabrowski.server;

import java.io.Serializable;

public class Player implements Serializable {
    public String name;
    public boolean isAlive = true;
    public Position position;
    public long timeSinceAttack = 0;

    public Player(String name, float x, float y) {
        this.name = name;
        position = new Position(x, y);
    }
}
