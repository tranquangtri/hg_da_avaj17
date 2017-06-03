/*
 * Copyright [2017] [TTT group]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package game.server;

import java.util.ArrayList;
import java.util.List;

interface Exchanger{
    public void exchange(ExchangeCard exchangeCard);
}
class ExchangerClockWise implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        String tmp = exchangeCard.get(exchangeCard.size() - 1);
        for (int i = exchangeCard.size() - 1; i > 0; --i)
            exchangeCard.set(i, exchangeCard.get(i - 1));
        exchangeCard.set(0, tmp);
    }
}
class ExchangerCounterClockWise implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        String tmp = exchangeCard.get(0);

        for (int i = exchangeCard.size() - 1; i > 0; --i)
            exchangeCard.set(i - 1, exchangeCard.get(i));

        exchangeCard.set(exchangeCard.size() - 1, tmp);
    }
}
class ExchangerFaceToFace implements Exchanger{

    @Override
    public void exchange(ExchangeCard exchangeCard) {
        for (int i = 0; i < 2; ++i) {
            String tmp = exchangeCard.get(i);
            exchangeCard.set(i, exchangeCard.get(i + 1));
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
        if (exchangeCardTime == 0) return new ExchangerClockWise();
        if (exchangeCardTime == 1) return new ExchangerCounterClockWise();
        if (exchangeCardTime == 3) return new ExchangerFaceToFace(); //!!
        return new ExchangerNoExchange();
    }
    public ExchangeCard(){
        exchangeCard = new ArrayList<>();
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
        if (exchangeCardTime == 4) exchangeCardTime =0;
        return exchangeCardTime;
    }

    /**
     * Tìm vị trí quân 3 bích <b>trong bộ bài trao đổi</b>
     * @return
     */
    public int find3BlangesIfHaving() {
        for (int i = 0; i < this.exchangeCard.size(); ++i) {
            int index1 = this.exchangeCard.get(i).indexOf("3");
            int index2 = this.exchangeCard.get(i).indexOf("0");
            if ((index1 == -1 && index2 == -1) && (index2 == index1 + 1))
                return i;
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
    public String receive3Card(String dataFromClient) {
        if (dataFromClient.contains("3Cards"))
            exchangeCard.add("Exchange card-" + dataFromClient.split("-")[1]);
        return "Waiting to exchange card";
    }
    public void set(int index, String item){
        exchangeCard.set(index, item);
    }
}
