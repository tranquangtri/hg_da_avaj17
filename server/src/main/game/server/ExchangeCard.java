package game.server;

import java.util.ArrayList;
import java.util.List;

interface Exchanger{
    public void exchange(ExchangeCard exchangeCard);
}
class ExchangerClockWise implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        String tmp = exchangeCard.get(0);
        for (int i = 0; i < exchangeCard.size() - 1; ++i)
            exchangeCard.set(i, exchangeCard.get(i + 1));
        exchangeCard.set(exchangeCard.size() - 1, tmp);
    }
}
class ExchangerCounterClockWise implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        String tmp = exchangeCard.get(exchangeCard.size() - 1);
        for (int i = exchangeCard.size() - 1; i > 0; --i) 
            exchangeCard.set(i, exchangeCard.get(i - 1)); 
        exchangeCard.set(0, tmp);
    }
}
class ExchangerFaceToFace implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        for (int i = 0; i < 2; ++i) {
            String tmp = exchangeCard.get(i);
            exchangeCard.set(i, exchangeCard.get(i + 2));
            exchangeCard.set(i + 2, tmp);
        }
    }
}
class ExchangerNoExchange implements Exchanger{
    @Override
    public void exchange(ExchangeCard exchangeCard) {

    }
}

/**
 * <p>Lưu giữ các bộ 3 quân bải cần trao đổi</p>
 * <p>Người sử dụng class này có thể thêm một hoặc nhiều quân bài qua phương thức
 * <tt>add</tt> hoặc <tt>receive3Card</tt></p>
 * <p>Class không immutate, các quân bài trao đổi được lưu
 * bên trong có thể bị <b>thay đổi</b> thông qua phương thức <tt>exchange</tt></p>
 * @implNote <tt>ExchangeCard</tt> thực chất là một wrapper của List nhưng thêm vào đó
 * một số chức năng
 */
final class ExchangeCard{
    private final List<String> exchangeCard;
    private Exchanger exchanger;
    
    public static Exchanger getExchangerFromTime(int exchangeCardTime){
        if (exchangeCardTime == 0) return new ExchangerClockWise(); // trao doi nguoc chieu kim dong ho
        if (exchangeCardTime == 1) return new ExchangerCounterClockWise(); // trao doi cung chieu kim dong ho
        if (exchangeCardTime == 3) return new ExchangerFaceToFace(); // trao doi cho phia doi dien
        return new ExchangerNoExchange();
    }
    
    public ExchangeCard(){
        exchangeCard = new ArrayList<>();
        for (int i = 0; i < 4; ++i)
            exchangeCard.add("");
    }

    /**
     * <p>Trao đổi bải, làm <ud><b>thay đổi trạng thái</b></ud> của đối tượng được gọi</p>
     * <p>Sau khi trao đổi, có thể truy xuất đến các quân bài thông qua phương thức <tt>get</tt></p>
     * @param exchangeCardTime (đọc thêm luật chơi của heartGame)</i>
     * @return
     */
    public int exchange(int exchangeCardTime) {
        Exchanger exchanger = getExchangerFromTime(exchangeCardTime);
        exchanger.exchange(this);
        if (exchangeCardTime == 4) exchangeCardTime = 0;
        return exchangeCardTime;
    }

    /**
     * Tìm vị trí quân 2 chuồn <b>trong bộ bài trao đổi</b>
     * @return
     */
    public int find2ClubIfHaving() {
        for (int i = 0; i < this.exchangeCard.size(); ++i) {
            try {
                String[] data = this.exchangeCard.get(i).split("-")[1].split(" ");
                for (int j = 0; j < data.length; ++j)
                    if ("2".equals(data[j]) && (j % 2 == 0) && "1".equals(data[j + 1]))
                        return i;
            }
            catch (Exception ex) {break;}
        }
        return -1;
    }
    
    public String get(int index){
        return exchangeCard.get(index);
    }
    public int size(){
        return exchangeCard.size();
    }
    public void add(String item){
        exchangeCard.add(item);
    }
    public boolean isEmpty(){
        return exchangeCard.isEmpty();
    }
    public String receive3Card(int indexOfClient, String dataFromClient) {
        if (dataFromClient.contains("3Cards"))
            exchangeCard.set(indexOfClient, "Exchange card-" + dataFromClient.split("-")[1]);
        return "Waiting to exchange card";
    }
    public void set(int index, String item){
        exchangeCard.set(index, item);
    }
}
