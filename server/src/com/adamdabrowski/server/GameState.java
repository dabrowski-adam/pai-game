package com.adamdabrowski.server;

import java.util.List;

public class GameState {
    public final List<Player> players;
    public final float radius;

    public GameState(List<Player> players, float radius) {
        this.players = players;
        this.radius = radius;
    }
}
