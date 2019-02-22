package com.adamdabrowski.game;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logic {
    private static Logic instance = new Logic();

    // Server state
    private final Queue<String> chat;
    boolean isLoggedIn;
    boolean isInLobby;

    // Client state
    boolean isChatOpen;


    public static Logic getInstance() { return instance; }

    private Logic() {
        chat = new LinkedBlockingQueue<String>(5);
        isLoggedIn = false;
        isInLobby = false;

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
