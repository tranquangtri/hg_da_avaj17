package game.server;

import game.server.core.DataReceivedAnalysis;
import game.server.entity.Result;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class MyThread {
    private final Thread thr;
    private int index;
    
    public MyThread(int index, IClientManager clientManager, DataReceivedAnalysis dataAnalysis, Solve solve) {
        this.index = index;
        thr = new Thread(){
             @Override
            public void run() {
                while (true) {
                    String dataReceived = clientManager.receive(index);
                    int state = dataAnalysis.resultAfterAnalysis(dataReceived);
                    Result result;
                    synchronized (solve) {
                        result = solve.solvingForServer(state, index, dataReceived);
                    }
                    System.out.println("Client " + index + "--------" + dataReceived);
                    System.out.println("State login: " + state);
                    
                    clientManager.send(result.getIndex(), result.getMessage());

                    if (!result.getMessage().contains("Duplicate username")) {
                        synchronized (solve) {
                            if (Solve.countFeedback == clientManager.getCount()) {
                                Solve.countFeedback = 0;
                                DataReceivedAnalysis.state += 1;
                            }
                            return;
                        }
                    }
                }
            }
        };
    }
    
    public void Start() {
        this.thr.start();
    }
}



public class RoutineServer implements IClientHandler{
    private IClientManager clientManager;
    public void setClientManager(IClientManager clientManager){
        this.clientManager = clientManager;
    }
    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
       
        Solve solve = new Solve();
        DataReceivedAnalysis dataAnalysis = new DataReceivedAnalysis();
        
        // Vi giao dien load len qua cham trong khi server da gui tin nen phia client khong the xu li duoc message dan
        // den loop forever
        // De khac phuc: phia server can cho khoang 2s de toan bo giao dien duoc load len sau do moi gui tin ./.
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RoutineServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Number of client: " + Integer.toString(clientManager.getCount()));
        clientManager.sendAll("Welcome to Heart games ! Please, input username:");

        ArrayList<MyThread> thread = new ArrayList<>();
        for (int i = 0; i < clientManager.getCount(); ++i) {
            thread.add(new MyThread(i, clientManager, dataAnalysis, solve));
            thread.get(i).Start();
        }
        
        int i = 0;
        
        while (true) {
            if (DataReceivedAnalysis.state >= 1) {
                
                if (i == clientManager.getCount()) 
                    i = 0;

                for (; i < clientManager.getCount(); i++){
                    System.out.println("________________Listening client: " + i);
                    if (DataReceivedAnalysis.state != 4) { // state bằng 4 đang là bước nhận xong bài client muốn trao đổi, tại bước này server không nhận được message do client gửi 
                                                            // vì tại state = 3 server không gửi message cho client (server phải nhận đủ bài từ client mới thực hiện trao đổi được)
                        String dataReceived = clientManager.receive(i);
                        int state = dataAnalysis.resultAfterAnalysis(dataReceived);
                        Result result = solve.solvingForServer(state, i, dataReceived);
                        
                        System.out.println("Client " + i + " sended:" + dataReceived);
                        System.out.println("State: " + state);
                        System.out.println("Send to client: " + result.getMessage());

                        if (DataReceivedAnalysis.state < 3) // Đang là bước nhận  3 lá bài từ client => nhận đủ mới send
                            clientManager.send(i, result.getMessage());
                        if (DataReceivedAnalysis.state > 4) {
                            clientManager.sendAll(result.getMessage());
                            System.out.println("Send all: " + result.getMessage());
                            if (result.getMessage().contains(" end"))
                                solve.reset();
                        }
                        //clientManager.sendAll("should click 'ACCEPT' to start game");

                    }
                    else { // State = 4 (nhận đủ bài) => gửi tin cho client
                        Result result = solve.solvingForServer(4, i, "");
                        System.out.println(result.getMessage());
                        clientManager.send(i, result.getMessage());
                        
                    }

                    if (Solve.countFeedback == clientManager.getCount()) { // Biến đếm để kiểm tra server nhận đủ 4 gói tin (kết thúc 1 công đoạn) từ
                                                                            // client => nhảy qua công đaon5 khác khi đã nhận đủ 4 tin cho 1 giai đoạn
                        Solve.countFeedback = 0;
                        
                        if (DataReceivedAnalysis.state < 5) 
                            DataReceivedAnalysis.state += 1;
                        
                        i = solve.getUserManager().getStrIndex() - 1;
                        System.out.println("Start index: " + (i + 1));
                    }
                }
            } else {
                System.out.println();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RoutineServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
