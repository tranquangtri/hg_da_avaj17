package game.server;

import java.util.ArrayList;

/** ClientManager
 *  Là class singleton dùng quản lý các client
 *  Thread-safe
 * */
final class ClientManager{
    private static ClientManager clientManager = null;
    private final ArrayList<IClient> clients;
    private final static Object mutex = new Object();
    private ClientManager(){
        clients = new ArrayList<>();
    }
    public static ClientManager getInstance(){
        if (clientManager == null) {
            synchronized (mutex) {
                clientManager = new ClientManager();
            }
        }
        return clientManager;
    }
    public void add(IClient client){
        synchronized (clients) {
            clients.add(client);
        }
    }
    public void sendAll(String message){
        for (IClient client : clients){
            client.send(message);
        }
    }
    public void send(int client, String message){
        clients.get(client).send(message);
    }
    public int getCount(){
        return clients.size();
    }
}