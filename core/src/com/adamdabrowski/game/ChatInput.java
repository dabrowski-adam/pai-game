package com.adamdabrowski.game;

import com.badlogic.gdx.Input;

import java.util.Queue;

public class ChatInput implements Input.TextInputListener {
    Queue<String> messages;

    public ChatInput(Queue<String> messages) {
        this.messages = messages;
    }

    @Override
    public void input(String text) {
        messages.add(text);
        Logic.getInstance().CloseChat();
    }

    @Override
    public void canceled() {
        Logic.getInstance().CloseChat();
    }
}
