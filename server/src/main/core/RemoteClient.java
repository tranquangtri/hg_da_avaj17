package game.server.core;
import game.server.*;
import java.io.*;
import java.net.Socket;

/**
 * Đại diện cho một kết nối Client đến Server
 * <p> Thực chất là một lớp lưu giữ clientSocket và chứa các phương thức để
 * đơn giản quá trình gửi nhận dữ liệu dùng BufferedRead, PrintWriter
 */
class RemoteClient implements IClient {
    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader bufferedReader;
    public RemoteClient(Socket socket){
        this.socket =  socket;
        try {
            this.writer = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(String message){
            writer.println(message);
            writer.flush();
    }
    public String receive(){
        String ret = null;
        try {
            ret = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
