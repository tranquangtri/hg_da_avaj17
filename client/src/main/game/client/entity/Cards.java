package game.client.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Cards {
    private ArrayList<Card> cards = new ArrayList<>();
    
    public Cards() {}
    
    public Cards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Cards(String data) { // Ham tao bai cho user (13 la hoac 3 la)
        if ("".equals(data))
            return;
        
        String[] card = data.split(" ");
        for (int i = 0; i < card.length; ++i)
            this.cards.add(new Card(Integer.parseInt(card[i]), Integer.parseInt(card[++i])));
    }
    
    
    
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
    public void setCard(int index, int value, int type) {
        this.cards.get(index).setValue(value);
        this.cards.get(index).setType(type);
    }
    
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public void add(Card card) {
        this.cards.add(card);
    }
    
    public void add(int value, int type) {
        this.cards.add(new Card(value, type));
    }
    
    public void delete(int index) {
        this.cards.remove(index);
    }
    
    public void deleteAll() {
        this.cards.removeAll(this.cards);
    }
    
    
    public ArrayList<Card> shuffleCards(ArrayList<Card> cards) { // ham xao bai ./.
        Set<Card> newCards = new HashSet<>();
        Random rand = new Random();
        
        while (newCards.size() != this.cards.size()) 
            newCards.add(cards.get(rand.nextInt(this.cards.size())));
        
        this.cards.removeAll(cards);
        this.cards = new ArrayList<>(newCards);
        return this.cards;
    }
    
    public ArrayList<ArrayList<Card>> divideCards(ArrayList<Card> card_52) {
        int j = 0;
        ArrayList<ArrayList<Card>> card_13 = new ArrayList<>();
        
        for (int i = 0; i < 4; ++i)
            card_13.add(new ArrayList<>());
        
        for (int i = 0; i < card_52.size(); ++i) {
            if (j == 4) j = 0;
            card_13.get(j++).add(card_52.get(i));
        }
        
        return card_13;
    }
    
    
    public int find2ClubIn1Cards(Cards card) {
        for (int i = 0; i < card.getCards().size(); ++i)
            if (card.getCards().get(i).getValue() == 2 && card.getCards().get(i).getType() == 1)
                return i;
        return -1;
    }
    
    public int findCard(Card card) {
        for (int i = 0; i < this.cards.size(); ++i)
            if (this.cards.get(i).equalCard(card))
                return i;
        return -1;
    }
    
    
    public void sortCard(boolean isAddCard) {
        if (isAddCard)
            shuffleCards(this.cards);
        
        for (int i = 0; i < this.cards.size() - 1; ++i) 
            for (int j = i + 1; j < this.cards.size(); ++j) 
                if (this.cards.get(i).isSmaller(this.cards.get(j))) {
                    Card tmp = new Card(this.cards.get(i));
                    this.cards.set(i, this.cards.get(j));
                    this.cards.set(j, tmp);
                }
    }
    
    
    private ArrayList<Card> findPlayingCards(Card firstCard) {
        ArrayList<Card> playingCards = new ArrayList<>();
        
        int flag = 0;
        for (int i = 0; i < this.cards.size(); ++i) { 
            if (this.cards.get(i).getType() == firstCard.getType()) {
                playingCards.add(this.cards.get(i));
                flag = 1;
            }
        }
        // Khong tim thay la bai nao co kieu trung voi la bai cua player 1 danh
        if (flag == 0)
            for (int i = 0; i < this.cards.size(); ++i) {
                if (firstCard.getValue() == 2 && firstCard.getType() == 1) {
                    if (this.cards.get(i).getType() == 3 || 
                       (this.cards.get(i).getValue()== 12 && this.cards.get(i).getType() == 0)) 
                        continue;
                }
                playingCards.add(this.cards.get(i));
            }
        
        return playingCards;
    }
    
    private ArrayList<Card> findPlayingCardsWhenStartGame(boolean isBreakingHeart) {
        ArrayList<Card> playingCards = new ArrayList<>();
        int index = find2ClubIn1Cards(this); // tim con 2 chuong xem co khong
        
        if (index != -1) { 
            playingCards.add(this.cards.get(index));
        }
        else {
            int endIndex = this.cards.size() - 1;
        
            if (endIndex != -1 && this.cards.get(endIndex).getType() == 3)
                isBreakingHeart = true;

            for (int i = 0; i < this.cards.size(); ++i) 
                if (isBreakingHeart != false || this.cards.get(i).getType() != 3)
                    playingCards.add(this.cards.get(i));
        }
        
        return playingCards;
    }
   
    public Card autoPlay(boolean isBreakingHeart, ArrayList<Card> others) {
        ArrayList<Card> playingCards = new ArrayList<>();
        Card resultCard = null;
        
        if (!others.isEmpty())
            playingCards = findPlayingCards(others.get(0));
        else 
            playingCards = findPlayingCardsWhenStartGame(isBreakingHeart);
        
        sortCard(false); // sap xep lai bai khi khong co them card moi
        resultCard = playingCards.get(0);
        int ind = this.findCard(resultCard);
        cards.remove(ind);
        return resultCard;
     }
  
    public ArrayList<Card> autoExchange(){
        ArrayList<Card> exchangeCard = new ArrayList<>();
        
        for (int i = 0; i < 3; ++i)
            exchangeCard.add(this.cards.get(i));
        
        for (int i = 0; i < 3; ++i)
            this.cards.remove(exchangeCard.get(i));
        
        return exchangeCard;
    }
}
