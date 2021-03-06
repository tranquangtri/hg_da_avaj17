package game.server.entity;



public class Card {
    private int value;
    private int type;
    
    public Card(){}
    
    public Card(int value, int type) {
        this.value = value;
        this.type = type;
    }
    
    public Card(Card card) {
        this.value = card.getValue();
        this.type = card.getType();
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getValue() {
        return this.value;
    }
    
    //kiểm tra 2 thẻ giống nhau để khi phát sinh loại trừ trường hợp 2 thẻ bài trùng nhau
    boolean equalCard(Card c){
        if(this.type == c.type && this.value == c.value)
            return true;
        return false;
    }
    
    public boolean isSmaller(Card card) {
        if (this.type < card.getType())
            return true;
        if (this.type == card.getType()) 
            if (this.value < card.getValue())
                return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        Card tmp = (Card)obj;
        if (this.value == tmp.getValue() && this.type == tmp.getType())
            return true;
        return false;
    }
    
    
}
