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
    private final ArrayList<IClient> clients;
    private final static Object mutex = new Object();
    public ClientManager(){
        clients = new ArrayList<>();
    }
    @Override
    public void add(IClient client){
        synchronized (clients) {
            clients.add(client);
        }
    }
    
    @Override
    public void remove(int index) {
        synchronized (clients) {
            clients.remove(index);
        }
    }
    
    @Override
    public void set(int index, IClient client) {
        synchronized (clients) {
            clients.set(index, client);
        }
    }
   
    @Override
    public void sendAll(String message){
        clients.forEach((client) -> {
            client.send(message);
        });
    }
    
    @Override
    public void send(int client, String message){
        clients.get(client).send(message);
    }
    
    @Override
    public int getCount(){
        return clients.size();
    }
    
    @Override
    public String receive(int index){
        return clients.get(index).receive();
    }
}