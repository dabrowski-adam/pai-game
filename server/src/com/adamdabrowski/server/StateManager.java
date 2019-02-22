package com.adamdabrowski.server;

import java.util.ArrayList;
import java.util.List;

public class StateManager {
    private static StateManager instance = new StateManager();
    private static List<Session> listeners = new ArrayList<>();

    public static StateManager getInstance() { return instance; }

    private  StateManager() { }

    public synchronized void registerListener(Session session) {
        listeners.add(session);
    }

    public synchronized void removeListener(Session session) {
        listeners.remove(session);
    }

}
