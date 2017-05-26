package game.client.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Class Server lưu giữ socket và chứa các phương thức đơn giản hóa quá trình gửi nhận dữ liệu
 */
final public class Server {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private Server(Socket socket){
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    static Server connect(String host, int port){
        try {
            Socket socket = new Socket(host, port);
            Server server = new Server(socket);
            return server;

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
            ret = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
