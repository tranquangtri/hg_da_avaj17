package game.server.core;

import game.server.IClient;
import game.server.IClientManager;

import java.util.ArrayList;

/**
 * <p> Quản lý các {@link IClient}, tối đa 4
 * <p> Chứa các phương thức send, sendall, receive để gửi nhận dữ liệu đến client
 * <p> ở class RoutineServer lớp này được sử dụng qua interface {@link IClientManager}
 * <p> ở class Server, lớp này sẽ được khởi tạo, phương thức add dùng để thêm các {@link IClient}
 * từ mỗi thread, phương thức add là Thread-safe
 * @author      Trần Quang Trí
 */
final class ClientManager implements IClientManager{
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
    void add(IClient client){
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
    public String receive(int index){
        return clients.get(index).receive();
    }
}