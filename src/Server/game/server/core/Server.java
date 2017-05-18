package game.server.core;
import game.server.RoutineServer;

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
        try {
            Object o = Class.forName("game.server.RoutineServer").newInstance();
            if (o instanceof Runnable){
                Runnable routine = (Runnable)o;
                routine.run();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}


