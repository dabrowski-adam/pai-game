package com.adamdabrowski.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Runnable {
    StateManager stateManager;
    Map<Player, Session> playerSessions = new HashMap<>();
    List<Player> players = new ArrayList<>();
    float groundRadius = 100f;

    float timeElapsed = 0f;

    public Game() {
        stateManager = StateManager.getInstance();
    }

    private void End() {
        stateManager.isGame = false;

        for (Player player : players) {
            if (player.isAlive) {
                stateManager.WriteToChat(player.name + " is the winner!");
            }
        }
    }

    public void Start(List<Session> sessions) {
        stateManager.isGame = true;

        timeElapsed = 0f;
        groundRadius = 100f; // TODO: Load from config
        players.clear();
        playerSessions.clear();

        for (Session session : sessions) {
            Player player = new Player(session.name, 10f, 10f);
            player.isAlive = true;
            players.add(player); // TODO: Randomize positions
            playerSessions.put(player, session);
        }
    }

    private void Update(long deltaTime) {
        // TODO: Load from config
        if (stateManager.lobby.size() >= 2 && !stateManager.isGame) {
            Start(stateManager.lobby);
        }

        // Check if game is active
        if (!stateManager.isGame) {
            return;
        }

        // Check if someone won
        int aliveCount = 0;
        for (Player player : players) {
            if (player.isAlive) { aliveCount++; }
        }

        if (aliveCount == 1) {
            // TODO: Send winning message
            End();
        }

        for (Player player : players) {
            // Update cooldowns
            player.timeSinceAttack += deltaTime;

            // Kill players out of bounds
            float x = player.position.x;
            float y = player.position.y;

            if (x * x + y * y > groundRadius * groundRadius) {
                player.isAlive = false;
            }

            // React to player actions
            if (player.isAlive) {
                Session session = playerSessions.get(player);
                if (session != null) {
                    boolean goLeft = false;
                    boolean goRight = false;
                    boolean goUp = false;
                    boolean goDown = false;
                    boolean attack = false;

                    synchronized (session.inputQueue) {
                        for (Message msg : session.inputQueue) {
                            if (!goLeft && msg.type == MessageType.GO_LEFT) {
                                goLeft = true;
                            }
                            if (!goRight && msg.type == MessageType.GO_RIGHT) {
                                goRight = true;
                            }
                            if (!goUp && msg.type == MessageType.GO_UP) {
                                goUp = true;
                            }
                            if (!goDown && msg.type == MessageType.GO_DOWN) {
                                goDown = true;
                            }
                            if (!attack && msg.type == MessageType.ATTACK) {
                                attack = true;
                            }
                        }
                        session.inputQueue.clear();
                    }

                    float speed = XMLConfig.getInstance().speed;
                    if (goLeft) {
                        player.position.x -= speed; // TODO: Get speed from config
                    }
                    if (goRight) {
                        player.position.x += speed;
                    }
                    if (goUp) {
                        player.position.y += speed;
                    }
                    if (goDown) {
                        player.position.y -= speed;
                    }
                    if (attack && player.timeSinceAttack > XMLConfig.getInstance().cooldown) { // TODO: Load from config
                        player.timeSinceAttack = 0;

                        for (Player anotherPlayer : players) {
                            if (anotherPlayer == player || !anotherPlayer.isAlive) {
                                continue;
                            }

                            // Check for collision
                            float attackRadius = XMLConfig.getInstance().radius; // TODO: Load from config
                            float x2 = anotherPlayer.position.x;
                            float y2 = anotherPlayer.position.y;

                            if ((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y) < attackRadius * attackRadius) {
                                anotherPlayer.isAlive = false;
                            }
                        }
                    }
                }
            }
        }

        // Shrink ground
        groundRadius = 100f - (timeElapsed / 30000) * 100f;

        // Temporary reset
        if (groundRadius < 10) { End(); }

        // Send updated state
        List<Player> playersClone = new ArrayList<>();
        for (Player player : players) {
            Player playerClone = new Player(player.name, player.position.x, player.position.y);
            playerClone.isAlive = player.isAlive;
            playerClone.timeSinceAttack = player.timeSinceAttack;
            playersClone.add(playerClone);
        }
        stateManager.SendGameState(new GameState(playersClone, groundRadius));
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            long deltaTime = 1000 / 30;
            try {
                Update(deltaTime);

                Thread.sleep(deltaTime); // TODO: Get tickrate from config
                timeElapsed += deltaTime;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

