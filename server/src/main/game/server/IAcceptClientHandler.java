package game.server;


import java.net.ServerSocket;

/**
 * Handler làm nhiệm vụ chấp nhận kết nối từ Client
 * @implNote LimitTimeAcceptClientHandler là một implement của interface này
 *           với nhiệm vụ accept các client trong một khoảng thời gian xác định
 */
public interface IAcceptClientHandler extends Runnable {
    void setClientManager(IClientManager clientManager);
    void setServerSocket(ServerSocket serverSocket);
}
