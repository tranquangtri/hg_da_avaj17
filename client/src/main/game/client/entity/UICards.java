
package game.client.entity;

import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class UICards {
    private ArrayList<JLabel> uiCards = null;
   
    
    public UICards() {
        this.uiCards = new ArrayList<>();
    }
    
    

    public ArrayList<JLabel> getUiCards() {
        return uiCards;
    }
    
    public JLabel getUICard(int index) {
        return this.uiCards.get(index);
    }
    
    public int getSize() {
        return this.uiCards.size();
    }
    
    public void setUiCards(ArrayList<JLabel> uiCards) {
        this.uiCards = uiCards;
    }
    
    public void setUICard(int index, JLabel card) {
        this.uiCards.set(index, card);
    }
   
    
    

    public void addCards(JLabel card) {
        this.uiCards.add(card);
    }
    
    private String imagePath(int value, int type) {
        return  "/Images/" + value + type + ".png";
    }
    
    
    public void setCardsInScreen(int index, Card card) {
        this.uiCards.get(index).setIcon(new ImageIcon(getClass().getResource(imagePath(card.getValue(), card.getType()))));
    }
    
    public void setCardsOnScreen(Cards cards) {
        for (int i = 0; i < cards.getCards().size(); ++i) {
            Icon icon = new ImageIcon(getClass().getResource(imagePath(cards.getCards().get(i).getValue(), cards.getCards().get(i).getType())));
            if (this.uiCards.get(i).getIcon() != icon)
                this.uiCards.get(i).setIcon(icon);
        }
    }
    
    public void setCardsInScreenNull() {
        for (int i = 0; i < this.uiCards.size(); ++i)
            this.uiCards.get(i).setIcon(null);
    }
    
    public void setEnablePlayingCards(Card firstCard, Cards cards) {
        int index = cards.find2ClubIn1Cards(cards);
      
        if (index != -1) { // th tim thay 2 chuong trong bo bai
            for (int i = 0; i < cards.getCards().size(); ++i) {
                if (i != index)  this.uiCards.get(i).setEnabled(false);
                else
                    this.uiCards.get(i).setEnabled(true);
            }
        }
        else { // th khong tim thay 2 chuong trong bo bai
            // tim trong bo bai hien tai cua player co co bai nao trung voi bai cua player 1 danh khong, co thi enable no,
            // khong thi unenable
            int flag = 0;
            for (int i = 0; i < cards.getCards().size(); ++i) { 
                if (cards.getCards().get(i).getType() == firstCard.getType()) {
                    this.uiCards.get(i).setEnabled(true);
                    flag = 1;
                }
            }
            // Khong tim thay la bai nao co kieu trung voi la bai cua player 1 danh
            if (flag == 0)
                for (int i = 0; i < cards.getCards().size(); ++i) {
                    if (firstCard.getValue() == 2 && firstCard.getType() == 1) {
                        if (cards.getCards().get(i).getType() == 3 || (cards.getCards().get(i).getValue()== 12 && cards.getCards().get(i).getType() == 0)) {
                            this.uiCards.get(i).setEnabled(false);
                            continue;
                        }
                    }
                    this.uiCards.get(i).setEnabled(true);
                }
        }
    }
    
    public void setEnablePlayingCardsForUserWin(boolean isBreakingHeart, Cards cards) {
        int endIndex = cards.getCards().size() - 1;
        
        if (endIndex != -1 && cards.getCards().get(endIndex).getType() == 3)
            isBreakingHeart = true;
        
        for (int i = 0; i < cards.getCards().size(); ++i) {
            if (isBreakingHeart == false && cards.getCards().get(i).getType() == 3)
                this.uiCards.get(i).setEnabled(false);
            else this.uiCards.get(i).setEnabled(true);
        }
    }
    
    public void setEnableForAllCards(boolean isEnable) {
        for (int i = 0; i < this.uiCards.size(); ++i)
            this.uiCards.get(i).setEnabled(isEnable);
    }
     
    public void setCardsBeforePlay() {
        for (int i = 0; i < this.uiCards.size(); ++i)
            this.uiCards.get(i).setIcon(new ImageIcon(getClass().getResource(imagePath(0, 0))));
    }
}
