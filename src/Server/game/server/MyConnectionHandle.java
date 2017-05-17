package game.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class MyConnectionHandle implements IConnectionHandle {

    private Socket clientsocket;
    public MyConnectionHandle(Socket clientsocket) {
        this.clientsocket = clientsocket;
    }
    /* Xử lý các request từ client đến server */
    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(clientsocket.getOutputStream());
            writer.println("Welcome to webserver of Heartgame");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
