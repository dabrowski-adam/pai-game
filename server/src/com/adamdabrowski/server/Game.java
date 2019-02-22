package com.adamdabrowski.server;

import java.util.ArrayList;
import java.util.List;

public class Game implements Runnable {
    StateManager stateManager;
    List<Player> players = new ArrayList<>();
    float groundRadius = 100f;

    public Game() {
        stateManager = StateManager.getInstance();
    }

    private void Reset() {
        players.clear();
        groundRadius = 100f; // TODO: Load from config

        stateManager.isGame = false;
    }

    private void Update(long deltaTime) {
        // Check if someone won
        int aliveCount = 0;
        for (Player player : players) {
            if (!player.isAlive) { aliveCount++; }
        }

        if (aliveCount == 1 - 1) {
            // TODO: Send winning message
            Reset();
        }

        // React to player actions

        // Send updated state
        stateManager.SendGameState(new GameState(new ArrayList<>(), groundRadius));
    }

    @Override
    public void run() {
        long deltaTime = 1000 / 30;
        try {
            Thread.sleep(deltaTime); // TODO: Get tickrate from config
            Update(deltaTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

