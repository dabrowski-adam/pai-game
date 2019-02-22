package com.adamdabrowski.game;

import com.adamdabrowski.server.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

public class ClientIncoming implements Runnable {
    private ObjectInputStream inputStream;

    ClientIncoming(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Object message = inputStream.readObject();
                if (message instanceof Message) {
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
