package com.adamdabrowski.game;

import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientIncoming implements Runnable {
    private ObjectInputStream inputStream;
    private Logic logic;

    ClientIncoming(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
        logic = Logic.getInstance();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Object message = inputStream.readObject();
                if (message instanceof Message) {
                    if (((Message) message).type == MessageType.CHAT) {
                        logic.DisplayMessage(((Message) message).payload);
                    }
//                    System.out.printf("Response: %s\n\n", ((Message) message).payload);
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
