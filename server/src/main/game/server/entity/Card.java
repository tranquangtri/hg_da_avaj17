package game.server.entity;



public class Card {
    private int value;
    private int type;
    
    public Card(){}
    
    public Card(int value, int type) {
        this.value = value;
        this.type = type;
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
}