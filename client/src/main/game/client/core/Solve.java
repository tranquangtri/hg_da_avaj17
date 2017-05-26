/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.core;

import game.client.entity.Cards;
import game.client.entity.User;
import java.util.Scanner;


public class Solve {
    private User user = null;
    private static final Scanner scanf = new Scanner(System.in);
    
    public Solve() {
        this.user = new User();
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User usr) {
        this.user = usr;
    }
    
    
    public void getInfoOfUserFromServer(String dataReceived) {
         String[] str = dataReceived.split("-")[0].split(" ");
         this.user.setUserName(str[0]);
         this.user.setwinMathces(Integer.parseInt(str[1]));
         this.user.setMatches(Integer.parseInt(str[2]));
    }
    
    
    private String choose3CardsToExchange(Cards cards) {
        String data = "";
        System.out.println("Choose 3 cards to exchange:");
        
        for (int i = 0; i < 3; ++i) {
            int index = scanf.nextInt();
            data += Integer.toString(cards.getCards().get(index).getValue()) + " " + 
                    Integer.toString(cards.getCards().get(index).getType());
            if (i != 2)
                data += " ";
        }
        
        return "3Cards-" + data;
    } // Chon 3 la bai de trao doi
    
    // Server se quyet dinh client co duoc chon 3 la bai de trao doi hay khong thong qua message duoc gui tu server den
    // Ham nay se phan tich xem server co yeu cau client chon bai de trao doi khong
    
    private String exchangeCardsOrStartGame(String dataReceived, Cards cards) {
        if (dataReceived.contains("Card3"))
            return "SttPlay" + choose3CardsToExchange(cards);
        return "SttPlay";
    } 
    
    
    
    
    public String solvingForClient(int step, String dataFromForm, String dataReceived, Cards cards) {
         switch (step) {
             case -2: { // Gui thong tin len server thong bao  username moi sau khi ten cu bi trung
                 return "Username again-" + dataFromForm;
             }
             case -1: { // Gui thong tin len server thong bao username de dang nhap la ten gi
                 return "Username-" + dataFromForm;
             }
             case 0: { // Gui yeu cau len server thong bao accept de bat dau choi game
                 return "Accept";
             }
             case 1: { // Gui yeu cau len server yeu cau chi bai
                 return "Devide card";
             }
             case 2: { // Gui thong tin len server de thong bao trao doi hoac khong trao doi
                 cards = new Cards(dataReceived.split("-")[1]);
                 return exchangeCardsOrStartGame(dataReceived, cards);
             }
             case 4: {
                 return "Done";
             }
         }
         return "DM";
     }
}
