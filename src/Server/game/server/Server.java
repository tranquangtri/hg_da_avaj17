package game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

class Server{
    IConfiguration configuration;
    public Server(IConfiguration configuration){
        this.configuration = configuration;
    }
    public void start() throws IOException{
        final ArrayList<Socket> sockets = new ArrayList<>();
        final ServerSocket server = new ServerSocket(configuration.getPort());
        int count = 0;
        Thread threadTimeOut = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {}
            }
        });
        Thread threadAccept = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int count = 0;
                    while (true) {
                        Thread.sleep(1);
                        Socket socketAccepted = server.accept();
                        System.out.println(++count);
                        sockets.add(socketAccepted);
                    }
                }
                catch (Exception e2) {
                    return;
                }
            }
        });
        threadAccept.start();
        threadTimeOut.start();
        while (threadTimeOut.isAlive() && threadAccept.isAlive()){
        }
        if (threadAccept.isAlive()) {
            threadAccept.interrupt();
        }

        if (threadTimeOut.isAlive()){
            threadTimeOut.interrupt();
        }

        if (!threadAccept.isAlive()) {
            System.out.println("Thread accept dead");
        }

        if (!threadTimeOut.isAlive()){
            System.out.println("Thread timeout dead");
        }

    }
}
