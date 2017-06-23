package game.server.core;


import game.server.ClientSolve;
import game.server.IClient;
import game.server.entity.Card;
import game.server.entity.Cards;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class NPCClient implements IClient {
    BlockingDeque<String> msgSendQueue;
    BlockingDeque<String> msgReceiveQueue;
    private String id;
    
    public NPCClient(String id){
        msgSendQueue = new LinkedBlockingDeque<>(100);
        msgReceiveQueue = new LinkedBlockingDeque<>(100);
        this.id = id;
        new Thread(new ClientThread(id, msgSendQueue, msgReceiveQueue)).start();
    }
    
    @Override
    public void send(String message){
        try {
            msgSendQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String receive(){
        String ret = null;
        try {
            ret = msgReceiveQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }
}

class ClientThread implements Runnable {
    BlockingQueue<String> msgSendQueue;
    BlockingQueue<String> msgReceiveQueue;
    private String id;
    private String dataFromServer;
    private ClientSolve solve;

    public ClientThread(String id, BlockingQueue<String> msgSendQueue,
                        BlockingQueue<String> msgReceiveQueue) {
        this.msgSendQueue = msgSendQueue;
        this.msgReceiveQueue = msgReceiveQueue;
        solve = new ClientSolve();
        dataFromServer = "";
        this.id = id;
    }

    private void send(String msg) {
        try {
            msgReceiveQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receiveDataAndAnalysis() {
        try {
            dataFromServer = msgSendQueue.take();
            System.out.println("NPC RECEIVED: " + dataFromServer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void receivedSTTPlay(Cards myCards) {
        // nhan data
        receiveDataAndAnalysis();
        
        boolean isPlay = solve.receiveSTTPlayAndExchangeCardIfHaving(dataFromServer);
        
        if (isPlay) {
            Card card = myCards.autoPlay(solve.getCardsPlayed().getCards(), false);
            System.out.println(solve.getUser().getUserName() +  " VVVVVVVVVVVVVV " + card.getValue() + "-" + 
                               card.getType() + "SIZE " + solve.getCards().getCards().size());
            send("Card played-" + card.getValue() + " " + card.getType());
        }
    }

    @Override
    public void run() {
        try {
            receiveDataAndAnalysis();
            send("Username-" + id);
            receiveDataAndAnalysis();
            solve.getInforOfUser(dataFromServer);

            while (true) {
                send("Accept");
                System.out.println("SENDED");
                receiveDataAndAnalysis();

                send("Devide card"); // Server gui tin la: cho chia bai, client gui tin yeu cau chia bai
                receiveDataAndAnalysis();

                solve.receivedCardFromServer(dataFromServer); // Nhan 13 la bai tu server
                solve.receivedUsersFromServer(dataFromServer);

                Cards myCards = solve.getCards();

                if (solve.getIsExchangeCard()) {
                    solve.setCardExchange(new Cards(myCards.autoExchange()));
                    send(solve.exchnageCard());
                    receivedSTTPlay(myCards); // nhan thu tu choi, neu npc la nguoi choi dau tien thi danh bai trong ham nay 
                } 

                else { // Neu khong nhan duoc yeu cau trao doi bai thi yeu cau server gui thu tu choi
                    send("SttPlay");
                    receivedSTTPlay(myCards);
                }

                while (true) {
                    receiveDataAndAnalysis();
                    ArrayList<Integer> result = solve.play(dataFromServer);

                    if (result.get(0) == 4) { // th gom bai: an diem, khong an diem, wingame

                        solve.updateOrderOfNewPlay(dataFromServer);
                        solve.getCardsPlayed().deleteAll();

                        result.set(0, solve.getUser().getSttPlay()); // cap nhat lai vi tri danh

                        if (dataFromServer.contains(" end")) {
                            solve.reset();
                            System.out.println("RESETED DONE +++++++++++++++++++++++++++");
                            break;
                        }
                    }

                    if (solve.getUser().getSttPlay() == result.get(0) && 
                        solve.getCardsPlayed().getCards().size() == result.get(0)) {

                        Card card = myCards.autoPlay(solve.getCardsPlayed().getCards(), solve.getBreakingHeart());
                        System.out.println(solve.getUser().getUserName() +  " VVVVVVVVVVVVVV " + card.getValue() + "-" + 
                                           card.getType() + "SIZE " + solve.getCards().getCards().size());

                        String msg = "";

                        if (myCards.getCards().isEmpty() == true && 
                           (solve.getCardsPlayed().getCards().size() == 3)) 
                            msg = "Card played-" + card.getValue() + " " + card.getType() + "-end";

                        else 
                            msg = "Card played-" + card.getValue() + " " + card.getType();

                        send(msg);
                    }
                }
            }
        }  
        catch (Exception ex) {
            return;
        }
    }
}