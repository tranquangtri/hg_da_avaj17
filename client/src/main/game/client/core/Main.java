/**
 * Client entry của
 */
package game.client.core;
import java.io.*;


class Command_Hello implements ICommand{
    final private String message;
    public Command_Hello(String message){
        this.message = message;
    }
    @Override
    public void excuteConsoleMode() {
        System.out.println("Server: " + message );
    }

    @Override
    public void excuteGuiMode() {

    }
}
 public class Main {
     public static void main(String args[]) throws IOException {
         Commands commands = new Commands(Commands.Mode.Console);
         /** Kết nối đến Server
          *  1. Nhận lời chào từ Server
          *  2. Gửi Hello Server đến Server
          *  */
         Server server = Server.connect("localhost", 1996);
         String messageWelcome = server.receive();

         /** Đối tượng commandManager căn cứ mode là console hay gui (giao diện) để gọi các phương thức khác nhau
          *  của đối tượng Command_Hello, đối tượng Command_Hello phải thực thi ICommand
          *  Các lệnh nào liên quan đến output (xuất ra màn hình) thì nên tạo một đối tượng ICommand như command_hello
          *  để sau này chuyển sang GUI dễ dàng hơn */
         commands.excute(new Command_Hello(messageWelcome));

         server.send("Hello server !");
         server.close();
     }
 }