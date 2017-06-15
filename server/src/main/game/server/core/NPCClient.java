package game.server.core;


import game.server.ClientSolve;
import game.server.IClient;
import game.server.entity.Card;
import game.server.entity.Cards;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;

class NPCClient implements IClient {
    BlockingDeque<String> msgSendQueue;
    BlockingDeque<String> msgReceiveQueue;
    public NPCClient(){
        msgSendQueue = new LinkedBlockingDeque<>(100);
        msgReceiveQueue = new LinkedBlockingDeque<>(100);
        new Thread(new ClientThread(msgSendQueue, msgReceiveQueue)).start();
    }
    public void send(String message){
        try {
            msgSendQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
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
    private String dataFromServer;
    private Server server;
    private ClientSolve solve;
    private int step;

    public ClientThread(BlockingQueue<String> msgSendQueue,
                        BlockingQueue<String> msgReceiveQueue) {
        this.msgSendQueue = msgSendQueue;
        this.msgReceiveQueue = msgReceiveQueue;
        solve = new ClientSolve();
        dataFromServer = "";
    }

    private void send(String msg) {
        try {
            msgReceiveQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("client-send:"+msg);
    }

    private String receive() {
        try {
            return msgSendQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void receiveDataAndAnalysis() { // Nhan du lieu tu server va phan tich xem yeu cau cua server la gi
        dataFromServer = receive();
        step = new DataReceivedAnalysis().resultAfterAnalysis(dataFromServer);

        System.out.println("client-data:" + dataFromServer);
        System.out.println("step:" + step);
    }

    private void receivedSTTPlay() {
        // nhan data
        receiveDataAndAnalysis();
    }

    @Override
    public void run() {
        System.out.println(receive());
        send("Username-" + Integer.toString(this.hashCode()));
        receive();
        send("Accept");
        receiveDataAndAnalysis();
        send("Devide card"); // Server gui tin la: cho chia bai, client gui tin yeu cau chia bai
        receiveDataAndAnalysis();
        solve.receivedCardFromServer(dataFromServer); // Nhan 13 la bai tu server
        Cards myCards = solve.getCards();
        if (solve.getIsExchangeCard()) { // Neu nhan duoc yeu cau trao doi bai tu server thi chuyen xuong isAccept = 1
            solve.setCardExchange(new Cards(myCards.autoExchange()));
            send(solve.exchnageCard());
            receivedSTTPlay();
            boolean isPlay = solve.receiveSTTPlayAndExchangeCardIfHaving(dataFromServer);
            if (isPlay) {
                //
                Card card = myCards.autoPlay(solve.getCardsPlayed().getCards(), false);
                send("Card played-" + card.getValue() + " " + card.getType());
            }
        } else { // Neu khong nhan duoc yeu cau trao doi bai thi yeu cau server gui thu tu choi
            send("SttPlay");
            receivedSTTPlay();
        }
        while (true) {
            receiveDataAndAnalysis();
            ArrayList<Integer> result = solve.play(dataFromServer);
            System.out.println("Solve:"+dataFromServer + "  result:"+result.get(0));
            if (result.size() > 1) {
                if (solve.getUser().getSttPlay() == result.get(1)) {
                    solve.setTypeOfCard(-1);
                }

                solve.updateOrderOfNewPlay(dataFromServer);
                solve.getCardsPlayed().deleteAll();

                if (dataFromServer.contains(" end")) {
                    step = 1;
                    DataReceivedAnalysis.state = 1;
                    solve.reset();
                }
            }
            if (result.get(0) == 0) {
                Card card = myCards.autoPlay(solve.getCardsPlayed().getCards(), false);
                if (myCards.getCards().isEmpty() == true && solve.getCardsPlayed().getCards().size() == 3)
                    send("Card played-" + card.getValue() + " " + card.getType() + "-end");
                else
                    send("Card played-" + card.getValue() + " " + card.getType());
            }
            while (true) ;
        }
    }
}