package game.server;
public class RoutineServer implements Runnable{
    public final IClientManager clientManager;
    public RoutineServer(IClientManager clientManager){
        this.clientManager = clientManager;
    }

    @Override
    public void run() {
        /**
         *  Xử lý server  ở đây
         *  Example:
         *  1. Gửi "Welcome to Heart games !" đến các client
         *  2. Client gửi trả lại Welcome Server
         *
         */

        System.out.println("Number of client: " + Integer.toString(clientManager.getCount()));
        clientManager.sendAll("Welcome to Heart games !");
        for (int i =0; i < clientManager.getCount(); i++){
        System.out.println("Client " + i + " " + clientManager.receive(i));
        }


    }
}
