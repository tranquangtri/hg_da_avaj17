/*
 * Copyright [2017] [TTT group]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server;

import game.server.entity.*;

import java.util.ArrayList;
import java.util.Arrays;


public class ClientSolve {
    private User user = null;
    private Cards cards = null;
    private Cards cardsExchange = null;
    private Cards cardsPlayed = null;
    private boolean isExchangeCard; // kiem tra xem server co yeu cau client gui bai de trao doi khong
    private int typeOfCard; // luu kieu cua la bai dau tien duoc danh ./.

    public ClientSolve() {
        this.user = new User();
        this.cards = new Cards();
        this.cardsPlayed = new Cards();
        this.isExchangeCard = false;
        this.typeOfCard = -1;
    }
    
    
    public User getUser() {
        return this.user;
    }
    
    public Cards getCards() {
        return this.cards;
    }
    
    public boolean getIsExchangeCard() {
        return this.isExchangeCard;
    }
    
    public Cards getCardExchange() {
        return this.cardsExchange;
    }
    
    public Cards getCardsPlayed() {
        return this.cardsPlayed;
    }
    
    public Card getCard(int index) {
        return this.cards.getCards().get(index);
    }
    
    public void getInforOfUser(String dataReceived) {
         String[] str = dataReceived.split("-")[0].split(" ");
         this.user.setUserName(str[0]);
         this.user.setwinMathces(Integer.parseInt(str[1]));
         this.user.setMatches(Integer.parseInt(str[2]));
    }
    
    
    public void setUser(User usr) {
        this.user = usr;
    }
    
    public void setCards(Cards cards) {
        this.cards = cards;
    }
    
    public void setIsExchangeCard(boolean yesNo) {
        this.isExchangeCard = yesNo;
    }
    
    public void setCardExchange(Cards cardsExchange) {
        this.cardsExchange = cardsExchange;
    }
    
    public void setTypeOfCard(int type) {
        this.typeOfCard = type;
    }
     
     
    public int getTypeOfCard() {
        return this.typeOfCard;
    }
    
    
    
    public String exchnageCard() {
        String data = "SttPlay3Cards-";
        
        for (int i = 0; i < 3; ++i) {
            data += this.cardsExchange.getCards().get(i).getValue() + " " +
                    this.cardsExchange.getCards().get(i).getType();
            if (i != 2)
                data += " ";
        }
        
        return data;
    }
    
    public void sortCard(int  index) {
        int N = this.cards.getCards().size() - 1;
        for (int i = index; i < N; ++i)
            this.cards.getCards().set(i, this.cards.getCards().get(i + 1));
        this.cards.delete(N);
    }
    
    public void updateOrderOfNewPlay(String dataReceived) {
        String[] data = dataReceived.split("-");
        String[] dat = data[5].split(" ");
        
        int N = dat.length == 9 ? dat.length - 1 : dat.length;
        System.out.println("Data5 " + data[5]);

        for (int i = 0; i < N; i += 2)
            if (Integer.parseInt(dat[i]) == this.user.getSttPlay()) {
                this.user.setSttPlay(Integer.parseInt(dat[i + 1]));
                break;
            }
    }
    
    public ArrayList<Integer> play(String dataReceived) {
        String[] data = dataReceived.split("-");
        String[] card = data[1].split(" ");
        
        ArrayList<Integer> result = new ArrayList<>();
        
        if (this.cardsPlayed.getCards().size() != 4) // Chi them bai khi con it nhat 1 nguoi chua danh
            this.cardsPlayed.add(Integer.parseInt(card[0]), Integer.parseInt(card[1]));
       
        if (cardsPlayed.getCards().size() == 1) // Neu la la bai dau tien thi luu lai kieu cua la bai
            this.typeOfCard = Integer.parseInt(card[1]);

        System.err.println("Debug user input data:");
        Arrays.stream(dataReceived.split(" ")).forEach((it)->System.err.print(it + ","));
        if (this.user.getSttPlay() == Integer.parseInt(dataReceived.split("-")[2].substring(0,1)))
            result.add(0);
        else
            result.add(-1);
        
        if (data.length == 6) { // cac buoc cap nhat man hinh sau khi 4 player da danh bai se lam ben GUI
            if (this.user.getSttPlay() == Integer.parseInt(dataReceived.split("-")[4].substring(0,1))){
                result.remove(0);
                result.add(0);
            }
            else result.add(-1);
            if (data[3].contains("winpoint")) { // tra ra ben ngoai index cua player an diem va diem
                String[] dat = data[4].split(" ");
                result.add(Integer.parseInt(dat[0]));
                result.add(Integer.parseInt(dat[1]));
                result.add(Integer.parseInt(dat[2]));
            }
            else 
                result.add(Integer.parseInt(data[4]));
        }
        return result;
    }
    
    public void reset() {
        this.cards = new Cards();
        this.cardsPlayed = new Cards();
        this.isExchangeCard = false;
        this.typeOfCard = -1;
    }
   
    
    public boolean receiveSTTPlayAndExchangeCardIfHaving(String dataReceived) {
        String[] data = dataReceived.split("-");
        
        try {
            String[] dat = data[3].split(" ");

            for (int i = 0; i < dat.length; ++i) 
                this.cards.add(Integer.parseInt(dat[i]), Integer.parseInt(dat[++i]));

            this.cards.sortCard(true);
            this.cardsExchange.deleteAll();
        }
        catch(Exception ex){}
        
        this.user.setSttPlay(Integer.parseInt(data[1]));
        return dataReceived.contains("-start");
    }
    
    public boolean receivedCardFromServer(String dataReceived) {
        this.cards = new Cards(dataReceived.split("-")[1]);
        this.cards.sortCard(false);
        if (dataReceived.contains("Card3")) {
            this.isExchangeCard = true;
            this.cardsExchange = new Cards("");
        }
        return !this.cards.getCards().isEmpty();
    }
    
    public String[] receivedUsersFromServer(String dataReceived) {
        return dataReceived.split("-")[2].split(" ");
    }
   
}
