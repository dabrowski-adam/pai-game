package com.adamdabrowski.game;


import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Queue;

public class ClientOutgoing implements Runnable {
    private ObjectOutputStream outputStream;
    private Queue<Message> messages;

    ClientOutgoing(ObjectOutputStream outputStream, Queue<Message> messages) {
        this.outputStream = outputStream;
        this.messages = messages;
    }

    @Override
    public void run() {
            try {
                outputStream.writeObject(new Message(MessageType.HELLO, ""));

                while (!Thread.interrupted()) {
                        Message message = messages.poll();
                        if (message == null) { continue; }

                        outputStream.writeObject(message);

//                        switch (message.type) {
//                            case LOGIN:
//                                outputStream.writeObject(new Message(MessageType.LOGIN, message.payload));
//                                break;
//                            case CHAT:
//                                outputStream.writeObject(new Message(MessageType.CHAT, message.payload));
//                                break;
//                        }
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
