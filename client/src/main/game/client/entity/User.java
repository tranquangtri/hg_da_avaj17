
package game.client.entity;

public class User {
    private String userName;
    private int matches;
    private int winMatches;
    private int sttPlay;
    
    public User() {
        this.userName = "";
        this.matches = 0;
        this.winMatches = 0;
        this.sttPlay = -1;
    }
    
    public User(String userName, int matchces, int winMatches, int state) {
        this.userName = userName;
        this.matches = matchces;
        this.winMatches = winMatches;
        this.sttPlay = -1;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public void setMatches(int matches) {
        this.matches = matches;
    }
    
    public void setwinMathces(int winMatches) {
        this.winMatches = winMatches;
    }
    
    public void setSttPlay(int stt) {
        this.sttPlay = stt;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    
    public int getMatches() {
        return this.matches;
    }
    
    public int getWinMatches() {
        return this.winMatches;
    }
    
    public int getSttPlay() {
        return this.sttPlay;
    }
    
    public Boolean isEmptyUser() {
        return this.userName.equals("") && (this.matches == 0 && this.winMatches == 0);
    }
}
