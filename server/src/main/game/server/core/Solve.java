/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.core;

import java.util.ArrayList;

import game.server.database.DataConnection;
import game.server.entity.Card;
import game.server.entity.Cards;
import game.server.entity.Result;
import game.server.entity.User;



public class Solve {
    private static Solve instance = null; // Singleton
    private ArrayList<User> users = null; // Quan li cac user
    private ArrayList<String> exchnageCard = null; // Quan li 3 bo bai trao doi 3 la
    private ArrayList<ArrayList<Card>> card_13 = null; // Quan li 4 bo bai 13 la
    private int exchangeCardTime; // Lan doi bai: co 4 lan doi bai
    private int flag = 0; // Trong qua trinh lam co 2 giai doan server khong gui tin cho client ma doi nhan du thong tin tu
                         // client do la nhan accept va nhan bai trao doi.
    
    public static int countFeedback = 0; // Dem so lan nhan phan hoi tu client. Neu du thi tro ve 0

    
    private Solve() {
        this.users = new ArrayList<>();
        this.exchnageCard = new ArrayList<>();
        this.exchangeCardTime = 0;
    }
    
    public static Solve Instance() {
        if (instance == null) {
            instance = new Solve();
        }
        return instance;
    }
    
    public int numberUsers() {
        return this.users.size();
    }
    
    
    
    //===================================================================================
    //========================= Cac ham ho tro de xu li message =========================
    //===================================================================================
    
    
    
    private Boolean isDuplicateRegisterUserName(String userName) {
        for (int i = 0; i < this.users.size(); ++i)
            if (userName.equals(this.users.get(i).getUserName()))
                return true;
        return false;
    } // Kiem tra user co bi trung khong
    
    private void exchangeCard() {
        switch (this.exchangeCardTime) {
            case 0: { // Lan 1: doi bai theo chieu kim dong ho
                String tmp = this.exchnageCard.get(this.exchnageCard.size() - 1);
                
                for (int i = this.exchnageCard.size() - 1; i > 0; --i)
                    this.exchnageCard.set(i, this.exchnageCard.get(i - 1));
                
                this.exchnageCard.set(0, tmp);
                break;
            }
            case 1: { // Lan 2: doi bai nguoc chieu kim dong ho
                String tmp = this.exchnageCard.get(0);
                
                for (int i = this.exchnageCard.size() - 1; i > 0; --i)
                    this.exchnageCard.set(i - 1, this.exchnageCard.get(i));
                
                this.exchnageCard.set(this.exchnageCard.size() - 1, tmp);
                break;
            }
            case 3: { // Lan 3: doi bai cho nguoi doi dien 
                for (int i = 0; i < 2; ++i) {
                    String tmp = this.exchnageCard.get(i);
                    this.exchnageCard.set(i, this.exchnageCard.get(i + 1));
                    this.exchnageCard.set(i + 2, tmp);
                }
                break;
            }
            case 4: { // Lan 4: khong doi bai, set bien exchangeCardTime = 0
                this.exchangeCardTime = 0;
                return;
            }
        }
    } // Trao doi bai
    
    private int find3BlangesInExchangeCardIfHaving() {
        for (int i = 0; i < this.exchnageCard.size(); ++i) {
            int index1 = this.exchnageCard.get(i).indexOf("3");
            int index2 = this.exchnageCard.get(i).indexOf("0");
            if ((index1 == -1 && index2 == -1) && (index2 == index1 + 1))
                 return i;
        }
        return -1;
    } // Kiem tra trong bai trao doi co 3 bich khong, neu co
                                                                    // thi sap xep lai thu tu choi
    
    private void sortSTTPlay(int startIndex) {
        if (startIndex != -1) {
            for (int i = 0; i != this.users.size();) {
                if (startIndex == this.users.size())
                    startIndex = 0;
                this.users.get(startIndex++).setSttPlay(i++);
            }
        }
        
        for (int i = 0; i < this.users.size(); ++i)
            System.out.println(this.users.get(i).getSttPlay());
    } // Sap xep thu tu choi
    
    
    
    
    //===================================================================================
    //========================= Cac ham xu li message chinh =============================
    //===================================================================================
    
    
    
    private String login(String userName) {
        User user = DataConnection.getUserIfHaving(userName);
        String dataSendClient = "";
        
        if (user.isEmptyUser()) {
            if (DataConnection.insert(userName, 0, 0) == false) // tao user moi nhung user do da co nguoi tao
                return "Duplicate username";
            user = new User(userName, 0, 0, 0);
        } 
        else if (isDuplicateRegisterUserName(userName)) // dang nhap user da co nhung da co nguoi lay xai truoc
            return "Duplicate username";
        
        countFeedback += 1;
        dataSendClient =  userName + " " + user.getMatches() + " " + user.getWinMatches() + "-should click 'ACCEPT' to start game" ;
            
        this.users.add(user);
        return dataSendClient;
    }
    
    private String createCard() {
        if (flag == 0) {
            Cards cards = new Cards(); // tao bo bai ./.
            cards.setCards(Cards.shuffleCards(cards.getCards())); // xao bai
            
            this.card_13 = Cards.divideCards(cards.getCards()); // chia bai thanh 4 bo moi bo 13 la
            this.sortSTTPlay(Cards.find3Blanges(card_13)); // Sap xep thu tu choi ban dau
            flag = 1;
        }
        return "Waiting to devide card";
    }
    
    private String packedCardSToSendClient(ArrayList<Card> cards) {
        String data = "";
        for (int i = 0; i < cards.size(); ++i) {
            data += Integer.toString(cards.get(i).getValue()) + " " + Integer.toString(cards.get(i).getType());
            if (i != cards.size() - 1)
                data += " ";
        }
        System.out.println(data);
        return "Cards-" + data;
    }
    
    private String devideCard(ArrayList<Card> cards) {
        String data = "Card3";
        
        if (this.exchangeCardTime != 3) 
            data += packedCardSToSendClient(cards);
        else 
            data = packedCardSToSendClient(cards);
        return data;
    }
    
    private String receive3Card(String dataFromClient) {
        if (dataFromClient.contains("3Cards")) 
            this.exchnageCard.add("Exchange card-" + dataFromClient.split("-")[1]);
        return "Waiting to exchange card";
    }
    
    private String sendSTTPlayAnd3CardIfHaving(int indexOfClient) {
        if (flag == 1) {
            this.exchangeCard();
            this.sortSTTPlay(this.find3BlangesInExchangeCardIfHaving());
            flag = 2;
        }
        if (!this.exchnageCard.isEmpty()) {
            return "STTPlay-" + this.users.get(indexOfClient).getSttPlay() + "-" + this.exchnageCard.get(indexOfClient);
        }
        return "STTPlay-" + this.users.get(indexOfClient).getSttPlay();
    }
    
    
    
    
    //===================================================================================
    //========================= Ham xu li tat ca cac message ============================
    //===================================================================================
    
    public Result solvingForServer(int state, int indexOfClient, String dataFromClient) {
        String data = "";
        
        switch (state) {
            case 0: { // Chiu trach nhiem login
                data = this.login(dataFromClient.split("-")[1]);
                break;
            }
            case 1: { // Chiu trach nhiem lay accept va tao bo bai
                data = this.createCard();
                break;
            }
            case 2: { // Chia bai cho client
                data = this.devideCard(this.card_13.get(indexOfClient));
                break;
            }
            case 3: { // Nhan bai trao doi neu co
                data = this.receive3Card(dataFromClient);
                break;
            }
            case 4: { // Gui thu tu choi va bai sau khi duoc doi cho client
                data = sendSTTPlayAnd3CardIfHaving(indexOfClient);
                break;
            }
        }
        
        if (state != 0)
            countFeedback += 1;
        
        return new Result(data, indexOfClient);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //    private String checkEnoughAcceptFromClients(ArrayList<Card> cards) {
//        for (int i = 0; i < this.users.size(); ++i)
//            if (this.users.get(i).getState() != 1)
//                return "Waiting";
//        return "Enough Accept-" + packedCardSToSendClient(cards);
//    }
//    
//    private boolean sortThePlayingOrder(ArrayList<ArrayList<Card>> card_13) {
//        int index = -1;
//        for (int i = 0; i < card_13.size(); ++i)
//            for (int j = 0; j < card_13.get(i).size(); ++j)
//                if (card_13.get(i).get(j).getValue() == 3 && card_13.get(i).get(j).getType() == 0) {
//                    index = i;
//                    break;
//                }
//        
//        if (index == -1)
//            return false;
//                    
//        int flag = 0;
//        while (flag != 4) {
//            if (index == this.users.size())
//                index = 0;
//            this.users.get(index++).setSttPlay(flag++);
//        }
//        
//        return true;
//    }
}
