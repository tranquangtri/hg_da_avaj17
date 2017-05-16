package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server{
    IConfiguration configuration;
    public Server(IConfiguration configuration){
        this.configuration = configuration;
    }
    public void start(){
        ServerSocket server = null;
        try {
            server = new ServerSocket(configuration.getPort());
            while (true) {
                Socket socketAccepted = server.accept();
                IConnectionHandle connectionHandle = (IConnectionHandle)Class
                        .forName("game.server.MyConnectionHandle")
                        .getConstructor(Socket.class)
                        .newInstance(socketAccepted);
                new Thread(connectionHandle).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
