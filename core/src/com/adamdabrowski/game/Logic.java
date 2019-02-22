package com.adamdabrowski.game;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logic {
    private static Logic instance = new Logic();
    private static final Queue<String> chat;

    static {
        chat = new LinkedBlockingQueue<String>(5);
    }

    public static Logic getInstance() { return instance; }

    private Logic() { }

    public void Update(float deltaTime) {

    }

    public synchronized void DisplayMessage(String message) {
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
}
