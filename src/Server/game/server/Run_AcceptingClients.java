package game.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/* Excute accepting clients from server socket*/
class Run_AcceptingClients implements Runnable{
    private final ClientManager clientManager;
    private final ServerSocket server;
    private Run_AcceptingClients(ServerSocket server){
        this.clientManager = ClientManager.getInstance();
        this.server = server;
    }
    public static Run_AcceptingClients fromServerSocket(ServerSocket server){
        return new Run_AcceptingClients(server);
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