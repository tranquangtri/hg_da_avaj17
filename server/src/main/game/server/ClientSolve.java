


public class ClientSolve {
    private User user = null;
    private Cards cards = null;
    private Cards cardsExchange = null;
    private Cards cardsPlayed = null;
    private boolean isExchangeCard; // kiem tra xem server co yeu cau client gui bai de trao doi khong
    private boolean breakingHeart;

    public ClientSolve() {
        this.user = new User();
        this.cards = new Cards("");
        this.cardsPlayed = new Cards("");
        this.isExchangeCard = false;
        this.breakingHeart = false;
    }
    
    
    public User getUser() {
        return this.user;
    }
    
    public Cards getCards() {
        return this.cards;
    }
    
    public boolean getIsExchangeCard() {
        return this.isExchangeCard;
    }
    
  
    
    public void getInforOfUser(String dataReceived) {
         String[] str = dataReceived.split("-")[0].split(" ");
         this.user.setUserName(str[0]);
         this.user.setwinMathces(Integer.parseInt(str[1]));
         this.user.setMatches(Integer.parseInt(str[2]));
    }
    
    public boolean getBreakingHeart() {
        return this.breakingHeart;
    }
    
    
    public void setUser(User usr) {
        this.user = usr;
    }
    
    public void setCards(Cards cards) {
        this.cards = cards;
    }
    
    public void setIsExchangeCard(boolean yesNo) {
        this.isExchangeCard = yesNo;
    }
    
    public void setCardExchange(Cards cardsExchange) {
        this.cardsExchange = cardsExchange;
    }
    
    
    
    public void sortCard(int  index) {
        int N = this.cards.getCards().size();
        for (int i = index; i < N; ++i)
            this.cards.getCards().set(i, this.cards.getCards().get(i + 1));
        this.cards.delete(N);
    }
    
    public void updateOrderOfNewPlay(String dataReceived) {
        String[] data = dataReceived.split("-");
        String[] dat = data[5].split(" ");
        
        int N = dat.length == 9 ? dat.length - 1 : dat.length;
        System.out.println("Data5 " + data[5]);

        for (int i = 0; i < N; i += 2)
            if (Integer.parseInt(dat[i]) == this.user.getSttPlay()) {
                this.user.setSttPlay(Integer.parseInt(dat[i + 1]));
                break;
            }
    }
    
    public ArrayList<Integer> play(String dataReceived) {
        String[] data = dataReceived.split("-");
        String[] card = data[1].split(" ");
        ArrayList<Integer> result = new ArrayList<>();
        
        if (this.cardsPlayed.getCards().size() != 4) // Chi them bai khi con it nhat 1 nguoi chua danh
            this.cardsPlayed.add(Integer.parseInt(card[0]), Integer.parseInt(card[1]));

        System.err.println("Debug user input data:");
        Arrays.stream(dataReceived.split(" ")).forEach((it)->System.err.print(it + ","));
        String[] sttPlayAndBreakingHeart = data[2].split(" "); // Lay luot choi va bien xac dinh tim vo chua
        this.breakingHeart = Integer.parseInt(sttPlayAndBreakingHeart[1]) == 1; // set tim vo da vo hay chua
       
        if (this.user.getSttPlay() == Integer.parseInt(sttPlayAndBreakingHeart[0])) 
            result.add(this.user.getSttPlay());
        else
            result.add(-1); // khong duoc quyen danh
        
        if (data.length == 6) { // cac buoc cap nhat man hinh sau khi 4 player da danh bai se lam ben GUI
            String[] dat = data[4].split(" "); // tra ra ben ngoai index cua player an diem va diem
            if (data[3].contains("winpoint")) {
                result.set(0, 4); // xac dinh co an diem
                result.add(Integer.parseInt(dat[1])); // vi tri cua user an diem
                result.add(Integer.parseInt(dat[2])); // diem ma user co duoc
                
                if (data[5].contains("wingame"))
                    result.add(Integer.parseInt(data[5].split("wingame")[1].split(" ")[1]));
            }
            else 
                result.set(0, 4);
        }
        return result;
    }
    
    public void reset() {
        this.cards = new Cards("");
        this.cardsPlayed = new Cards("");
        this.isExchangeCard = false;
        this.breakingHeart = false; 
    }
   
    
    public boolean receiveSTTPlayAndExchangeCardIfHaving(String dataReceived) {
        String[] data = dataReceived.split("-");
        
        try {
            String[] dat = data[3].split(" ");

            for (int i = 0; i < dat.length; ++i) 
                this.cards.add(Integer.parseInt(dat[i]), Integer.parseInt(dat[++i]));

            this.cards.sortCard(true);
            this.cardsExchange.deleteAll();
        }
        catch(Exception ex){}
        
        this.user.setSttPlay(Integer.parseInt(data[1]));
        return dataReceived.contains("-start");
    }
    
    public boolean receivedCardFromServer(String dataReceived) {
        this.cards = new Cards(dataReceived.split("-")[1]);
        this.cards.sortCard(false);
        if (dataReceived.contains("Card3")) {
            this.isExchangeCard = true;
            this.cardsExchange = new Cards("");
        }
        return !this.cards.getCards().isEmpty();
    }
    
    public String[] receivedUsersFromServer(String dataReceived) {
        return dataReceived.split("-")[2].split(" ");
    }
   
}
