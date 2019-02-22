package com.adamdabrowski.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalTime;

public class Session implements Runnable, AutoCloseable {
    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private StateManager stateManager;

    Session(Socket client) throws IOException {
        this.client = client;
        this.input = new ObjectInputStream(client.getInputStream());
        this.output = new ObjectOutputStream(client.getOutputStream());

        stateManager = StateManager.getInstance();
    }

    @Override
    public void run() {
        String id = client.getInetAddress().toString() + "@" + LocalTime.now().toString();

        try {
            loop: for (Message msg = readMessage(); msg != null; msg = readMessage()) {
                System.out.printf("%s :: %s | %s\n", id, msg.type, msg.payload);
                switch (msg.type) {
                    case HELLO:
                        stateManager.registerListener(this);
                        break;
                    case LOGIN:
                        id = msg.payload + client.getInetAddress().toString();
                        sendMessage(new Message(MessageType.LOGGED_IN, ""));
                        break;
                    case LOBBY_JOIN:
                        stateManager.JoinLobby(this);
                        sendMessage(new Message(MessageType.LOBBY_JOIN, ""));
                        break;
                    case LOBBY_LEAVE:
                        stateManager.LeaveLobby(this);
                        sendMessage(new Message(MessageType.LOBBY_LEAVE, ""));
                        break;
                    case BYE:
                        sendMessage(new Message(MessageType.INFO, "Good bye."));
                        break loop;
                    case CHAT:
                        stateManager.WriteToChat(id + ": " + msg.payload);
                    default:
                        sendMessage(new Message(MessageType.NYI, "Not yet implemented."));
                        break;
                }

                if (Thread.interrupted()) {
                    break;
                }
            }
        } catch (EOFException e) {
            // Connection dropped.
        } catch (IOException|ClassNotFoundException e) {
            System.err.printf("Exception caught: %s\n", e);
        } finally {
            System.out.printf("%s :: Connection ended.\n", id);
            try {
                client.close();
            } catch (Exception e) {
                System.err.printf("Exception caught: %s\n", e);
            }

            // TODO: Unregister from StateManager
        }
    }

    @Override
    public void close() throws Exception {
        client.close();
    }

    private Message readMessage() throws IOException, ClassNotFoundException {
        Object message = input.readObject();
        if (message instanceof Message) {
            return (Message) message;
        }
        return null;
    }

    private void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    }

    public void sendGameState(GameState gameState) {
        try {
            output.writeObject(gameState);
        } catch (IOException e) {
            System.err.printf("Exception caught: %s\n", e);
        }
    }

    public void sendChatMessage(String message) {
        try {
            sendMessage(new Message(MessageType.CHAT, message));
        } catch (IOException e) {
            System.err.printf("Exception caught: %s\n", e);
        }
    }
}
