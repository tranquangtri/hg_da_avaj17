/**
 * Client entry của
 */
package game.client.core;

import java.io.*;
import game.client.entity.Cards;


//class Command_Hello implements ICommand{
//    final private String message;
//    public Command_Hello(String message){
//        this.message = message;
//    }
//    @Override
//    public void excuteConsoleMode() {
//        System.out.println("Server: " + message );
//    }
//
//    @Override
//    public void excuteGuiMode() {
//
//    }
//}
 public class Main {
     public static void main(String args[]) throws IOException {
         Commands commands = new Commands(Commands.Mode.Console); // Lua chon che de console (console hoac gui) ./.
         
         /** Kết nối đến Server
          *  1. Nhận lời chào từ Server
          *  2. Gửi Hello Server đến Server
          *  */
         
         // Client chiu trach nhiem co trao doi bai hay khong
         
         
         
         Server server = Server.connect("localhost", 1996);
         Solve solve = new Solve();
         Cards cards = null;
         
         while (true) {
             String dataFromServer = server.receive();
             
            /** Đối tượng commandManager căn cứ mode là console hay gui (giao diện) để gọi các phương thức khác nhau
             *  của đối tượng Command_Hello, đối tượng Command_Hello phải thực thi ICommand
             *  Các lệnh nào liên quan đến output (xuất ra màn hình) thì nên tạo một đối tượng ICommand như command_hello
             *  để sau này chuyển sang GUI dễ dàng hơn */

            // commands.excute(new Command_Hello(dataFromServer)); // in du lieu nhan duoc ra man hinh console ./.
             
             int state = DataReceivedAnalysis.resultAfterAnalysis(dataFromServer);
             //if (state != 3) // Neu state = 3 thi khong send data vi dang doi nhan du bai
                //server.send(solve.solvingForClient(state, dataFromServer, cards));
             
         }
     }
 }