package com.adamdabrowski.game;

import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;
import com.badlogic.gdx.Input;

public class ChatInput implements Input.TextInputListener {
    Logic logic;
    ActionQueue actionQueue;

    public ChatInput() {
        logic = Logic.getInstance();
        actionQueue = ActionQueue.getInstance();
    }

    @Override
    public void input(String text) {
        if (logic.isLoggedIn) {
            actionQueue.QueueAction(new Message(MessageType.CHAT, text));
        } else {
            actionQueue.QueueAction(new Message(MessageType.LOGIN, text));
        }
        logic.CloseChat();
    }

    @Override
    public void canceled() {
        logic.CloseChat();
    }
}
