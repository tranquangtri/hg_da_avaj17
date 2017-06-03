package game.server.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
/**
 * Bootstrap
 * @author      Trần Quang Trí
 */
public class Main{
     public static void main(String args[]) throws IOException, InterruptedException {
         ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");
         Server server = (Server)applicationContext.getBean("server");
         server.start();
     }
 }

