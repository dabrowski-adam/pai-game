package com.adamdabrowski.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        IConfig config = new ClassConfig();

        try (ServerSocket server = new ServerSocket(config.GetPort())) {
            System.out.printf("Listening on port %d.\n", config.GetPort());

            do {
                try {
                    Socket client = server.accept();
                    Session session = new Session(client);
                    Thread thread = new Thread(session);
                    thread.start();
                } catch (IOException e) {
                    System.err.printf("Exception caught: %s\nConnection failed!", e);
                }
            } while (!Thread.interrupted());
        } catch (IOException e) {
            System.err.printf("Exception caught: %s\nServer shutting down!", e);
        }
    }
}
