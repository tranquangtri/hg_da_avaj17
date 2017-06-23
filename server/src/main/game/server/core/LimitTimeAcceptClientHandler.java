package game.server.core;
import game.server.IAcceptClientHandler;
import game.server.IClientManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

/* Excute accepting clients from server socket*/
class LimitTimeAcceptClientHandler implements IAcceptClientHandler {
    private IClientManager clientManager = null;
    private ServerSocket serverSocket = null;
    private final static int FOUR_CLIENTS = 4;
    private int timeout = 0;
    public void setTimeout(int timeout){
        this.timeout = timeout;
    }
    @Override
    public void setClientManager(IClientManager clientManager) {
        this.clientManager = clientManager;
    }
    @Override
    public void setServerSocket(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        System.out.println("LimitTimeAccepClientHandler run");
        Callable<Boolean> timeCount = ()-> {
            try {
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
            }
            return true;
        };

        /* Create cached thread pool */
        /* Start thread timeCount and acceptClients */
        ExecutorService services = Executors.newCachedThreadPool();
        Future<Boolean> futureTimeCount = services.submit(timeCount);
        services.execute(() -> {
            try {
                futureTimeCount.get();
                if (!serverSocket.isClosed()) ;
                serverSocket.close();
            } catch (InterruptedException | ExecutionException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            int index = 0;
            while (index < FOUR_CLIENTS) {
                Socket socketAccepted = serverSocket.accept();
                System.out.println("Player " + (index + 1) + " connected !");
                clientManager.add(new RemoteClient(socketAccepted));
                index++;
            }
            if (!serverSocket.isClosed())
                serverSocket.close();
        }
        catch (IOException e2) {
            if (e2 instanceof SocketException){
                // Thêm các npc client vào các slot trống
                if (clientManager.getCount() > 0) { // chi them npc khi co toi thieu 1 ket noi
                    int npcCount = 0;
                    while (clientManager.getCount() < 4){
                        npcCount++;
                        clientManager.add(new NPCClient("@NPC_" + Integer.toString(npcCount)));
                    }
                }
                return;
            }
            e2.printStackTrace();
        }
    }
}