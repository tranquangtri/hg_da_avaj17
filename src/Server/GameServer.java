/**
 * Server entry
 */
import java.util.*;
 public class GameServer{
     public static void main(String args[]){
        System.out.println("Hello, this is server");
     }
 }
 interface Member{
 	public int getId();
 	public String getName();
 }
 interface Group{
 	public int getId();
 	public List getMembers();
 }
