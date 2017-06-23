package game.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Cards {
    private ArrayList<Card> cards = new ArrayList<>();
    
    public Cards() { // Ham tao bo bai 52 la ./.
        
    }

    public Cards(ArrayList<Card> c){
        this.cards = c;
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
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public ArrayList<Card> create52Cards() {
        ArrayList<Card> cards52 = new ArrayList<>();
        for (int i = 1; i < 14; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (i == 1) 
                    cards52.add(new Card(14, j));
                else
                    cards52.add(new Card(i, j));
            }
        }
        return cards52;
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
    
    public int findCard(Card card) {
        for (int i = 0; i < this.cards.size(); ++i)
            if (this.cards.get(i).equalCard(card))
                return i;
        return -1;
    }
    
    
    
    public void delete(int index) {
        this.cards.remove(index);
    }

    public void deleteAll() {
        this.cards.removeAll(this.cards);
    }
    
    

    public void add(Card card) {
        int flag = 0;
        for (int i = 0; i < this.cards.size(); ++i)
            if (card == this.cards.get(i)) {
                flag = 1;
                break;
            }
        if (flag == 0)
            this.cards.add(card);
    }

    public void add(int value, int type) {
        this.cards.add(new Card(value, type));
    }
    
    public int find2ClubIn1Cards(Cards cards) {
        for (int i = 0; i < cards.getCards().size(); ++i)
            if (cards.getCards().get(i).getValue() == 2 && cards.getCards().get(i).getType() == 1)
                return i;
        return -1;
    }

    
    public ArrayList<Card> findPlayingCards(Card firstCard) {
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
    
    public ArrayList<Card> findPlayingCardsWhenStartGame(boolean isBreakingHeart) {
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
   
    public Card autoPlay(boolean isBreakingHeart, Cards others) {
        ArrayList<Card> playingCards = new ArrayList<>();
        Card resultCard = null;
        
        if (!others.getCards().isEmpty())
            playingCards = findPlayingCards(others.getCards().get(0));
        else 
            playingCards = findPlayingCardsWhenStartGame(isBreakingHeart);
        
        sortCard(false); // sap xep lai bai khi khong co them card moi
        resultCard = playingCards.get(0);
        int ind = this.findCard(resultCard);
        System.out.println("CARD REMOVED " + this.cards.get(ind).getValue() + "-" + this.cards.get(ind).getType());
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
