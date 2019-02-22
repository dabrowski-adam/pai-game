package com.adamdabrowski.server;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    private static StateManager instance = new StateManager();
    private static List<Session> listeners = new ArrayList<>();

    public List<Session> lobby = new ArrayList<>();
    public boolean isGame = false;

    public static StateManager getInstance() { return instance; }

    private StateManager() { }

    public synchronized void registerListener(Session session) {
        if (!listeners.contains(session)) {
            listeners.add(session);
        }
    }

    public synchronized void removeListener(Session session) {
        listeners.remove(session);
    }

    public void WriteToChat(String message) {
        for (Session session : listeners) {
            session.sendChatMessage(message);
        }
    }

    public void SendGameState(GameState gameState) {
        for (Session session : listeners) {
            session.sendGameState(gameState);
        }
    }

    public synchronized void JoinLobby(Session session) {
        if (!lobby.contains(session)) {
            lobby.add(session);
        }
    }

    public synchronized void LeaveLobby(Session session) {
        lobby.remove(session);
    }
}
