package game.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Cards {
    private ArrayList<Card> cards = new ArrayList<>();
    
    public Cards() { // Ham tao bo bai 52 la ./.
        for (int i = 1; i < 14; ++i) 
            for (int j = 0; j < 4; ++j) {
                if (i == 1) 
                    cards.add(new Card(14, j));
                else
                    cards.add(new Card(i, j));
            }
    }
    
    public Cards(String data) { // Ham tao bai cho user (13 la hoac 3 la)
        if (data == null)
            return;
        
        String[] card = data.split(" ");
        for (int i = 0; i < card.length; ++i)
            this.cards.add(new Card(Integer.parseInt(card[i]), Integer.parseInt(card[++i])));
    }
    
    public static String packedCardSToSendClient(ArrayList<Card> cards) {
        String data = "";
        for (int i = 0; i < cards.size(); ++i) {
            data += Integer.toString(cards.get(i).getValue()) + " " + Integer.toString(cards.get(i).getType());
            if (i != cards.size() - 1)
                data += " ";
        }
        System.out.println(data);
        return "Cards-" + data;
    }
    
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public ArrayList<Card> shuffleCards(ArrayList<Card> cards) { // ham xao bai ./.
        Set<Card> newCards = new HashSet<>();
        Random rand = new Random();
        
        while (newCards.size() != cards.size()) 
            newCards.add(cards.get(rand.nextInt(cards.size())));
        
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
    
    public int find2ClubIn4Cards(ArrayList<ArrayList<Card>> card_13) {
        for (int i = 0; i < card_13.size(); ++i)
            for (int j = 0; j < card_13.get(i).size(); ++j)
                if (card_13.get(i).get(j).getValue() == 2 && card_13.get(i).get(j).getType() == 1)
                    return i;
        return -1;
    }
    
}
