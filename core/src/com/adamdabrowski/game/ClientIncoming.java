package com.adamdabrowski.game;

import com.adamdabrowski.server.GameState;
import com.adamdabrowski.server.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientIncoming implements Runnable {
    private ObjectInputStream inputStream;
    private Logic logic;

    ClientIncoming(ObjectInputStream inputStream, Logic logic) {
        this.inputStream = inputStream;
        this.logic = logic;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Object message = inputStream.readObject();
                if (message instanceof Message) {
                    switch (((Message) message).type) {
                        case CHAT:
                            logic.DisplayMessage(((Message) message).payload);
                            break;
                        case LOGGED_IN:
                            logic.isLoggedIn = true;
                            break;
                        case LOGGED_OUT:
                            logic.isLoggedIn = false;
                            break;
                        case LOBBY_JOIN:
                            logic.isInLobby = true;
                            break;
                        case LOBBY_LEAVE:
                            logic.isInLobby = false;
                            break;
                    }
                } else if (message instanceof GameState) {
                    logic.UpdateGameState((GameState) message);
                } else {
//                    System.out.printf("Response: %s\n\n", message.toString());
                }
            } catch (SocketException e) {
                return; // Socket closed
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
