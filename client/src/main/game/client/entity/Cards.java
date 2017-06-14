package game.client.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Cards {
    private ArrayList<Card> cards = new ArrayList<>();
    
    public Cards() {}

    public Cards(String data) { // Ham tao bai cho user (13 la hoac 3 la)
        if (data == null)
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
    
    
    public void sortCard(boolean isAddCard) {
        if (isAddCard)
            shuffleCards(this.cards);
        
        for (int i = 0; i < cards.size() - 1; ++i) 
            for (int j = i + 1; j < cards.size(); ++j) 
                if (cards.get(i).isSmaller(cards.get(j))) {
                    Card tmp = new Card(cards.get(i));
                    cards.set(i, cards.get(j));
                    cards.set(j, tmp);
                }
    }
}
