package game.server;

import game.server.core.DataReceivedAnalysis;
import game.server.entity.Result;
import java.util.ArrayList;

class MyThread {
    private Thread thr;
    private int index;
    
    public MyThread(int index, IClientManager clientManager, DataReceivedAnalysis dataAnalysis, Solve solve) {
        this.index = index;
        thr = new Thread(){
             @Override
            public void run() {
                while (true) {
                    String dataReceived = clientManager.receive(index);
                    System.out.println("Client " + index + "--------" + dataReceived);

                    int state = dataAnalysis.resultAfterAnalysis(dataReceived);
                    System.out.println("State login: " + state);
                    Result result = solve.solvingForServer(state, index, dataReceived);
                    clientManager.send(result.getIndex(), result.getMessage());

                    if (!result.getMessage().contains("Duplicate username")) {
                        if (Solve.countFeedback == clientManager.getCount()) {
                            Solve.countFeedback = 0;
                            DataReceivedAnalysis.state += 1;
                        }
                        return;
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
        
        System.out.println("Number of client: " + Integer.toString(clientManager.getCount()));
        clientManager.sendAll("Welcome to Heart games ! Please, input username:");

        ArrayList<MyThread> thread = new ArrayList<>();
        for (int i = 0; i < 2; ++i) {
            thread.add(new MyThread(i, clientManager, dataAnalysis, solve));
            thread.get(i).Start();
        }
        
        while (true) {
            System.out.println();
            if (DataReceivedAnalysis.state >= 1) {
                if (!thread.isEmpty())
                    thread.removeAll(thread);

                for (int i = 0; i < clientManager.getCount(); i++){
                    if (DataReceivedAnalysis.state != 4) { // state bằng 4 đang là bước nhận xong bài client muốn trao đổi, tại bước này server không nhận được message do client gửi 
                                                            // vì tại state = 3 server không gửi message cho client (server phải nhận đủ bài từ client mới thực hiện trao đổi được)
                        String dataReceived = clientManager.receive(i);
                        System.out.println("Client " + i + "--------" + dataReceived);

                        int state = dataAnalysis.resultAfterAnalysis(dataReceived);
                        System.out.println("State: " + state);
                        Result result = solve.solvingForServer(state, i, dataReceived);

                        if (DataReceivedAnalysis.state != 3) // Đang là bước nhận  3 lá bài từ client => nhận đủ mới send
                            clientManager.send(result.getIndex(), result.getMessage());

                        if (result.getMessage().contains("Duplicate username")) --i; // nếu user đăng kí tên trùng thì tiếp tục để server lắng nghe socket đó
                    }
                    else { // State = 4 (nhận đủ bài) => gửi tin cho client
                        Result result = solve.solvingForServer(4, i, "");
                        System.out.println(result.getMessage());
                        clientManager.send(i, result.getMessage());
                    }

                    if (Solve.countFeedback == clientManager.getCount()) { // Biến đếm để kiểm tra server nhận đủ 4 gói tin (kết thúc 1 công đoạn) từ
                                                                            // client => nhảy qua công đaon5 khác khi đã nhận đủ 4 tin cho 1 giai đoạn
                        Solve.countFeedback = 0;
                        DataReceivedAnalysis.state += 1;
                    }
                }
            }
        }
    }
}
