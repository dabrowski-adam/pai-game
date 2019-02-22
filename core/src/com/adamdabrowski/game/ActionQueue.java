package com.adamdabrowski.game;

import com.adamdabrowski.server.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionQueue {
    private static ActionQueue ourInstance = new ActionQueue();
    private final Queue<Message> actionQueue;

    public static ActionQueue getInstance() {
        return ourInstance;
    }

    private ActionQueue() {
        actionQueue = new ConcurrentLinkedQueue<Message>();
    }

    public Queue<Message> GetActionQueue() {
        return actionQueue;
    }

    public void QueueAction(Message message) {
        actionQueue.add(message);
    }
}
