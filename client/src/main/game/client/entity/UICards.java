
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
        this.uiCards.set(0, card);
    }
   
    
    

    public void addCards(JLabel card) {
        this.uiCards.add(card);
    }
    
    private String imagePath(int value, int type) {
        return  "/Images/" + value + type + ".png";
    }
    
    
    public void setCardsInScreen(int index, Card card) {
        uiCards.get(index).setIcon(new ImageIcon(getClass().getResource(imagePath(card.getValue(), card.getType()))));
    }
    
    public void setCardsInScreen(Cards cards) {
        for (int i = 0; i < cards.getCards().size(); ++i) {
            Icon icon = new ImageIcon(getClass().getResource(imagePath(cards.getCards().get(i).getValue(), cards.getCards().get(i).getType())));
            if (uiCards.get(i).getIcon() != icon)
                uiCards.get(i).setIcon(icon);
        }
    }
    
    public void setCardsInScreenNull() {
        for (int i = 0; i < uiCards.size(); ++i)
            uiCards.get(i).setIcon(null);
    }
    
    public void setEnableForCards(int isEnable, int typeOfCard, Cards cards) {
        if (isEnable == -1) {
            for (int i = 0; i < this.uiCards.size(); ++i)
                this.uiCards.get(i).setEnabled(false);
        }
        else {
            int index = cards.find2ClubIn1Cards(cards);
            if (index != -1) {
                for (int i = 0; i < cards.getCards().size(); ++i) {
                    if (i != index)
                        this.uiCards.get(i).setEnabled(false);
                }
            }
            else {
                int flag = 0;
                for (int i = 0; i < cards.getCards().size(); ++i) {
                    if (cards.getCards().get(i).getType() == typeOfCard) {
                        this.uiCards.get(i).setEnabled(true);
                        flag = 1;
                    }
                }
                if (flag == 0)
                    for (int i = 0; i < cards.getCards().size(); ++i)
                        this.uiCards.get(i).setEnabled(true);
            }
        }
    }
     
}
