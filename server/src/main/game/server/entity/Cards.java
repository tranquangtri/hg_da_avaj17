package game.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Cards {
    private ArrayList<Card> cards = new ArrayList<>();
    
    public Cards() { // Ham tao bo bai 52 la ./.
        for (int i = 1; i < 14; ++i) 
            for (int j = 0; j < 4; ++j) {
                if (i == 1) 
                    cards.add(new Card(14, j));
                else
                    cards.add(new Card(i, j));
            }
    }

    public Cards(ArrayList<Card> c){
        this.cards = c;
    }
    public Cards(String data) { // Ham tao bai cho user (13 la hoac 3 la)
        if (data == "")
            return;
        
        String[] card = data.split(" ");
        for (int i = 0; i < card.length; ++i)
            this.cards.add(new Card(Integer.parseInt(card[i]), Integer.parseInt(card[++i])));
    }
    
    public static String packedCardSToSendClient(ArrayList<Card> cards) {
        String data = "";
        for (int i = 0; i < cards.size(); ++i) {
            data += Integer.toString(cards.get(i).getValue()) + " " + Integer.toString(cards.get(i).getType());
            if (i != cards.size() - 1)
                data += " ";
        }
        System.out.println(data);
        return "Cards-" + data;
    }
    
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public ArrayList<Card> shuffleCards(ArrayList<Card> cards) { // ham xao bai ./.
        Set<Card> newCards = new HashSet<>();
        Random rand = new Random();
        
        while (newCards.size() != cards.size()) 
            newCards.add(cards.get(rand.nextInt(cards.size())));
        
        this.cards.removeAll(cards);
        this.cards = new ArrayList<>(newCards);
        
        return this.cards;
    }
    
    public ArrayList<ArrayList<Card>> divideCards(ArrayList<Card> card_52) {
        int j = 0;
        ArrayList<ArrayList<Card>> card_13 = new ArrayList<>();
        
        for (int i = 0; i < 4; ++i)
            card_13.add(new ArrayList<>());
        
        for (int i = 0; i < card_52.size(); ++i) {
            if (j == 4) j = 0;
            card_13.get(j++).add(card_52.get(i));
        }
        
        return card_13;
    }
    
    public int find2ClubIn4Cards(ArrayList<ArrayList<Card>> card_13) {
        for (int i = 0; i < card_13.size(); ++i)
            for (int j = 0; j < card_13.get(i).size(); ++j)
                if (card_13.get(i).get(j).getValue() == 2 && card_13.get(i).get(j).getType() == 1)
                    return i;
        return -1;
    }


    public void sortCard(boolean isAddCard) {
        if (isAddCard)
            shuffleCards(this.cards);

        for (int i = 0; i < cards.size() - 1; ++i)
            for (int j = i + 1; j < cards.size(); ++j)
                if (cards.get(i).isSmaller(cards.get(j))) {
                    Card tmp = new Card(cards.get(i));
                    cards.set(i, cards.get(j));
                    cards.set(j, tmp);
                }
    }
    public void delete(int index) {
        this.cards.remove(index);
    }

    public void deleteAll() {
        this.cards.removeAll(this.cards);
    }

    public void add(Card card) {
        this.cards.add(card);
    }

    public void add(int value, int type) {
        this.cards.add(new Card(value, type));
    }

    /*
       Đây là hàm đơn giản cho máy chơi tự động.
       Vì đây là hàm đơn giản nên ta chỉ xét chiến thuật cho máy đánh,
   chỉ quan tâm đến những lá bài của người chơi khác đã đánh ra trong lượt
   đang đánh, chỉ có khả năng nhớ tim vỡ hay chưa, nữ hoàng đã ra hay chưa.
   */
    public Card autoPlay(ArrayList<Card> others, boolean hasBeenHeartBroken){
        /*
Phan lam 2 truong hop:
- Nếu có 2 chuồn, phải đi 2 chuồn trước.
- Nếu mình là người cuối cùng trong một lượt đánh:
    - Nếu tim chưa vỡ, Heart has not broken yet.(không thể đánh Q1 nếu có)
        - Nếu có đúng nước: đánh con bài lớn nhất của nước đó mà ta có. Lúc này có thắng,
        ôm bài cũng không bị ghi điểm.
        - Nếu không đúng nước: nếu ta không có bài đúng nước, buộc phải đánh các nước khác.
            - Kiểm tra có K1 A1 không, nếu có: ta sẽ ưu tiên đánh 2 con này để loại bỏ
            càng sớm càng tốt, vì giữ 2 con này có nguy cơ cao ôm Q1
            - Nêu không: có thể đánh con lớn nhất mà ta có.
    - Nếu tim đã vỡ:
        - Kiểm tra bài mình có Queen Q1 hay không, nếu có:
            - Kiểm tra nước đang đánh, nếu nước đang đánh là 1:
                - Kiểm tra lá bài lớn nhất trong lượt, nếu lớn hơn Queen: đánh Queen,
                người giữ lá lớn nhất sẽ ôm Queen.
                - Nếu là lớn nhất nhỏ hơn Queen: không đánh Queen
                    - Kiểm tra có quân Cơ nào không, nếu có:
                        - Kiểm tra có lá bài nào nhỏ hơn lá lớn nhất không, nếu có: đánh
                        lá lớn nhất ta có nhưng nhỏ hơn lá lớn nhất của người chơi.
                        - Nếu không: đánh lá lớn nhất.
                    - Nêu không: đánh lá lớn nhất ta có, trừ Queen.
            - Nước đang đánh khác 0:
                - Mình có bài đúng nước đang đánh không: nếu có:
                    - Kiểm tra có quân Cơ không, nếu có:
                        - Kiểm tra có lá bài nào nhỏ hơn lá lớn nhất không, nếu có: đánh
                        lá lớn nhất ta có nhưng nhỏ hơn lá lớn nhất của người chơi.
                        - Nếu không: đánh lá lớn nhất.
                    - Nếu không: đánh quân lớn nhất mình có.
                - Nếu không, ưu tiên đánh Queen.
        - Nếu không:
            - Kiểm tra có Cơ hoặc Queen nào không, nếu có:
                - Kiểm tra có có bài đúng nước đó không, nếu có:
                    - Kiểm tra có lá bài nào nhỏ hơn lá lớn nhất không, nếu có: đánh lá
                    lớn nhất ta có nhưng nhỏ hơn lá lớn nhất của người chơi.
                    - Nếu không: đánh lá lớn nhất.
                - Nếu không: đánh lá bài lớn nhất. uu tien K0 A0
            - Nếu không có Cơ hoặc Queen:
                - Nếu có đúng nước: đánh quân lớn nhất trong nước đó.
                - Nếu không có bài đúng nước: đánh lá bài lớn nhất, ưu tiên đánh K0 A0.
- Mình không phải là người cuối cùng đánh:
        - Nếu tim chưa vỡ:
            - Nếu là người đầu tiên:
                - Nếu có K0, A0: đánh K0 A0
                - Nếu không: đánh quân lớn nhất.(trừ quân cơ)
            - Nếu không:
                - Nếu có đúng nước nước: đánh con lớn nhất.
                - Nếu không có đúng nước:
                    - Nếu có K0 A0: Ưu tiên loại nước K0 A0.
                    - Nêu không: đánh quân lớn nhất.
        - Nếu tim đã vỡ:
            - Nếu là người đầu tiên: đánh quân nhỏ nhất.
            - Nếu không:
                - Nếu có đúng nước:
			- Neu co nuoc vua nho hon nuoc lon nhat: danh nuoc lon nhat nho hon nuoc lon nhat nguoi choi.
			- Neu khong: danh nuoc nho nhat.
                - Nếu không đúng nước:
                    - Kiểm tra có quân Q0 không, nếu có: đánh Q0
                    - Nếu không:
                        - Kiểm tra còn quân cơ không, nếu có: đánh quân cơ lớn nhất.
                        - Nếu không: Đánh quân khác lớn nhất.
        */
        Card thrownCard = new Card();

        for(Card c1 : this.cards){
            if(c1.getType() == 1 && c1.getValue() == 2){
                thrownCard = c1;
                this.cards.remove(c1);
                return new Card(2, 1);
            }
        }
        if(others.size() == 3){                                                 //neu la nguoi cuoi cung
            if(hasBeenHeartBroken == false){                                    //neu tim chua vo
                int flag = -1;
                for(Card c1 : this.cards){                                  //kiem tra co cung nuoc khong
                    if(c1.getType() == others.get(0).getType())
                        flag = others.get(0).getType();
                }
                if(flag != -1){                                                 //neu co dung nuoc
                    thrownCard =  this.findMaxUnder(others.get(0).getType(), -1);
                    this.cards.remove(this.findMaxUnder(others.get(0).getType(), -1));
                    return thrownCard;
                }                                                         //neu khong co dung nuoc
                for(Card c1: this.cards){
                    if(c1.getType() == 0 && (c1.getValue() == 13 || c1.getValue() == 14)){
                        thrownCard = c1;
                        this.cards.remove(c1);
                        return thrownCard;
                    }
                }
                Card c1 = this.cards.get(this.findMaxValue());
                this.cards.remove(this.findMaxValue());
                return c1;
            }else{                                                              // neu tim da vo
                int QueenFlag = 0;
                for(Card c: this.cards){                                    //Vong lap kiem tra co Queen khong
                    if(c.getType() == 0 && c.getValue() == 12){
                        QueenFlag = 1;
                        break;
                    }
                }
                if(QueenFlag != 0){                                             //Neu co Queen
                    if(others.get(0).getType() == 0){                           //nuoc dang danh la 0: bich
                        for(Card c: others){                                    //Kiem tra co la bai nao la Bich va >=12 khong
                            if(c.getType() == 0 && c.getValue() >= 13){
                                thrownCard = new Card(0,12);
                                this.cards.remove(new Card(0,12));
                                return thrownCard;
                            }
                        }
                        int HeartFlag = 0;                                      //Neu khong co la nao type ==0 && >=12
                        for(Card c: others){
                            if(c.getType() == 3){
                                HeartFlag = 1;
                                break;
                            }
                        }
                        if(HeartFlag !=0){
                            //tim la bai lon nhat cua cac doi phuong
                            Card maxOthersCard = (new Cards(others)).findMaxValue(others.get(0).getType());
                            int Flag1 = 0;
                            for(Card c : this.cards){
                                if(c.getType() == 0 && c.getValue() < maxOthersCard.getValue()){
                                    Flag1 = 1;
                                    break;
                                }
                            }
                            if(Flag1 == 1){
                                thrownCard = this.findMaxUnder(0,maxOthersCard.getValue());
                                this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                                return thrownCard;
                            }
                            thrownCard = this.findMaxUnder(0,-1);
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                        this.cards.remove(new Card(0,12));
                        thrownCard = this.findMaxUnder(0, -1);
                        if(thrownCard == null)
                            return new Card(0,12);
                        this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                        this.cards.add(new Card(0,12));
                        return thrownCard;
                    }else{                                                      //khac type 0
                        int flag = 0;
                        for(Card c : this.cards){
                            if(c.getType() == others.get(0).getType()){
                                flag =1;
                                break;
                            }
                        }
                        if(flag ==1){
                            int Heartflag = 0;
                            for(Card c : others){
                                if(c.getType() == 3){
                                    Heartflag = 1;
                                    break;
                                }
                            }
                            if(Heartflag ==1){                                  //kiem tra neu co Co
                                Card maxOthersCard = (new Cards(others)).findMaxValue(others.get(0).getType());
                                int Flag1 = 0;
                                for(Card c : this.cards){
                                    if(c.getType() == others.get(0).getType() && c.getValue() < maxOthersCard.getValue()){
                                        Flag1 = 1;
                                        break;
                                    }
                                }
                                if(Flag1 == 1){
                                    thrownCard = this.findMaxUnder(others.get(0).getType(), maxOthersCard.getValue());
                                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                                    return thrownCard;
                                }
                                thrownCard = this.findMaxUnder(others.get(0).getType(), -1);
                                this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                                return thrownCard;
                            }
                            thrownCard = this.findMaxUnder(others.get(0).getType(), -1);
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                        thrownCard = new Card(0,12);
                        this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                        return thrownCard;
                    }
                }else{                                                          //Neu khong co Queen
                    int HeartFlag = 0;
                    for(Card c : others){//kiem tra co Co hoac 12-0 khong
                        if((c.getType() == 3)||(c.getType() == 0 && c.getValue() == 12)){
                            HeartFlag = 1;
                            break;
                        }
                    }
                    if(HeartFlag == 1){
                        int trueType = 0;
                        for(Card c : this.cards){// kiem tra minh co dung nuoc khong
                            if(c.getType() == others.get(0).getType()){
                                trueType = 1;
                                break;
                            }
                        }
                        if(trueType == 1){
                            Card maxOthersCard = (new Cards(others)).findMaxValue(others.get(0).getType());
                            for(Card c : this.cards){
                                if(c.getType() == others.get(0).getType() && c.getValue() < maxOthersCard.getValue()){
                                    thrownCard = this.findMaxUnder(others.get(0).getType(), maxOthersCard.getValue());
                                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                                    return thrownCard;
                                }
                            }
                            thrownCard = this.findMaxUnder(others.get(0).getType(), -1);
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                        for(Card c: this.cards){
                            if(c.getType() == 0 && (c.getValue() == 13 || c.getValue() == 14)){
                                thrownCard = c;
                                this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                                return thrownCard;
                            }
                            thrownCard = this.cards.get(this.findMaxValue());
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                    }
                    for(Card c : this.cards){// kiem tra minh co dung nuoc khong
                        if(c.getType() == others.get(0).getType()){
                            thrownCard = this.findMaxUnder(others.get(0).getType(), -1);
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                    }
                    for(Card c: this.cards){
                        if(c.getType() == 0 && (c.getValue() == 13 || c.getValue() == 14)){
                            thrownCard = c;
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                    }
                    thrownCard = this.cards.get(this.findMaxValue());
                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                    return thrownCard;
                }
            }
        }else{                                                                  // neu khong phai la nguoi cuoi cung
            if(hasBeenHeartBroken){ //neu tim chua vo
                if(others.size() == 0){//neu la nguoi dau tien danh
                    for(Card c : this.cards){
                        if(c.getType() == 0 && (c.getValue() == 13 || c.getValue() == 14)){
                            thrownCard = c;
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                    }
                    Cards cs = new Cards();
                    for(Card c : this.cards){
                        if(c.getType() != 3){
                            cs.cards.add(c);
                        }
                    }
                    thrownCard = this.cards.get(this.findMaxValue());
                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                    return thrownCard;
                }
                //neu khong phai la nguoi dau tien danh
                for(Card c: this.cards){
                    if(c.getType() == others.get(0).getType()){
                        thrownCard = this.findMaxUnder(others.get(0).getType(), -1);
                        this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                        return thrownCard;
                    }
                }
                for(Card c: this.cards){
                    if(c.getType() == 0 && (c.getValue() == 13 || c.getValue() == 14)){
                        thrownCard = c;
                        this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                        return thrownCard;
                    }
                }
                for(Card c: this.cards){
                    if(c.getType() == 0 && c.getValue() == 12){
                        this.cards.remove(c);
                        thrownCard = this.cards.get(this.findMaxValue());
                        this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                        this.cards.add(new Card(0,12));
                        return thrownCard;
                    }
                }
                thrownCard = this.cards.get(this.findMaxValue());
                this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                return thrownCard;
            }
            //neu tim da vo
            if(others.size() == 0){//neu la nguoi dau tien danh
                thrownCard = this.cards.get(this.findMinValue());
                this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                return thrownCard;
            }
            //neu khong phai la nguoi dau tien danh
            for(Card c : this.cards){
                if(c.getType() == others.get(0).getType()){
                    Card maxOthersCard = (new Cards(others)).findMaxValue(others.get(0).getType());
                    for(Card cc : this.cards){
                        if(cc.getType() == maxOthersCard.getType() && cc.getValue() < maxOthersCard.getValue()){
                            thrownCard = this.findMaxUnder(maxOthersCard.getType(), maxOthersCard.getValue());
                            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                            return thrownCard;
                        }
                    }
                    thrownCard = c;
                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                    return thrownCard;
                }
            }
            for(Card c:this.cards){
                if(c.getType() == 0 && c.getValue() == 12){
                    thrownCard = c;
                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                    return thrownCard;
                }
            }
            for(Card c: this.cards){
                if(c.getType() == 3){
                    thrownCard = this.findMaxUnder(3, -1);
                    this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
                    return thrownCard;
                }
            }
            thrownCard = this.cards.get(this.findMaxValue());
            this.cards.remove(new Card(thrownCard.getType(),thrownCard.getValue() ));
            return thrownCard;
        }
    }
    /*
    Tim la bai lon nhat ta co thoa dieu kien nho hon mot la bai nao do.
    Neu underValue == -1; co nghia la khong co gia tri tren, nen ta chi can tim
    la bai lon nhat ta co.
    */
    public Card findMaxUnder(int type, int underValue){
        if(underValue == -1){
            int flag = -1;
            Card finalCard = new Card();
            for(Card c : this.cards){
                if(c.getType() == type){
                    flag = type;
                }
                if(flag != -1 && c.getType() != type){
                    break;
                }
                finalCard = c;
            }
            if(flag == -1){
                return null;
            }
            return finalCard;

        }else{
            ArrayList<Integer> value = new ArrayList();
            for(Card c : this.cards){
                if(c.getType() == type){
                    value.add(c.getValue());
                }
            }
            if(value.size() == 0){
                return null;
            }
            int finalvalue = 0;
            for(int i : value){
                if(i >= underValue){
                    break;
                }
                finalvalue = i;
            }
            for(Card c: this.cards){
                if(c.getType() == type && c.getValue() == finalvalue){
                    return c;
                }
            }
        }
        return null;
    }
    Card findMaxValue(int type){
        Cards L = new Cards();
        for(Card c : this.cards){
            if(c.getType() == type){
                L.cards.add(c);
            }
        }
        int indexMax = L.findMaxValue();
        Card card = new Card(type, indexMax);
        return card;
    }
    int findMaxValue(){
        int max = 0;
        int index = -1;
        int i = 0;
        for(Card c : this.cards){
            if(c.getValue() > max){
                index = i;
                max = c.getValue();
            }
            if(c.getValue() == max && c.getType() == 3){
                index = i;
            }
            i++;
        }
        return index;
    }
    int findMinValue(){
        int min = 15;
        int index = -1;
        int i = 0;
        for(Card c : this.cards){
            if(c.getValue() < min){
                index = i;
                min = c.getValue();
            }
            if(c.getValue() ==  min && c.getType() == 1){
                index = i;
            }
            i++;
        }
        return index;
    }

    /*
    -Đây là hàm cho máy tự động chọn ra 3 lá bài để đổi với người chơi khác khi tự động chơi.
    -Chiến lược sẽ là ưu tiên bỏ/ chuyển Q bích, K bích, A bích qua các người chơi khác vì
    nếu nắm giữ nhưng lá bài này sẽ có nguy cơ cao nhận được Queen khi đánh (dễ ăn 13 điểm)
    -Còn lại sẽ ưu tiên chuyển cho người chơi khác các lá bài lớn nhất trong tay, ưu tiên quân cơ
    vì quân cơ chỉ được đánh từ lúc tim vỡ về sau, lúc này tỉ lệ các người chơi khác ra quân cơ cao
    hơn lúc đầu, nên nếu giữ lại quân cơ lớn ta dễ ăn quân cơ của người chơi khác hơn. Còn các quân khác
    lớn ta có thể tranh thủ đánh lúc đầu, lúc tim chưa vỡ, dù có ăn ta cũng không bị ghi điểm.
    */
    public ArrayList<Card> autoExchange(){
        ArrayList<Card> exchangedCard = new ArrayList();
        int flagQ1 = 0, flagK1 = 0, flagA1 = 0;
        Card Q1 = new Card();
        Card K1 = new Card();
        Card A1 = new Card();
        for(Card c: this.cards){
            if(c.getValue() == 12 && c.getType() == 0){
                flagQ1 = 1;
                Q1 = c;
                exchangedCard.add(c);
            }
            if(c.getValue() == 13 && c.getType() == 0){
                flagK1 = 1;
                K1 = c;
                exchangedCard.add(c);
            }
            if(c.getValue() == 14 && c.getType() == 0){
                flagA1 = 1;
                A1 = c;
                exchangedCard.add(c);
            }

        }
        int num = 3;
        if(flagQ1 ==1){
            this.cards.remove(Q1);
            num--;
        }
        if(flagK1 ==1){
            this.cards.remove(K1);
            num--;
        }
        if(flagA1 ==1){
            this.cards.remove(A1);
            num--;
        }
        for(;num > 0;num--){
            exchangedCard.add(this.cards.get(this.findMaxValue()));
            this.cards.remove(this.findMaxValue());
        }
        return exchangedCard;
    }
}
