/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.client.core;

import game.client.entity.Card;
import game.client.entity.Cards;
import game.client.entity.User;



public class Solve {
    private User user = null;
    private Cards cards = null;
    private Cards cardsExchange = null;
    private boolean isExchangeCard; // kiem tra xem server co yeu cau client gui bai de trao doi khong
    
    public Solve() {
        this.user = new User();
        this.isExchangeCard = false;
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
    
    public Card getCard(int index) {
        return this.cards.getCards().get(index);
    }
    
    
    
    public void getInforOfUser(String dataReceived) {
         String[] str = dataReceived.split("-")[0].split(" ");
         this.user.setUserName(str[0]);
         this.user.setwinMathces(Integer.parseInt(str[1]));
         this.user.setMatches(Integer.parseInt(str[2]));
    }
    
    public boolean receivedCardFromServer(String dataReceived) {
        this.cards = new Cards(dataReceived.split("-")[1]);
        if (dataReceived.contains("Card3")) {
            this.isExchangeCard = true;
            this.cardsExchange = new Cards(null);
        }
        return !this.cards.getCards().isEmpty();
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
    
    public void receiveSTTPlayAndExchangeCardIfHaving(String dataReceived) {
        String[] data = dataReceived.split("-");
        if (data.length == 4) {
            String[] dat = data[3].split(" ");
            
            for (int i = 0; i < dat.length; ++i) 
                this.cards.add(Integer.parseInt(dat[i]), Integer.parseInt(dat[++i]));
        }
        this.user.setSttPlay(Integer.parseInt(data[1]));
    }
    
}
