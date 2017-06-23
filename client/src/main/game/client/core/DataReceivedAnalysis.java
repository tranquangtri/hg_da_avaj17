package game.client.core;

/*
Xử lí client-server tuân thủ theo nguyên tắc chung:
    + Lớp DataReceivedAnalysis sẽ nhận message được gửi đến và phân tích xem đó là message gì và cho lớp Solve biết
      thông qua 1 biến int.
    + Lớp solve dựa vào giá trị do lớp DataReceivedAnalysis trả ra và đưa ra xử lí thích hợp.
*/

public class DataReceivedAnalysis {
    public static int state = 0;
    
    public DataReceivedAnalysis(){}
    
    private int login(String dataReceived){
        if (dataReceived.contains("Duplicate username"))
            return -2;
        else if (dataReceived.contains("Welcome to Heart games"))
            return -1;
        else if (dataReceived.contains("should click 'ACCEPT' to start game")) {
            state = 1;
            return 0; // User nay la user moi ./.
        }
        return -10;
    } // Nhan yeu cau phai login
    
    private int waitngToDevideCard(String dataReceived) {
        if (dataReceived.contains("Waiting to devide card")) {
            state = 2;
            return 1;
        }
        return -10;
    } // Nhan yeu cau cho chia bai
    
    private int receive13Cards(String dataReceived) {
        if (dataReceived.contains("Cards-")) {
            state = 3;
            return 2;
        }
        return -10;
    } // Nhan yeu cau nhan 13 la bai
                  
    private int receiveSTTPlayAnd3Cards(String dataReceived) {
        if (dataReceived.contains("Exchange card-") == true || 
            dataReceived.contains("STTPlay-") == true) {
            state = 4;
            return 3;
        }
        return -10;
    }

    private int receivePlayedCard(String dataReceived) {
        if (dataReceived.contains("Card played")) {
            return 4;
        }
        return -10;       
    }
    
    
    
    
    
    public int resultAfterAnalysis(String dataReceived) {
        if (dataReceived != null) {
            switch (state) {
                case 0: {
                    return login(dataReceived);
                }
                case 1: {
                    return waitngToDevideCard(dataReceived);
                }
                case 2: {
                    return receive13Cards(dataReceived);
                }
                case 3: {
                    return receiveSTTPlayAnd3Cards(dataReceived);
                }
                case 4: {
                    return receivePlayedCard(dataReceived);
                }
            }
        }
        return -10;
    }
}
