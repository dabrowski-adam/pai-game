package com.adamdabrowski.game;


import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientOutgoing implements Runnable {
    private ObjectOutputStream outputStream;

    ClientOutgoing(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
                outputStream.writeObject(new Message(MessageType.CHAT, "HELLO FROM THE CLIENT SIDE"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
