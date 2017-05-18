/**
 * Client entry của
 */
import java.io.*;
import java.net.Socket;
 public class ClientConsole{
     public static void main(String args[]) throws IOException {
         try{
             Socket socket = new Socket("localhost", 1996);
             BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
             System.out.println("Server: " + in.readLine());
             PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
             printWriter.println("Hello server !");
             printWriter.flush();
             in.close();
             printWriter.close();
             socket.close();
         }catch(IOException e) {
             throw new RuntimeException(e);
         }
     }
 }