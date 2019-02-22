package com.adamdabrowski.game;

import com.adamdabrowski.server.GameState;
import com.adamdabrowski.server.Player;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logic {
    private static Logic instance = new Logic();

    // Server state
    private final Queue<String> chat;
    boolean isLoggedIn;
    boolean isInLobby;
    GameState gameState;

    // Client state
    boolean isChatOpen;

    public static Logic getInstance() { return instance; }

    private Logic() {
        chat = new LinkedBlockingQueue<String>(5);
        isLoggedIn = false;
        isInLobby = false;
        gameState = new GameState(new ArrayList<Player>(), 100f); // TODO: Get from config

        isChatOpen = false;
    }

    public void Update(float deltaTime) {

    }

    public void DisplayMessage(String message) {
        if (chat.size() == 5) {
            chat.remove();
        }
        chat.add(message);
    }

    public void UpdateGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState GetGameState() {
        return this.gameState;
    }

    public String GetMessages() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String msg : chat) {
            stringBuilder.append(msg).append("\n");
        }
        return stringBuilder.toString();
    }

    public void OpenChat() { isChatOpen = true; }

    public void CloseChat() { isChatOpen = false; }
}
