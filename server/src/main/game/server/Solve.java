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

import game.server.entity.Card;
import game.server.entity.Cards;
import game.server.entity.Result;

public class Solve {
    public enum OnlyReceiveServerFlag{
        ReceiveAccept,
        ReceiveExchange,
        Normal
    }
    private UserManager userManager;
    private ExchangeCard exchangeCard;
    private ArrayList<ArrayList<Card>> card_13 = null; // Quan li 4 bo bai 13 la
    private int exchangeCardTime; // Lan doi bai: co 4 lan doi bai
    private OnlyReceiveServerFlag flag; // Trong qua trinh lam co 2 giai doan server khong gui tin cho client ma doi nhan du thong tin tu
    // client do la nhan accept va nhan bai trao doi.
    public static int countFeedback = 0; // Dem so lan nhan phan hoi tu client. Neu du thi tro ve 0

    public Solve() {
        flag = OnlyReceiveServerFlag.ReceiveAccept;
        exchangeCard = new ExchangeCard();
        userManager = new UserManager();
        this.exchangeCardTime = 0;
    }

    public String login(String userName) {
        return userManager.login(userName);
    }

    private String createCard_Process() {
        if (flag == OnlyReceiveServerFlag.ReceiveAccept) {
            Cards cards = new Cards(); // tao bo bai ./.
            cards.setCards(cards.shuffleCards(cards.getCards())); // xao bai
            this.card_13 = cards.divideCards(cards.getCards()); // chia bai thanh 4 bo moi bo 13 la
            userManager.sortSTTPlay(cards.find3Blanges(card_13)); // Sap xep thu tu choi ban dau
            flag = OnlyReceiveServerFlag.ReceiveExchange;
        }
        return "Waiting to devide card";
    }

    private String devideCard(ArrayList<Card> cards) {
        String data = "Card3";
        if (this.exchangeCardTime != 3)
            data += Cards.packedCardSToSendClient(cards);
        else
            data = Cards.packedCardSToSendClient(cards);
        return data;
    }

    public String sendOrderPlayn3CardIfHave_Process(int indexOfClient) {
        if (flag == OnlyReceiveServerFlag.ReceiveExchange) {
            exchangeCardTime = exchangeCard.exchange(exchangeCardTime);
            userManager.sortSTTPlay(exchangeCard.find3BlangesIfHaving());
            flag = OnlyReceiveServerFlag.Normal;
        }
        String result = "";
        int sttPlay = userManager.get(indexOfClient).getSttPlay();

        if (!exchangeCard.isEmpty()) {
            result = "STTPlay-" + sttPlay + "-" + exchangeCard.get(indexOfClient);
        } else
            result = "STTPlay-" + sttPlay;

        if (sttPlay == 0)
            result += "-start";

        return result;
    }

    public Result solvingForServer(int state, int indexOfClient, String dataFromClient) {
        String data = "";
        data = getData(state, indexOfClient, dataFromClient, data);
        countFeedback += 1;
        return new Result(data, indexOfClient);
    }

    private String getData(int state, int indexOfClient, String dataFromClient, String data) {
        switch (state) {
            case 0:  // Chiu trach nhiem login
                return this.login(dataFromClient.split("-")[1]);
            case 1: // Chiu trach nhiem lay accept va tao bo bai
                return this.createCard_Process();
            case 2: // Chia bai cho client
                return this.devideCard(this.card_13.get(indexOfClient));
            case 3:  // Nhan bai trao doi neu co
                return exchangeCard.receive3Card(dataFromClient);
            case 4:  // Gui thu tu choi va bai sau khi duoc doi cho client
                return sendOrderPlayn3CardIfHave_Process(indexOfClient);
            default:
                throw new IllegalArgumentException("0,1,2,3,4");
        }
    }
}