package game.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class RemoteClient implements IClient{
    private final Socket socket;
    public RemoteClient(Socket socket){
        this.socket=  socket;
    }
    public void send(String message){
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(message);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
