package game.server;

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
    private final ExchangeCard exchangeCard;
    private ArrayList<ArrayList<Card>> card_13 = null; // Quan li 4 bo bai 13 la
    private ArrayList<Card> playedCard = null;
    private int exchangeCardTime; // Lan doi bai: co 4 lan doi bai
    private OnlyReceiveServerFlag flag; // Trong qua trinh lam co 2 giai doan server khong gui tin cho client ma doi nhan du thong tin tu client do la nhan accept va nhan bai trao doi.
    public static int countFeedback = 0; // Dem so lan nhan phan hoi tu client. Neu du thi tro ve 0

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
    

    public String login(String userName) {
        return this.userManager.login(userName);
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
                result += Integer.toString(newStrIndex) + " " + Integer.toString(index);
                this.userManager.get(newStrIndex++).setSttPlay(index++);

                if (newStrIndex == this.userManager.size())
                    newStrIndex = 0;
            }
            
            for (int i = 0; i < this.userManager.size(); ++i) {
               System.out.println("STTPLAY " + i + "-----" + this.userManager.get(i).getSttPlay());
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    public String isWinPoints() {
        Card cardMax = this.playedCard.get(0);
        int index = 0, point = 0, Flag = 0;
        
        for (int i = 0; i < this.playedCard.size(); ++i) {
            if (Flag == 1) 
                if (this.playedCard.get(i).getValue() == 12 && this.playedCard.get(i).getType() == 0)
                    point += 14;
            
            if (this.playedCard.get(i).getType() > 1) {
                point += 1;
                Flag = 1;
            }
            
            if (cardMax.isSmaller(this.playedCard.get(i))) {
                cardMax = this.playedCard.get(i);
                index = i;
            }
        }
        
        this.playedCard.removeAll(this.playedCard);
        System.out.println("INDEX     " + index);
        
        for (int i = 0; i < this.userManager.size(); ++i) {
            System.out.println("i     " + i + "++++" + this.userManager.get(i).getSttPlay());
            if (this.userManager.get(i).getSttPlay() == index) {
                this.userManager.setStrIndex(i);
//                break;
            }
        }
        
        System.out.println("STRT     " + this.userManager.getStrIndex());
        
        // Cap nhat lai sttPlay cho cac user
//        int j = this.userManager.getStrIndex();
//        for (int i = 0; i < this.userManager.size(); ++i) {
//            if (j == this.userManager.size())
//                j = 0;
//            this.userManager.get(j++).setSttPlay(i);
//        }
        
        
        if (point == 0)
            return "None win points-" + index + "-" + findNextPlay(this.userManager.getStrIndex());
        
        this.userManager.get(index).setPoint(point);
        return "winpoint-" + index + " " + point + "-" + findNextPlay(this.userManager.getStrIndex());
    }
    
    public String play(int indexOfClient, String dataFromClient) {
        String[] data = dataFromClient.split("-")[1].split(" ");
        String result = dataFromClient;
        
        Card card = new Card(Integer.parseInt(data[0]), Integer.parseInt(data[1])); // Nhan bai duoc danh tu client va dua vao mang
        playedCard.add(card);
                
        if (countFeedback == 3)
            result += "-3";
        else
            result += "-" + (countFeedback + 1);
        
        if (playedCard.size() == 4)
            result += "-" + isWinPoints();
     
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
                return this.login(dataFromClient.split("-")[1]);
            case 1: // Chiu trach nhiem lay accept va tao bo bai
                return this.createCard_Process();
            case 2: // Chia bai cho client
                return this.devideCard(this.card_13.get(indexOfClient));
            case 3:  // Nhan bai trao doi neu co
                return exchangeCard.receive3Card(indexOfClient, dataFromClient);
            case 4:  // Gui thu tu choi va bai sau khi duoc doi cho client
                return sendOrderPlayn3CardIfHave_Process(indexOfClient);
            case 5: 
                return play(indexOfClient, dataFromClient);
            default:
                throw new IllegalArgumentException("0,1,2,3,4");
        }
    }
}