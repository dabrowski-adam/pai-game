package com.adamdabrowski.game;

import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class GameInput implements InputProcessor {
    Logic logic;
    ActionQueue actionQueue;
    ChatInput chatInput;

    public GameInput(Logic logic, ActionQueue actionQueue, ChatInput chatInput) {
        this.logic = logic;
        this.actionQueue = actionQueue;
        this.chatInput = chatInput;
    }

    @Override
    public boolean keyDown(int keycode) {
        // Chat input
        if (!logic.isChatOpen && keycode == Input.Keys.ENTER) {
            logic.OpenChat();

            if (!logic.isLoggedIn) {
                Gdx.input.getTextInput(chatInput, "Enter nickname", "John Smith", "...");
            } else {
                Gdx.input.getTextInput(chatInput, "Send chat message", "Hello!", "...");
            }
        }

        // Lobby toggle
        if (keycode == Input.Keys.SPACE) {
            if (logic.isInLobby) {
                actionQueue.QueueAction(new Message(MessageType.LOBBY_LEAVE, ""));
            } else {
                actionQueue.QueueAction(new Message(MessageType.LOBBY_JOIN, ""));
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
