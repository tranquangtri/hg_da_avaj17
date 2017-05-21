package game.server.core;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/* Excute accepting clients from server socket*/
class TaskAcceptClients implements Runnable{
    private final ClientManager clientManager;
    private final ServerSocket server;
    private TaskAcceptClients(ServerSocket server){
        this.clientManager = ClientManager.getInstance();
        this.server = server;
    }
    public static TaskAcceptClients fromServerSocket(ServerSocket server){
        return new TaskAcceptClients(server);
    }
    public void run(){
        try {
            int count = 0;
            while (count < 4) {
                Socket socketAccepted = server.accept();
                count++;
                System.out.println("Player " + count + " connected !");
                clientManager.add(new RemoteClient(socketAccepted));
            }
            if (!server.isClosed())
                server.close();
        }
        catch (IOException e2) {
            if (e2 instanceof SocketException){
                return;
            }
            e2.printStackTrace();
        }
    }
}