/**
 * Server entry
 */
package game.server.core;

import java.io.*;
/**
 * game.server.core.Server là ngõ vào của server
 * <p> Nhiệm vụ là khởi động Server và load các thông số cần thiết
 * @author      Trần Quang Trí
 */
public class Main{
     public static void main(String args[]) throws IOException, InterruptedException {
         Server server = new Server(1996);
         server.start();
     }
 }

