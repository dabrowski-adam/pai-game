package com.adamdabrowski.game;


import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;

public class ClientOutgoing implements Runnable {
    private ObjectOutputStream outputStream;
    private Queue<String> messages;

    ClientOutgoing(ObjectOutputStream outputStream, Queue<String> messages) {
        this.outputStream = outputStream;
        this.messages = messages;
    }

    @Override
    public void run() {
            try {
                outputStream.writeObject(new Message(MessageType.HELLO, ""));

                while (!Thread.interrupted()) {
                    if (messages.size() > 0) {
                        String message = messages.remove();
                        outputStream.writeObject(new Message(MessageType.CHAT, message));
                    }
//                    Thread.sleep(1000);
//                    outputStream.writeObject(new Message(MessageType.CHAT, "HELLO FROM THE CLIENT SIDE"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
    }
}
