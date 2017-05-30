package game.server;

import game.server.core.DataReceivedAnalysis;
import game.server.entity.Result;
import game.server.core.Solve;
import game.server.database.DataConnection;



public class RoutineServer implements IClientHandler{
    private IClientManager clientManager;
    public void setClientManager(IClientManager clientManager){
        this.clientManager = clientManager;
    }
    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        /**
         *  Xử lý server  ở đây
         *  Example:
         *  1. Gửi "Welcome to Heart games !" đến các client
         *  2. Client gửi trả lại Welcome Server
         *
         */
        Solve solve = Solve.Instance();
        DataConnection con = new DataConnection();
        DataReceivedAnalysis dataAnalysis = new DataReceivedAnalysis();
        
        if (con != null) {
            System.out.println("Number of client: " + Integer.toString(clientManager.getCount()));
            clientManager.sendAll("Welcome to Heart games ! Please, input username:");
            
           
            while (true) {
                for (int i = 0; i < clientManager.getCount(); i++){
                    if (DataReceivedAnalysis.state != 4) { // state bằng 4 đang là bước nhận xong bài client muốn trao đổi, tại bước này server không nhận được message do client gửi 
                                                            // vì tại state = 3 server không gửi message cho client (server phải nhận đủ bài từ client mới thực hiện trao đổi được)
                        String dataReceived = clientManager.receive(i);
                        System.out.println("Client " + i + "--------" + dataReceived);

                        int state = dataAnalysis.resultAfterAnalysis(dataReceived, con.getConnection());
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
