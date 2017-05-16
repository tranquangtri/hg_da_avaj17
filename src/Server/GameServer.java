/**
 * Server entry
 */
import java.io.*;
import java.net.*;
import java.util.*;
 public class GameServer{
     public static void main(String args[]) throws IOException {
         System.out.println("#1 Start server");
         ServerSocket server = null;
         ArrayList<Socket> clientSockets = new ArrayList<>();
         /* Open socket */
         System.out.println("#2 Open socket");
         server = new ServerSocket(1234);
         while (true) {
            /* Accept connection from client */
             Socket socketAccepted = server.accept();
             clientSockets.add(socketAccepted);
             new Thread(new ClientThread(socketAccepted)).start();

             /* Log */
             System.out.println("Accept " + Integer.toString(clientSockets.size()));
         }
     }
 }
 class ClientThread implements Runnable{

     private Socket clientsocket;
     public ClientThread(Socket clientsocket){
         this.clientsocket = clientsocket;
     }
     @Override
     public void run() {
         // Xử lý đối với client

     }
 }
