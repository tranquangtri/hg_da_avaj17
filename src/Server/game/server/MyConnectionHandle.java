package game.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class MyConnectionHandle implements IConnectionHandle {

    private Socket clientsocket;
    public MyConnectionHandle(Socket clientsocket) {
        this.clientsocket = clientsocket;
    }
    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(clientsocket.getOutputStream());
            writer.println("Welcome to Webserver of Heartgame");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
