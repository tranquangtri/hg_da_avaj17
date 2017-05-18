/**
 * Server entry
 */
package game.server;

import java.io.*;

public class Main{
     public static void main(String args[]) throws IOException, InterruptedException {
         Server server = new Server(1996);
         server.start();
     }
 }

