/**
 * Server entry
 */
package game.server;

import java.io.*;

public class Main{
     public static void main(String args[]) throws IOException, InterruptedException {
         IConfiguration configuration = new NormalConfiguration(1996);
         Server server = new Server(configuration);
         server.start();
     }
 }

