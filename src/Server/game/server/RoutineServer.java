package game.server;
import game.server.core.*;
public class RoutineServer implements Runnable{
    public final ClientManager clientManager;
    public RoutineServer(){
        clientManager = ClientManager.getInstance();
    }
    @Override
    public void run() {
        System.out.println("Number of client: " + Integer.toString(ClientManager.getInstance().getCount()));
        ClientManager clientManager = ClientManager.getInstance();
        clientManager.sendAll("Welcome to Heart games !");
        for (int i =0; i < clientManager.getCount(); i++){
            System.out.println("Client " + i + " " + clientManager.receive(i));
        }
    }
}
