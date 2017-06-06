package game.server.core;

import java.sql.Connection;

/*
Xử lí client-server tuân thủ theo nguyên tắc chung:
    + Lớp DataReceivedAnalysis sẽ nhận message được gửi đến và phân tích xem đó là message gì và cho lớp Solve biết
      thông qua 1 biến int.
    + Lớp solve dựa vào giá trị do lớp DataReceivedAnalysis trả ra và đưa ra xử lí thích hợp.
*/


public class DataReceivedAnalysis {
    public static int state = 0;
    
    public DataReceivedAnalysis() {}
    
    private int login(String dataReceived) {
        if (dataReceived.contains("Username-") || dataReceived.contains("Username again-"))
            return 0;
        return -10;
    } // nhan yeu cau la login
    
    private int accept(String dataReceived) {
        if (dataReceived.contains("Accept"))
            return 1;
        return -10;
    } // nhan yeu cau la accept
    
    private int devideCard(String dataReceived) {
        if (dataReceived.contains("Devide card"))
            return 2;
        return -10;
    } // nhan yeu cau la chia bai
    
    private int exchange3CardsOrNo(String dataReceived) {
        if (dataReceived.contains("3Cards") || dataReceived.contains("SttPlay"))
            return 3;
        return -10;
    } // nhan yeu cau trao doi 3 la bai hoac choi game
    
    
    
    
    
    
    public int resultAfterAnalysis(String dataReceived) {
        switch (state) {
            case 0: {
                return login(dataReceived); // Nhan thong tin login
            }
            case 1: {
                return accept(dataReceived); // Nhan thong tin accept
            }
            case 2: {
                return devideCard(dataReceived); // Nhan thong tin yeu cau chia bai
            }
            case 3: {
                return exchange3CardsOrNo(dataReceived); //
            }
        }
        return -1;
    }
}
