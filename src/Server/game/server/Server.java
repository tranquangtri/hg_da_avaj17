package game.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.*;

class Server{
    private final int port;

    public Server(int port){
        this.port = port;
    }

    public void start(){
        /* Create and open socket on specific port */
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        }catch (IOException e){
            System.err.println("\nCannot open and create serversocket !\n");
            throw new RuntimeException(e);
        }

        /* Create time counting task for n seconds*/
        Callable<Boolean> timeCount = ()-> {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {}
                return true;
        };

        /* Create cached thread pool */
        ExecutorService services = Executors.newCachedThreadPool();

        /* Start thread timeCount and acceptClients */
        Future<Boolean> futureTimeCount = services.submit(timeCount);
        services.execute(Run_AcceptingClients.fromServerSocket(server));

         /* Wait until timeout and close server socket */
        try {
            futureTimeCount.get();
            if (!server.isClosed());
                 server.close();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Number of client: " + Integer.toString(ClientManager.getInstance().getCount()));
        ClientManager clientManager = ClientManager.getInstance();
        clientManager.sendAll("Welcome to Heart games !");
        for (int i =0; i < clientManager.getCount(); i++){
            System.out.println("Client " + i + " " + clientManager.receive(i));
        }
    }
}


