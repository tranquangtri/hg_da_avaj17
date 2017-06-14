package game.server;

import game.server.core.DataReceivedAnalysis;
import game.server.database.DataConnection;
import java.util.ArrayList;

import game.server.entity.Card;
import game.server.entity.Cards;
import game.server.entity.Result;

public class Solve {
    public enum OnlyReceiveServerFlag{
        ReceiveAccept,
        ReceiveExchange,
        Normal
    }
    private final UserManager userManager;
    private ExchangeCard exchangeCard;
    private ArrayList<ArrayList<Card>> card_13 = null; // Quan li 4 bo bai 13 la
    private ArrayList<Card> playedCard = null;
    private int exchangeCardTime; // Lan doi bai: co 4 lan doi bai
    private OnlyReceiveServerFlag flag; // Trong qua trinh lam co 2 giai doan server khong gui tin cho client ma doi nhan du thong tin tu client do la nhan accept va nhan bai trao doi.
    public static int countFeedback = 0; // Dem so lan nhan phan hoi tu client. Neu du thi tro ve 0
    private int breakingHeart = 0; // mac dinh tim chua vo

    public Solve() {
        flag = OnlyReceiveServerFlag.ReceiveAccept;
        exchangeCard = new ExchangeCard();
        userManager = new UserManager();
        playedCard = new ArrayList<>();
        this.exchangeCardTime = 0;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }
    

    public String login(String userName, int socket) {
        return this.userManager.login(userName, socket);
    }

    private String createCard_Process() {
        if (this.flag == OnlyReceiveServerFlag.ReceiveAccept) {
            Cards cards = new Cards(); // tao bo bai ./.
            cards.setCards(cards.shuffleCards(cards.getCards())); // xao bai
            this.card_13 = cards.divideCards(cards.getCards()); // chia bai thanh 4 bo moi bo 13 la
            int str = cards.find2ClubIn4Cards(this.card_13);
            this.userManager.sortSTTPlay(str); // Sap xep thu tu choi ban dau
            this.userManager.setStrIndex(str);
            this.flag = OnlyReceiveServerFlag.ReceiveExchange;
        }
        return "Waiting to devide card";
    }

    private String devideCard(ArrayList<Card> cards) {
        String data = "Card3";
        if (this.exchangeCardTime != 3)
            data += Cards.packedCardSToSendClient(cards);
        else
            data = Cards.packedCardSToSendClient(cards);
        
        data += "-";
        for (int i = 0; i < this.userManager.size(); ++i) {
            data += this.userManager.get(i).getUserName();
            if (i != this.userManager.size() - 1)
                data += " ";
        }
        
        return data;
    }

    public String sendOrderPlayn3CardIfHave_Process(int indexOfClient) {
        if (this.flag == OnlyReceiveServerFlag.ReceiveExchange) {
            this.exchangeCardTime = this.exchangeCard.exchange(this.exchangeCardTime);
            int str = this.exchangeCard.find2ClubIfHaving();
            this.userManager.sortSTTPlay(str);
            this.userManager.setStrIndex(str);
            this.flag = OnlyReceiveServerFlag.Normal;
        }
        String result = "";
        int sttPlay = this.userManager.get(indexOfClient).getSttPlay();
       
        if (!this.exchangeCard.isEmpty()) {
            result = "STTPlay-" + sttPlay + "-" + this.exchangeCard.get(indexOfClient);
        } else
            result = "STTPlay-" + sttPlay;

        if (sttPlay == 0)
            result += "-start";

        return result;
    }
    
    public String findNextPlay(int newStrIndex) {
        String result = "";
        int index = 0;
        
        try {
            for (int i = 0; i < this.userManager.size(); ++i) {
                if (i != 0)
                    result += " ";
                result += Integer.toString(newStrIndex++) + " " + Integer.toString(index++);
                if (newStrIndex == this.userManager.size())
                    newStrIndex = 0;
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    public int findIndexOfUserWinPoint(int strIndex) {
        for (int i = 0; i < this.userManager.size(); ++i)
            if (this.userManager.get(i).getSocket() == strIndex)
                return i;
        return -1;
    }
    
    public int findIndexOfUserWinGame() {
        int result = 0, flag = 0, minPoint = this.userManager.get(0).getPoint();
        
        for (int i = 0; i < this.userManager.size(); ++i) {
            if (this.userManager.get(i).getPoint() >= 10) 
                flag = 1;
            if (minPoint > this.userManager.get(i).getPoint()) {
                minPoint = this.userManager.get(i).getPoint();
                result = i;
            }
        }
        
        if (flag != 0) {
            DataConnection con = new DataConnection();
            for (int i = 0; i < this.userManager.size(); ++i) {
                if (i == result)
                    this.userManager.get(i).plusWinMatch();
                this.userManager.get(i).plusMatch();
                con.update(this.userManager.get(i).getUserName(), 
                           this.userManager.get(i).getMatches(), this.userManager.get(i).getWinMatches());
            }
            con.freeConnection();
        }
        else 
            result = -1;
        
        return result;
    }
    
    public void isBreakingHeart() {
        for (int i = 0; i < this.playedCard.size(); ++i)
            if (this.playedCard.get(0).getType() != 3 && this.playedCard.get(i).getType() == 3) {
                this.breakingHeart = 1; // tim vo
                break;
            }
    }
   
    public String WinPoints() {
        Card cardMax = this.playedCard.get(0);
        int index = 0, point = 0;
        
        // tim thu tu nguoi choi danh la bai lon nhat
        for (int i = 0; i < this.playedCard.size(); ++i) {
            if (this.playedCard.get(i).getValue() == 12 && this.playedCard.get(i).getType() == 0)
                point += 13;

            if (this.playedCard.get(i).getType() == 3) 
                point += 1;
            
            if (this.playedCard.get(i).getType() == this.playedCard.get(0).getType()) {
                if (cardMax.isSmaller(this.playedCard.get(i))) {
                    cardMax = this.playedCard.get(i);
                    index = i;
                }
            }
        }
        
       
        //xoa cac la bai da danh
        this.playedCard.removeAll(this.playedCard);
        
        for (int i = 0; i < this.userManager.size(); ++i) {
            if (this.userManager.get(i).getSttPlay() == index) {
                this.userManager.setStrIndex(i);
                break;
            }
        }
        
        System.out.println("STRT INDEX    " + this.userManager.getStrIndex());
        
         //Cap nhat lai sttPlay cho cac user
        int j = this.userManager.getStrIndex();
        for (int i = 0; i < this.userManager.size(); ++i) {
            if (j == this.userManager.size())
                j = 0;
            this.userManager.get(j++).setSttPlay(i);
        }
        
        if (point == 0)
            return "None win points-" + index + "-" + findNextPlay(index);
        
        int userWinPoint = findIndexOfUserWinPoint(this.userManager.getStrIndex());
        this.userManager.get(userWinPoint).plusPoint(point);
        
        return "winpoint-" + index + " " + userWinPoint + " " + point + "-" + findNextPlay(index);
    }
    
    public void reset() {
        try {
            flag = OnlyReceiveServerFlag.ReceiveAccept;
            exchangeCard = new ExchangeCard();
            playedCard = new ArrayList<>();
            DataReceivedAnalysis.state = 0; // quay ve buoc cho nhan accept, nhung do ben RoutineServer se + them 1 nen ta set o day = 0
            
            this.exchangeCardTime += 1;
            if (this.exchangeCardTime == 4) this.exchangeCardTime = 0;
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String play(String dataFromClient) {
        
        String[] data = dataFromClient.split("-")[1].split(" ");
        String result = dataFromClient;
        
        Card card = new Card(Integer.parseInt(data[0]), Integer.parseInt(data[1])); // Nhan bai duoc danh tu client va dua vao mang
        
        playedCard.add(card);
        isBreakingHeart(); // khi 1 la bai duoc them vao thi xac dinh xem tim da vo chua ./.
                
        if (countFeedback == 3)
            result += "-3" + " " + this.breakingHeart;
        else
            result += "-" + (countFeedback + 1) + " " + this.breakingHeart;
        
        if (playedCard.size() == 4) {
            result += "-" + WinPoints();
            if (dataFromClient.contains("-end")) {
                int userWinGame = this.findIndexOfUserWinGame();
                String[] tmp = result.split("-end");
                
                if (userWinGame != -1) 
                    result = tmp[0] + tmp[1] + " " + "wingame " + userWinGame + " end";
                else 
                    result = tmp[0] + tmp[1] + " end";
            }
        }
     
        return result;
    }
    
    public Result solvingForServer(int state, int indexOfClient, String dataFromClient) {
        String data = "";
        data = getData(state, indexOfClient, dataFromClient);
        if (!data.contains("Duplicate username"))
            countFeedback += 1;
        return new Result(data, indexOfClient);
    }

    private String getData(int state, int indexOfClient, String dataFromClient) {
        switch (state) {
            case 0:  // Chiu trach nhiem login
                return this.login(dataFromClient.split("-")[1], indexOfClient);
            case 1: // Chiu trach nhiem lay accept va tao bo bai
                return this.createCard_Process();
            case 2: // Chia bai cho client
                return this.devideCard(this.card_13.get(indexOfClient));
            case 3:  // Nhan bai trao doi neu co
                return exchangeCard.receive3Card(indexOfClient, dataFromClient);
            case 4:  // Gui thu tu choi va bai sau khi duoc doi cho client
                return sendOrderPlayn3CardIfHave_Process(indexOfClient);
            case 5: 
                return play(dataFromClient);
            default:
                throw new IllegalArgumentException("0,1,2,3,4");
        }
    }
}