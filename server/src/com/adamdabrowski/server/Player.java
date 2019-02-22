package com.adamdabrowski.server;

public class Player {
    public boolean isAlive = true;
    public Position position;

    public Player(float x, float y) {
        position = new Position(x, y);
    }
}
