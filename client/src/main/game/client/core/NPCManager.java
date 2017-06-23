package game.client.core;

import game.client.entity.Card;
import game.client.entity.Cards;
import java.util.ArrayList;


public class NPCManager {
    private ArrayList<String> namesOfNPC;
    private ArrayList<Integer> sttPlay;
    private ArrayList<Cards> cardsOfNPC;
    private ArrayList<Card> cardPlayed;
    
    private int timeExchange;
    private boolean isBreakingHeart;
    
    
    public NPCManager() {
        this.namesOfNPC = new ArrayList<>();
        this.cardsOfNPC = new ArrayList<>();
        this.sttPlay = new ArrayList<>();
        this.cardPlayed = new ArrayList<>();
        
        this.timeExchange = 0; // tuong ung lan doi bai dau tien
        this.isBreakingHeart = false;
        
        for (int i = 0; i < 4; ++i)
            this.sttPlay.add(-1);
    }
    
    

    public ArrayList<String> getNamesOfNPC() {
        return namesOfNPC;
    }

    public ArrayList<Integer> getSttPlay() {
        return sttPlay;
    }

    public ArrayList<Cards> getCardsOfNPC() {
        return cardsOfNPC;
    }

    public ArrayList<Card> getCardPlayed() {
        return cardPlayed;
    }
    
    public int getTimeExchange() {
        return timeExchange;
    }

    public boolean getIsBreakingHeart() {
        return isBreakingHeart;
    }
    
    
   
    

    public void setNamesOfNPC(ArrayList<String> namesOfNPC) {
        this.namesOfNPC = namesOfNPC;
    }

    public void setSttPlay(ArrayList<Integer> sttPlayOfNPC) {
        this.sttPlay = sttPlayOfNPC;
    }

    public void setCardsOfNPC(ArrayList<Cards> cardsOfNPC) {
        this.cardsOfNPC = cardsOfNPC;
    }

    public void setCardPlayed(ArrayList<Card> cardPlayed) {
        this.cardPlayed = cardPlayed;
    }

    public void setTimeExchange(int timeExchange) {
        this.timeExchange = timeExchange;
    }
    
    public void setIsBreakingHeart(boolean isBreakingHeart) {
        this.isBreakingHeart = isBreakingHeart;
    }
    
    
    
    private Cards create52Cards() {
        Cards cards = new Cards();
        
        for (int i = 1; i < 14; ++i) 
            for (int j = 0; j < 4; ++j) {
                if (i == 1) 
                    cards.add(new Card(14, j));
                else
                    cards.add(new Card(i, j));
            }
        
        return cards;
    }
    
    public void addPlayedCard(Card card) {
        if (this.cardPlayed.size() <= 4) {
            this.cardPlayed.add(card);
            isBreakingHeart();
        }
    }
    
    public void deletePlayedCard() {
        this.cardPlayed.removeAll(this.cardPlayed);
    }
    
    
    
    private ArrayList<ArrayList<Card>> ExchangerClockWise(ArrayList<ArrayList<Card>> exchangeCard) {
        ArrayList<Card> tmp = exchangeCard.get(0);
        
        for (int i = 0; i < exchangeCard.size() - 1; ++i)
            exchangeCard.set(i, exchangeCard.get(i + 1));
        exchangeCard.set(exchangeCard.size() - 1, tmp);
        
        ArrayList<ArrayList<Card>> result = new ArrayList<>();
        
        for (int i = 0; i < 4; ++i) 
            result.add(exchangeCard.get(i));
        
        return result;
    }
    
    private ArrayList<ArrayList<Card>> ExchangerCounterClockWise(ArrayList<ArrayList<Card>> exchangeCard) {
        ArrayList<Card> tmp = exchangeCard.get(exchangeCard.size() - 1);
        
        for (int i = exchangeCard.size() - 1; i > 0; --i) 
            exchangeCard.set(i, exchangeCard.get(i - 1)); 
        exchangeCard.set(0, tmp);
        
        ArrayList<ArrayList<Card>> result = new ArrayList<>();
        for (int i = 0; i < 4; ++i)
            result.add(exchangeCard.get(i));
        
        return result;
    }
    
    private ArrayList<ArrayList<Card>> ExchangerFaceToFace(ArrayList<ArrayList<Card>> exchangeCard) {
        for (int i = 0; i < 2; ++i) {
            ArrayList<Card> tmp = exchangeCard.get(i);
            exchangeCard.set(i, exchangeCard.get(i + 2));
            exchangeCard.set(i + 2, tmp);
        }
        
        ArrayList<ArrayList<Card>> result = new ArrayList<>();
        for (int i = 0; i < 4; ++i)
            result.add(exchangeCard.get(i));
        
        return result;
    }
    
    
    
    private ArrayList<Card> npcExchangeCard(int index) {
        ArrayList<Card> exchangeCard = new ArrayList<>();
        
        for (int i = 0; i < 3; ++i) 
            exchangeCard.add(this.cardsOfNPC.get(index).getCards().get(i));
        
        for (int i = 0; i < 3; ++i)
            this.cardsOfNPC.get(index).getCards().remove(exchangeCard.get(i));
        
        return exchangeCard;
    }
    
    private void addExchangeCardForNPC(int index, ArrayList<Card> exchangeCard) {
        for (int i = 0; i < exchangeCard.size(); ++i)
            this.cardsOfNPC.get(index).add(exchangeCard.get(i));
        this.cardsOfNPC.get(index).sortCard(true);
    }
    
    
    private int find2ClubOnNPCCards() {
        for (int i = 0; i < this.cardsOfNPC.size(); ++i)
            for (int j = 0; j < this.cardsOfNPC.get(i).getCards().size(); ++j)
                if (this.cardsOfNPC.get(i).getCards().get(j).getValue() == 2 && this.cardsOfNPC.get(i).getCards().get(j).getType() == 1)
                    return i;
        return -1;
    }
   
    public int findSTTPlayForPlayer() {
        int strtIndex = find2ClubOnNPCCards();
        if (strtIndex == -1) strtIndex = 3; // mac dinh nguoi choi se nam o vi tri cuoi cung, neu khong tim duoc 2 chuong o phia npc thi 2 chuong se nam o phia player
      
        
        for (int i = 0; i != 4;) {
            if (strtIndex == 4)
                strtIndex = 0;
            try {
                this.sttPlay.set(strtIndex++, i++);
            }
            catch (Exception ex) {break;}
        }
       
        return this.sttPlay.get(3);
    }
    
    public ArrayList<Card> devideCardsForPlayer() {
        Cards cards = create52Cards(); // tao bo bai 52 la
        
        ArrayList<Card> cardsShuffe = cards.shuffleCards(cards.getCards()); // xao bai
        ArrayList<ArrayList<Card>> card13 = cards.divideCards(cardsShuffe); // chia thanh 4 bo 13 la bai
     
        for (int i = 0; i < 3; ++i) {
            this.cardsOfNPC.add(new Cards(card13.get(i)));
            this.cardsOfNPC.get(i).sortCard(false);
        }
        
        return card13.get(3);
    }
    
    public ArrayList<Card> exchangeCardForPlayer(ArrayList<Card> cards) {
        ArrayList<ArrayList<Card>> exchangeCards = new ArrayList<>();
        
        for (int i = 0 ; i < this.cardsOfNPC.size(); ++i)
            exchangeCards.add(npcExchangeCard(i));
        exchangeCards.add(cards);
        
        switch(this.timeExchange) {
            case 0: {
                exchangeCards = ExchangerClockWise(exchangeCards);
                break;
            }
            case 1: {
                exchangeCards = ExchangerCounterClockWise(exchangeCards);
                break;
            }
            case 3: {
                exchangeCards = ExchangerFaceToFace(exchangeCards);
                break;
            }
        }
        for (int i = 0; i < 3; ++i) 
            addExchangeCardForNPC(i, exchangeCards.get(i));
       
        return exchangeCards.get(3);
    }
    
    
    private int findIndexPlayerStart(int start) {
        for (int i = 0; i < this.sttPlay.size(); ++i)
            if (this.sttPlay.get(i) == start)
                return i;
        return -1;
    }
    
    public int[] findIndexPlayMaxCard() {
        Card cardMax = this.cardPlayed.get(0);
        int index = 0, point = 0;
        
        
        for (int i = 0; i < this.cardPlayed.size(); ++i) {
            if (this.cardPlayed.get(i).getValue() == 12 && this.cardPlayed.get(i).getType() == 0)
                point += 13;

            if (this.cardPlayed.get(i).getType() == 3) 
                point += 1;
            
            if (this.cardPlayed.get(i).getType() == this.cardPlayed.get(0).getType()) {
                if (cardMax.isSmaller(this.cardPlayed.get(i))) {
                    cardMax = this.cardPlayed.get(i);
                    index = i;
                }
            }
        }
        
        for (int i = 0; i < this.cardsOfNPC.size(); ++i) {
            System.out.println("INDEX " + i);
            for (int j = 0; j < this.cardsOfNPC.get(i).getCards().size(); ++j)
                System.out.println(" CARD: " + this.cardsOfNPC.get(i).getCards().get(j).getValue() + " " + this.cardsOfNPC.get(i).getCards().get(j).getType());
            System.out.println();
        }
        
        for (int i = 0; i < this.sttPlay.size(); ++i)
            if (index == this.sttPlay.get(i)) {
                index = i;
                break;
            }
        
        int[] result = {index, point};
        return result;
    }
    
    public int updateSTTPlay(int newStrIndex) {
        int index = 0;
        
        try {
            for (int i = 0; i < this.sttPlay.size(); ++i) {
                this.sttPlay.set(newStrIndex++, index++);
                if (newStrIndex == this.sttPlay.size())
                    newStrIndex = 0;
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return this.sttPlay.get(3);
    }
    
    public void reset() {
        this.cardsOfNPC = new ArrayList<>();
        this.sttPlay = new ArrayList<>();
        this.cardPlayed = new ArrayList<>();
        
        this.isBreakingHeart = false;
        this.timeExchange += 1; // tuong ung lan doi bai dau tien
        if (this.timeExchange == 4) this.timeExchange = 0;
        
        for (int i = 0; i < 4; ++i)
            this.sttPlay.add(-1);
    }
    
    private void isBreakingHeart() {
        for (int i = 0; i < this.cardPlayed.size(); ++i)
            if (this.cardPlayed.get(0).getType() != 3 && this.cardPlayed.get(i).getType() == 3) {
                this.isBreakingHeart = true; // tim vo
                break;
            }
    }
    
    public ArrayList<Card> autoPlay() {
        int start = this.cardPlayed.size();
        int nextPlay = findIndexPlayerStart(start++);
        
        while (nextPlay != -1 && nextPlay != 3) {
            addPlayedCard(this.cardsOfNPC.get(nextPlay).autoPlay(this.isBreakingHeart, this.cardPlayed));
            nextPlay = findIndexPlayerStart(start++);
        }
        
        return this.cardPlayed;
    }
}
