package game.server.core;
import game.server.IAcceptClientHandler;
import game.server.IClientHandler;
import game.server.IClientManager;
import game.server.RoutineServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.concurrent.*;

/**
 * Tạo serverSocket, lắng nghe các kết nối của client và thêm vào {@link ClientManager}
 * sau đó gọi RoutineServer để xử lý theo kịch bản gửi nhận dữ liệu
 */
class Server{
    private final int port;
    private IClientHandler clientHandler = null;
    private IAcceptClientHandler acceptClientHandler = null;
    private IClientManager clientManager = null;

    public Server(int port){
        this.port = port;
    }
    public void setClientManager(IClientManager clientManager){
        this.clientManager = clientManager;
    }
    public void setClientHandler(IClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }
    public void setAcceptClientHandler(IAcceptClientHandler acceptClientHandler){
        this.acceptClientHandler = acceptClientHandler;
    }


    public void start() {
        /** AcceptClientHandler
         *      - Sẽ làm nhiệm vụ chấp nhận các kết nối từ serverSocket
         *      - Các kết nối được chấp nhận sẽ được thêm vào và quản lý
         *      bởi ClientManager
         *  Sau cùng ClientHandler được chạy để cài đặt quá trình gửi nhận
         *  gói tin theo kịch bản.
         *
         *  AcceptClientHandler, ClientHandler, ClientManager được inject
         *  từ applicationContext.xml
         *  */
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        acceptClientHandler.setServerSocket(serverSocket);
        acceptClientHandler.setClientManager(clientManager);
        acceptClientHandler.run();

        clientHandler.setClientManager(clientManager);
        clientHandler.run();
    }
}


