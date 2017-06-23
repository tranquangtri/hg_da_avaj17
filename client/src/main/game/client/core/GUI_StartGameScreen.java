package game.client.core;

import game.client.entity.Card;
import game.client.entity.Cards;
import game.client.entity.UICards;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;



public class GUI_StartGameScreen extends javax.swing.JFrame {

    private static String dataFromServer; 
    private static Server server;
    private static Solve solve;
    private static int step;
    
    
    private static int isAccept;
    private static int isClick;
    private static int isPlayNPC;
   
    private static UICards cardsPlayingScreen;
    private static UICards cardsPlayedScreen;
    
    private static ArrayList<JLabel> scoreUsers;
    private static NPCManager npcManager;
    
   
    private Thread playOnline;
    private Thread playNPC;
    
    public GUI_StartGameScreen() {
        initComponents();
        
        this.settingPlayedCard();
        this.settingPlayingCard();
        this.settingScore();
        
        // Set thong tin ban dau cho giao dien ./.
        
        GUI_StartGameScreen.isAccept = 0;
        GUI_StartGameScreen.isClick = 0;
        GUI_StartGameScreen.isPlayNPC = 0;
        
        
        GUI_StartGameScreen.txt_Username.setVisible(false);
        GUI_StartGameScreen.label_Username_Login.setVisible(false);
        GUI_StartGameScreen.button_Login.setVisible(false);
        
        // Set thong tin ban dau cho xu li
        
        GUI_StartGameScreen.solve = new Solve();
        GUI_StartGameScreen.dataFromServer = "";
    }
    
    private void delay(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {}
    }
    
    
    
    //------------------------------------------------------------------------------------------------------
    //++++++++++++++++++++++           CAC THREAD TAO RA DE XU LI TRONG PROJECT           ++++++++++++++++++
    //------------------------------------------------------------------------------------------------------
    
    
    
    private void login() {
        new Thread(()-> {
           if (GUI_StartGameScreen.txt_Username.getText().equals("")) {
                GUI_StartGameScreen.label_Message.setText("Please fill usernam. Try new name");
                return;
            }

            GUI_StartGameScreen.server.send("Username-" + GUI_StartGameScreen.txt_Username.getText());
            GUI_StartGameScreen.receiveDataAndAnalysis(); // Nhan du lieu tu server va phan tich xem do la thong tin gi (tra ra bien toan cuc step)

            switch (GUI_StartGameScreen.step) {
                case -2:
                    GUI_StartGameScreen.label_Message.setText("Username is duplicate. Try new name");
                    GUI_StartGameScreen.label_Message.setVisible(true);
                    break;
                case 0:
                    // Set gia tri cho man hinh info

                    GUI_StartGameScreen.solve.getInforOfUser(dataFromServer);
                    label_Username_Info.setText(GUI_StartGameScreen.solve.getUser().getUserName());
                    label_WinMatches.setText("Win matches: " + GUI_StartGameScreen.solve.getUser().getWinMatches());
                    label_AllMatches.setText("All matches: " + GUI_StartGameScreen.solve.getUser().getMatches());
                    // Hien thi man hinh ke tiep va tat man hinh cu

                    playForm.setVisible(true);
                    infoForm.setVisible(true);
                    loginForm.setVisible(false);
                    break;
                default:
                    break;
            }
        }).start();
    }
    
    //++++++++++++++++++++++                       Choi online                             +++++++++++++++++
    
    
    private void accept_next() {
        new Thread(()-> {
             try {
                 if (GUI_StartGameScreen.isAccept == 0) {
                    if (isClick == 1)return;

                    GUI_StartGameScreen.isClick = 1; // bien de ngan khong cho nguoi dung click nhieu lan vao button
                    GUI_StartGameScreen.server.send("Accept"); // gui Accept chap nhan choi game
                    receiveDataAndAnalysis();

                    GUI_StartGameScreen.server.send("Devide card"); // Server gui tin la: cho chia bai, client gui tin yeu cau chia bai
                    receiveDataAndAnalysis();

                    GUI_StartGameScreen.solve.receivedCardFromServer(dataFromServer); // Nhan 13 la bai tu server
                    receiveCardAndShowScreen();

                    settingUserScoreOnlineInScreen();

                    if (solve.getIsExchangeCard()) // Neu nhan duoc yeu cau trao doi bai tu server thi chuyen xuong isAccept = 1
                        label_Introdution.setText("Choose 3 cards to exchange");
                    else { // Neu khong nhan duoc yeu cau trao doi bai thi yeu cau server gui thu tu choi 
                        label_Introdution.setText("Waiting to start game");
                        server.send("SttPlay");
                        receivedSTTPlay();
                        GUI_StartGameScreen.isAccept += 1;
                    }
                    GUI_StartGameScreen.isClick = 0;
                }
                else if (GUI_StartGameScreen.isAccept == 1) {

                    if (GUI_StartGameScreen.isClick == 1) 
                        return;

                    if (solve.getCardExchange().getCards().size() != 3) {
                        label_Introdution.setText("Choose 3 cards");
                        return;
                    }

                    GUI_StartGameScreen.isClick = 1;

                    // set icon cho bai da gui di = trong
                    cardsPlayedScreen.setCardsInScreenNull();

                    // send data
                    label_Introdution.setText("Waiting to start game");
                    server.send(solve.exchnageCard());

                    receivedSTTPlay();

                    // cap nhat du lieu cho man hinh
                    for (int i = 0; i < 13; ++i) 
                        cardsPlayingScreen.setCardsInScreen(i, solve.getCard(i));
                    GUI_StartGameScreen.isClick = 0;
                }

                GUI_StartGameScreen.isAccept += 1;
                button_Exit.setVisible(true);
             }
             catch (Exception ex) {}
        }).start();
    }
    
    private void playOnline() {
        playOnline = new Thread() {
           @Override
           public void run() {
               while (true) {
                    try {
                        if (step >= 3) {
                            receiveDataAndAnalysis();
                            ArrayList<Integer> result = solve.play(dataFromServer);

                            if (step < 0 || result == null) return; // mot hay nhieu player mat ket noi

                            if (result.get(0) == 0)
                                cardsPlayingScreen.setEnablePlayingCards(solve.getCardsPlayed().getCards().get(0), solve.getCards());
                            else cardsPlayingScreen.setEnableForAllCards(false);

                            cardsPlayedScreen.setCardsOnScreen(solve.getCardsPlayed());

                            if (result.size() > 1) {
                                if (result.size() >= 4) { //th: an diem 
                                    String[] data = scoreUsers.get(result.get(2)).getText().split(" ");
                                    int score = score = result.get(3) + Integer.parseInt(data[1]);
                                    scoreUsers.get(result.get(2)).setText(data[0] + " " + score);

                                    delay(1);

                                    if (result.size() == 5)
                                        JOptionPane.showMessageDialog(null, "Player " + scoreUsers.get(result.get(4)).getText().split(":")[0] + "win game ");
                                }


                                if (solve.getUser().getSttPlay() == result.get(1)) 
                                    cardsPlayingScreen.setEnablePlayingCardsForUserWin(solve.getBreakingHeart(), solve.getCards());
                                else cardsPlayingScreen.setEnableForAllCards(false);

                                solve.updateOrderOfNewPlay(dataFromServer);
                                solve.getCardsPlayed().deleteAll();
                                label_sttPlay.setText(Integer.toString(solve.getUser().getSttPlay()));

                                if (scoreUsers.get(0).getText().contains("@NPC_")) delay(1);
                                cardsPlayedScreen.setCardsInScreenNull();

                                if (dataFromServer.contains(" end")) {
                                    isAccept = isClick = 0;
                                    step = 1; DataReceivedAnalysis.state = 1;
                                    solve.reset();
                                    cardsPlayingScreen.setEnableForAllCards(true);
                                    cardsPlayingScreen.setCardsBeforePlay();
                                    label_sttPlay.setText("");
                                    label_Introdution.setText("Click \"ACCEPT\" to start game");
                                    label_Introdution.setVisible(true);
                                }
                            }
                        }
                        else {
                            if (loginForm.isVisible() == false && playForm.isVisible() == false) return;
                            System.out.println();
                            delay(1);
                        }
                    }
                    catch (HeadlessException | NumberFormatException ex) {
                        return;
                    }
               }
           }
        };
        playOnline.start();
    }
    
    
    //++++++++++++++++++++++                       Choi voi may                             ++++++++++++++++
    
    
    private void accept_next_withNPC() {
        new Thread(()-> {
            try {
                if (GUI_StartGameScreen.isAccept == 1) {
                    solve.setCards(new Cards(npcManager.devideCardsForPlayer())); // nhan 13 la bai tu NPC MANAGER
                    solve.getCards().sortCard(false);

                    receiveCardAndShowScreen(); // hien thi bai len giao dien
                    settingUserScoreOfflineInScreen(); // setting user cho giao dien

                    if (npcManager.getTimeExchange() != 2) 
                    {
                        label_Introdution.setText("Choose 3 cards to exchange");
                        step = 2;
                    }
                    else 
                    { 
                        step = 3;
                        label_Introdution.setText("Waiting to start game");
                        solve.getUser().setSttPlay(npcManager.findSTTPlayForPlayer());
                        if (solve.getUser().getSttPlay() != 0) {
                            cardsPlayingScreen.setEnableForAllCards(false);
                            if (!playNPC.isAlive()) playNPC.start();
                        }
                        else 
                            cardsPlayingScreen.setEnablePlayingCards(null, solve.getCards());
                        GUI_StartGameScreen.isAccept += 1;
                    }
                }
                else if (GUI_StartGameScreen.isAccept == 2) {

                    if (solve.getCardExchange().getCards().size() != 3) {
                        label_Introdution.setText("Choose 3 cards");
                        return;
                    }

                    // set icon cho bai da gui di = trong
                    cardsPlayedScreen.setCardsInScreenNull();

                    solve.receiveExchangeCardIfHavingOffline(npcManager.exchangeCardForPlayer(solve.getCardExchange().getCards()));
                    solve.getUser().setSttPlay(npcManager.findSTTPlayForPlayer());

                    label_sttPlay.setText(Integer.toString(solve.getUser().getSttPlay()));
                    label_Introdution.setText("Waiting to start game");

                    if (solve.getUser().getSttPlay() != 0) {
                        cardsPlayingScreen.setEnableForAllCards(false);
                        if (!playNPC.isAlive()) playNPC.start();
                    }
                    else 
                        cardsPlayingScreen.setEnablePlayingCards(null, solve.getCards());       

                    // cap nhat du lieu cho man hinh
                    for (int i = 0; i < 13; ++i) 
                        cardsPlayingScreen.setCardsInScreen(i, solve.getCard(i));

                    step = 3;
                }

                GUI_StartGameScreen.isAccept += 1;
                button_Exit.setVisible(true);
            }
            catch (HeadlessException ex){
            }
        }).start();
    }
    
    private void playOffline() {
        playNPC = new Thread() {
            @Override
            public void run() {
                while (true) {
                     try {
                         ArrayList<Card> cardPlayedOfNPC = npcManager.autoPlay();

                        for (int i = 0; i < cardPlayedOfNPC.size(); ++i) {
                            solve.getCardsPlayed().add(cardPlayedOfNPC.get(i));
                            cardsPlayedScreen.setCardsInScreen(i, cardPlayedOfNPC.get(i));
                        }

                        if (isAccept > 2 && (npcManager.getCardPlayed().size() > 0 && npcManager.getCardPlayed().size() != 4)) 
                            cardsPlayingScreen.setEnablePlayingCards(solve.getCardsPlayed().getCards().get(0), solve.getCards());


                        if (npcManager.getCardPlayed().size() == 4) {
                            int[] winPoint = npcManager.findIndexPlayMaxCard();
                            if (winPoint[1] != 0) { // th co an diem
                                String[] data = scoreUsers.get(winPoint[0]).getText().split(" ");
                                int point = winPoint[1] + Integer.parseInt(data[1]);
                                scoreUsers.get(winPoint[0]).setText(data[0] + " " + Integer.toString(point));
                            }

                            delay(2);
                            cardsPlayedScreen.setCardsInScreenNull();

                            int newSTTPlay = npcManager.updateSTTPlay(winPoint[0]);
                            label_sttPlay.setText(Integer.toString(newSTTPlay));

                            if (newSTTPlay == 0)
                                cardsPlayingScreen.setEnablePlayingCardsForUserWin(npcManager.getIsBreakingHeart(), solve.getCards());
                            else cardsPlayingScreen.setEnableForAllCards(false);

                            if (!solve.getCards().getCards().isEmpty()) { // th ket thuc game dau
                                npcManager.deletePlayedCard();
                                solve.getCardsPlayed().deleteAll();
                            }
                            else {
                                npcManager.reset();
                                solve.resetOffline();
                                step = isAccept = 1;
                                label_sttPlay.setText("");
                                cardsPlayedScreen.setCardsInScreenNull();
                                cardsPlayingScreen.setEnableForAllCards(true);
                                cardsPlayingScreen.setCardsBeforePlay();
                            }
                        }
                     }
                     catch (NumberFormatException ex) {
                         return;
                     }
                }
            }
        };
    }
    
    
    //------------------------------------------------------------------------------------------------------
    //++++++++++++++++++++++           CAC HAM VIET THEM DE HO TRO XU LI SU KIEN           +++++++++++++++++
    //------------------------------------------------------------------------------------------------------
    
    //++++++++++++++++++++++                     Setting giao dien                ++++++++++++++++++++++++++
    
    
    private void settingPlayingCard() {
        cardsPlayingScreen = new UICards();
        
        cardsPlayingScreen.addCards(label_Card1);
        cardsPlayingScreen.addCards(label_Card2);cardsPlayingScreen.addCards(label_Card3);cardsPlayingScreen.addCards(label_Card4);
        cardsPlayingScreen.addCards(label_Card5);cardsPlayingScreen.addCards(label_Card6);cardsPlayingScreen.addCards(label_Card7);
        cardsPlayingScreen.addCards(label_Card8);cardsPlayingScreen.addCards(label_Card9);cardsPlayingScreen.addCards(label_Card10);
        cardsPlayingScreen.addCards(label_Card11);cardsPlayingScreen.addCards(label_Card12);cardsPlayingScreen.addCards(label_Card13);
       
        
        for (int i = 0; i < cardsPlayingScreen.getSize(); ++i) {
            cardsPlayingScreen.getUICard(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (step == 2 && solve.getCardExchange().getCards().size() != 3) {
                        Card card = playCard(e);
                        if (card != null) {
                            solve.getCardExchange().getCards().add(card);
                            int indexOfScreen = solve.getCardExchange().getCards().size() - 1;
                            cardsPlayedScreen.setCardsInScreen(indexOfScreen, card);
                        }
                    }
                    
                    else if (step >= 3) {
                        Card card = playCard(e);
                        if (card != null) {
                            if (isPlayNPC == 0) {
                                if (isLongerCard() == true && solve.getCardsPlayed().getCards().size() == 3)
                                    server.send("Card played-" + card.getValue() + " " + card.getType() + "-end");
                                else 
                                    server.send("Card played-" + card.getValue() + " " + card.getType());
                            }
                            else if (isPlayNPC == 1) {
                                solve.getCardsPlayed().add(card);
                                npcManager.addPlayedCard(card);
                                if (!playNPC.isAlive())
                                    playNPC.start();
                            }
                        }
                    }
                    
                    for (int i = 0; i < cardsPlayingScreen.getSize(); ++i) 
                            cardsPlayingScreen.getUICard(i).setLocation(cardsPlayingScreen.getUICard(i).getLocation().x, 510);
                }
                
                @Override
                public void mouseEntered(MouseEvent e) { // non hover
                    JLabel cardHover = (JLabel)e.getSource();
                    if (cardHover.isEnabled()) {
                        cardHover.setLocation(cardHover.getLocation().x, 506);
                        int index = findingIndexLabelInArray(cardHover);
                        cardsPlayingScreen.setUICard(index, cardHover);
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) { // hover
                    JLabel cardHover = (JLabel)e.getSource();
                    if (cardHover.isEnabled()) {
                        cardHover.setLocation(cardHover.getLocation().x, 510);
                        int index = findingIndexLabelInArray(cardHover);
                        cardsPlayingScreen.setUICard(index, cardHover);
                    }
                }

            });
        }
    }
    
    private void settingPlayedCard() {
        cardsPlayedScreen = new UICards();
        cardsPlayedScreen.addCards(label_Card14);cardsPlayedScreen.addCards(label_Card15);
        cardsPlayedScreen.addCards(label_Card16);cardsPlayedScreen.addCards(label_Card17);
    }
    
    
   
    private void settingScore() {
        scoreUsers = new ArrayList<>();
        scoreUsers.add(label_ScorePlayer0); scoreUsers.add(label_ScorePlayer1);
        scoreUsers.add(label_ScorePlayer2); scoreUsers.add(label_ScorePlayer3);
    }
    
    private void settingUserScoreInScreen(String[] userNames) {
        int j = 0;
        String line = "";
        
        for (int i = 0; i < userNames.length; ++i) {
            scoreUsers.get(i).setText(userNames[i].toUpperCase() + ": 0");
            if (userNames[i].equals(solve.getUser().getUserName()))
                j = i;
        }
        
        for (int i = 0; i < solve.getUser().getUserName().length(); ++i)
            line += "_";
        
        label_aboveline.setText(line);
        label_belowline.setText(line);
        
        // set vi tri cho label line
        
        label_aboveline.setLocation(scoreUsers.get(j).getLocation().x,  label_aboveline.getLocation().y);
        label_belowline.setLocation(scoreUsers.get(j).getLocation().x,  label_belowline.getLocation().y);
    }
    
    //++++++++++++++++++++++                            ONLINE                ++++++++++++++++++++++++++++++
    
    private void settingUserScoreOnlineInScreen() {
        if (!scoreUsers.get(0).getText().contains("SCORE: 0"))
            return;
  
        String[] userNames = GUI_StartGameScreen.solve.receivedUsersFromServer(dataFromServer); // Nhan cac user tu server
        settingUserScoreInScreen(userNames);
    }
    
    //++++++++++++++++++++++                            OFFLINE                +++++++++++++++++++++++++++++
    
    private void settingUserScoreOfflineInScreen() {
        if (!scoreUsers.get(0).getText().contains("SCORE: 0"))
            return;
        
        solve.getUser().setUserName("Player");
        String[] userNames = {"@NPC_0", "@NPC_1", "@NPC_2", "Player"};
        settingUserScoreInScreen(userNames);
    }
    
    //++++++++++++++++++++++        Cac ham ho tro hien thi bai len giao dien      +++++++++++++++++++++++++++
    
   
    
    private int findingIndexLabelInArray(JLabel label) {
        if (label.isEnabled()) {
            for (int i = 0; i < cardsPlayingScreen.getSize(); ++i)
                if (cardsPlayingScreen.getUICard(i).equals(label))
                    return i;
        }
        return -1;
    }
    
    private void sortCard(int index) {
        int N = cardsPlayingScreen.getSize()- 1;
        for (int i = index; i < N; ++i)
            cardsPlayingScreen.getUICard(i).setIcon(cardsPlayingScreen.getUICard(i + 1).getIcon());
        cardsPlayingScreen.getUICard(N).setIcon(null);
        cardsPlayingScreen.getUICard(N).setEnabled(false);
    }
    
    private static boolean isLongerCard() {
        return cardsPlayingScreen.getUICard(0).getIcon() == null;
    }
    
    private Card playCard(MouseEvent e) {
        int index = findingIndexLabelInArray((JLabel)e.getSource());
        
        if (index == -1)
            return null;
        
        Card card = solve.getCard(index);
       
        //sap xep lai bai
        sortCard(index);
        solve.sortCard(index);
        
        return card;
    }
    
    
    
    //++++++++++++++++++++++        Nhan du lieu tu server va xu li        ++++++++++++++++++++++++++++++++++++
    
    
    
    @SuppressWarnings("empty-statement")
    private static void receiveDataAndAnalysis() { // Nhan du lieu tu server va phan tich xem yeu cau cua server la gi
        try {
            dataFromServer = server.receive();
            step = new DataReceivedAnalysis().resultAfterAnalysis(dataFromServer);
            
            if (step == -10) {
               JOptionPane.showMessageDialog(null, "One player is exited. Now we will disconnect");
               GUI_StartGameScreen.server.close();
               
               try {
                   playForm.dispose();
                   infoForm.dispose();
               }
               catch(Exception e){
               };
            }
            System.out.println(dataFromServer);
            System.out.println(step);
        }
        catch (HeadlessException ex) {
            step = -10;
           // server.close();
        }
    }
    
    private static void receivedSTTPlay() {
        // nhan data
        receiveDataAndAnalysis();
        boolean isPlay = solve.receiveSTTPlayAndExchangeCardIfHaving(dataFromServer);
        
        // Neu client khong phai la nguoi danh bai dau tien thi enable cac la bai
        if (!isPlay)
            cardsPlayingScreen.setEnableForAllCards(false);
        else 
            cardsPlayingScreen.setEnablePlayingCards(null, solve.getCards());
        

        label_sttPlay.setText(Integer.toString(solve.getUser().getSttPlay()));
        label_Introdution.setVisible(false);
    }
    
    private void receiveCardAndShowScreen() {
        for (int i = 0; i < solve.getCards().getCards().size(); ++i) 
            cardsPlayingScreen.setCardsInScreen(i, solve.getCards().getCards().get(i));
    }
    
 
    
     //------------------------------------------------------------------------------------------------------
    //++++++++++++++++++++++         ANH XA CAC SU KIEN TU GIAO DIEN VAO CODE           +++++++++++++++++++++
    //-------------------------------------------------------------------------------------------------------
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        choosePlayingTypeForm = new javax.swing.JFrame();
        button_Online = new javax.swing.JButton();
        button_NPC = new javax.swing.JButton();
        label_background_ChoosePlayingType = new javax.swing.JLabel();
        loginForm = new javax.swing.JFrame();
        txt_Username = new javax.swing.JTextField();
        label_Username_Login = new javax.swing.JLabel();
        label_Message = new javax.swing.JLabel();
        button_Login = new javax.swing.JButton();
        label_Background_Login = new javax.swing.JLabel();
        infoForm = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        label_Username_Info = new javax.swing.JLabel();
        label_WinMatches = new javax.swing.JLabel();
        label_AllMatches = new javax.swing.JLabel();
        button_Done = new javax.swing.JButton();
        label_Background_Info = new javax.swing.JLabel();
        playForm = new javax.swing.JFrame();
        label_sttPlay = new javax.swing.JLabel();
        label_Introdution = new javax.swing.JLabel();
        label_Card1 = new javax.swing.JLabel();
        label_Card2 = new javax.swing.JLabel();
        label_Card3 = new javax.swing.JLabel();
        label_Card4 = new javax.swing.JLabel();
        label_Card5 = new javax.swing.JLabel();
        label_Card6 = new javax.swing.JLabel();
        label_Card7 = new javax.swing.JLabel();
        label_Card8 = new javax.swing.JLabel();
        label_Card9 = new javax.swing.JLabel();
        label_Card10 = new javax.swing.JLabel();
        label_Card11 = new javax.swing.JLabel();
        label_Card12 = new javax.swing.JLabel();
        label_Card13 = new javax.swing.JLabel();
        label_Card14 = new javax.swing.JLabel();
        label_Card15 = new javax.swing.JLabel();
        label_Card16 = new javax.swing.JLabel();
        label_Card17 = new javax.swing.JLabel();
        label_belowline = new javax.swing.JLabel();
        label_aboveline = new javax.swing.JLabel();
        label_ScorePlayer0 = new javax.swing.JLabel();
        label_ScorePlayer1 = new javax.swing.JLabel();
        label_ScorePlayer2 = new javax.swing.JLabel();
        label_ScorePlayer3 = new javax.swing.JLabel();
        button_Accept_Next = new javax.swing.JButton();
        button_Exit = new javax.swing.JButton();
        label_Background_Play = new javax.swing.JLabel();
        button_Start = new javax.swing.JButton();
        label_Background_Startgame = new javax.swing.JLabel();

        choosePlayingTypeForm.setTitle("Choosing playing type");
        choosePlayingTypeForm.setMinimumSize(new java.awt.Dimension(400, 400));
        choosePlayingTypeForm.setResizable(false);
        choosePlayingTypeForm.getContentPane().setLayout(null);

        button_Online.setIcon(new javax.swing.ImageIcon("C:\\Users\\TRANQUANGTRUNG\\Desktop\\java\\client\\src\\resources\\Images\\online_button.png")); // NOI18N
        button_Online.setBorderPainted(false);
        button_Online.setContentAreaFilled(false);
        button_Online.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_OnlineMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_OnlineMouseExited(evt);
            }
        });
        button_Online.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_OnlineActionPerformed(evt);
            }
        });
        choosePlayingTypeForm.getContentPane().add(button_Online);
        button_Online.setBounds(140, 140, 130, 60);

        button_NPC.setIcon(new javax.swing.ImageIcon("C:\\Users\\TRANQUANGTRUNG\\Desktop\\java\\client\\src\\resources\\Images\\npc_button.png")); // NOI18N
        button_NPC.setBorderPainted(false);
        button_NPC.setContentAreaFilled(false);
        button_NPC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_NPCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_NPCMouseExited(evt);
            }
        });
        button_NPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_NPCActionPerformed(evt);
            }
        });
        choosePlayingTypeForm.getContentPane().add(button_NPC);
        button_NPC.setBounds(140, 220, 130, 60);

        label_background_ChoosePlayingType.setIcon(new javax.swing.ImageIcon("C:\\Users\\TRANQUANGTRUNG\\Desktop\\java\\client\\src\\resources\\Images\\background_info.png")); // NOI18N
        choosePlayingTypeForm.getContentPane().add(label_background_ChoosePlayingType);
        label_background_ChoosePlayingType.setBounds(0, 0, 400, 400);

        loginForm.setTitle("Login");
        loginForm.setMinimumSize(new java.awt.Dimension(400, 400));
        loginForm.setResizable(false);
        loginForm.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                loginFormWindowClosing(evt);
            }
        });
        loginForm.getContentPane().setLayout(null);

        txt_Username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_UsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_UsernameKeyTyped(evt);
            }
        });
        loginForm.getContentPane().add(txt_Username);
        txt_Username.setBounds(80, 160, 250, 30);

        label_Username_Login.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        label_Username_Login.setForeground(new java.awt.Color(255, 255, 0));
        label_Username_Login.setText("Username");
        loginForm.getContentPane().add(label_Username_Login);
        label_Username_Login.setBounds(80, 130, 110, 20);

        label_Message.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        label_Message.setForeground(new java.awt.Color(255, 255, 0));
        label_Message.setText("Connecting server.....");
        loginForm.getContentPane().add(label_Message);
        label_Message.setBounds(140, 200, 200, 14);

        button_Login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/login_button.png"))); // NOI18N
        button_Login.setBorderPainted(false);
        button_Login.setContentAreaFilled(false);
        button_Login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_LoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_LoginMouseExited(evt);
            }
        });
        button_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_LoginActionPerformed(evt);
            }
        });
        loginForm.getContentPane().add(button_Login);
        button_Login.setBounds(130, 230, 140, 50);

        label_Background_Login.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/background_info.png"))); // NOI18N
        loginForm.getContentPane().add(label_Background_Login);
        label_Background_Login.setBounds(0, 0, 400, 400);

        infoForm.setTitle("Infor of user");
        infoForm.setMinimumSize(new java.awt.Dimension(400, 400));
        infoForm.setResizable(false);
        infoForm.getContentPane().setLayout(null);

        jLabel1.setForeground(new java.awt.Color(255, 255, 0));
        jLabel1.setText("__________");
        infoForm.getContentPane().add(jLabel1);
        jLabel1.setBounds(140, 120, 80, 14);

        label_Username_Info.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        label_Username_Info.setForeground(new java.awt.Color(255, 255, 0));
        label_Username_Info.setText("Username");
        infoForm.getContentPane().add(label_Username_Info);
        label_Username_Info.setBounds(140, 100, 160, 30);

        label_WinMatches.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_WinMatches.setForeground(new java.awt.Color(255, 255, 0));
        label_WinMatches.setText("Win matches: number");
        infoForm.getContentPane().add(label_WinMatches);
        label_WinMatches.setBounds(140, 160, 140, 15);

        label_AllMatches.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_AllMatches.setForeground(new java.awt.Color(255, 255, 0));
        label_AllMatches.setText("All matches: number");
        infoForm.getContentPane().add(label_AllMatches);
        label_AllMatches.setBounds(140, 190, 130, 15);

        button_Done.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/done_button.png"))); // NOI18N
        button_Done.setBorderPainted(false);
        button_Done.setContentAreaFilled(false);
        button_Done.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_DoneMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_DoneMouseExited(evt);
            }
        });
        button_Done.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_DoneActionPerformed(evt);
            }
        });
        infoForm.getContentPane().add(button_Done);
        button_Done.setBounds(140, 240, 100, 40);

        label_Background_Info.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/background_info.png"))); // NOI18N
        infoForm.getContentPane().add(label_Background_Info);
        label_Background_Info.setBounds(0, -6, 400, 410);

        playForm.setTitle("Play");
        playForm.setMinimumSize(new java.awt.Dimension(700, 700));
        playForm.setResizable(false);
        playForm.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                playFormWindowClosing(evt);
            }
        });
        playForm.getContentPane().setLayout(null);

        label_sttPlay.setFont(new java.awt.Font("Tahoma", 2, 80)); // NOI18N
        label_sttPlay.setForeground(new java.awt.Color(255, 255, 0));
        playForm.getContentPane().add(label_sttPlay);
        label_sttPlay.setBounds(570, 160, 60, 100);

        label_Introdution.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_Introdution.setForeground(new java.awt.Color(255, 255, 0));
        label_Introdution.setText("Click \"Accept\" to start game");
        playForm.getContentPane().add(label_Introdution);
        label_Introdution.setBounds(480, 470, 210, 17);

        label_Card1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card1);
        label_Card1.setBounds(520, 510, 110, 140);

        label_Card2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card2);
        label_Card2.setBounds(480, 510, 150, 140);

        label_Card3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card3);
        label_Card3.setBounds(440, 510, 150, 140);

        label_Card4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card4);
        label_Card4.setBounds(400, 510, 150, 140);

        label_Card5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card5);
        label_Card5.setBounds(360, 510, 150, 140);

        label_Card6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card6);
        label_Card6.setBounds(320, 510, 150, 140);

        label_Card7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card7);
        label_Card7.setBounds(280, 510, 150, 140);

        label_Card8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card8);
        label_Card8.setBounds(240, 510, 150, 140);

        label_Card9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card9);
        label_Card9.setBounds(200, 510, 150, 140);

        label_Card10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card10);
        label_Card10.setBounds(160, 510, 150, 140);

        label_Card11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card11);
        label_Card11.setBounds(120, 510, 150, 140);

        label_Card12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card12);
        label_Card12.setBounds(80, 510, 150, 140);

        label_Card13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/00.png"))); // NOI18N
        playForm.getContentPane().add(label_Card13);
        label_Card13.setBounds(50, 510, 150, 140);

        label_Card14.setMaximumSize(new java.awt.Dimension(100, 140));
        label_Card14.setMinimumSize(new java.awt.Dimension(100, 140));
        label_Card14.setPreferredSize(new java.awt.Dimension(100, 140));
        playForm.getContentPane().add(label_Card14);
        label_Card14.setBounds(50, 250, 100, 140);

        label_Card15.setMaximumSize(new java.awt.Dimension(100, 140));
        playForm.getContentPane().add(label_Card15);
        label_Card15.setBounds(160, 250, 100, 140);
        playForm.getContentPane().add(label_Card16);
        label_Card16.setBounds(270, 250, 100, 140);
        playForm.getContentPane().add(label_Card17);
        label_Card17.setBounds(390, 250, 100, 140);

        label_belowline.setForeground(new java.awt.Color(255, 255, 0));
        label_belowline.setText("_________");
        playForm.getContentPane().add(label_belowline);
        label_belowline.setBounds(40, 140, 90, 20);

        label_aboveline.setForeground(new java.awt.Color(255, 255, 0));
        label_aboveline.setText("_________");
        playForm.getContentPane().add(label_aboveline);
        label_aboveline.setBounds(40, 100, 90, 20);

        label_ScorePlayer0.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        label_ScorePlayer0.setForeground(new java.awt.Color(255, 255, 0));
        label_ScorePlayer0.setText("SCORE: 0");
        playForm.getContentPane().add(label_ScorePlayer0);
        label_ScorePlayer0.setBounds(40, 120, 110, 30);

        label_ScorePlayer1.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        label_ScorePlayer1.setForeground(new java.awt.Color(255, 255, 0));
        label_ScorePlayer1.setText("SCORE: 0");
        playForm.getContentPane().add(label_ScorePlayer1);
        label_ScorePlayer1.setBounds(180, 120, 110, 30);

        label_ScorePlayer2.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        label_ScorePlayer2.setForeground(new java.awt.Color(255, 255, 0));
        label_ScorePlayer2.setText("SCORE: 0");
        playForm.getContentPane().add(label_ScorePlayer2);
        label_ScorePlayer2.setBounds(310, 120, 110, 30);

        label_ScorePlayer3.setFont(new java.awt.Font("Tahoma", 3, 12)); // NOI18N
        label_ScorePlayer3.setForeground(new java.awt.Color(255, 255, 0));
        label_ScorePlayer3.setText("SCORE: 0");
        playForm.getContentPane().add(label_ScorePlayer3);
        label_ScorePlayer3.setBounds(440, 120, 130, 30);

        button_Accept_Next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/accept_button.png"))); // NOI18N
        button_Accept_Next.setBorderPainted(false);
        button_Accept_Next.setContentAreaFilled(false);
        button_Accept_Next.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_Accept_NextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_Accept_NextMouseExited(evt);
            }
        });
        button_Accept_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_Accept_NextActionPerformed(evt);
            }
        });
        playForm.getContentPane().add(button_Accept_Next);
        button_Accept_Next.setBounds(530, 300, 140, 60);

        button_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exit_button.png"))); // NOI18N
        button_Exit.setBorderPainted(false);
        button_Exit.setContentAreaFilled(false);
        button_Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_ExitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_ExitMouseExited(evt);
            }
        });
        button_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ExitActionPerformed(evt);
            }
        });
        playForm.getContentPane().add(button_Exit);
        button_Exit.setBounds(530, 370, 140, 60);

        label_Background_Play.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/background_playgame.jpg"))); // NOI18N
        playForm.getContentPane().add(label_Background_Play);
        label_Background_Play.setBounds(0, 0, 700, 700);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("My heart game");
        setPreferredSize(new java.awt.Dimension(700, 700));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(null);

        button_Start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/start_button.png"))); // NOI18N
        button_Start.setBorderPainted(false);
        button_Start.setContentAreaFilled(false);
        button_Start.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button_StartMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button_StartMouseExited(evt);
            }
        });
        button_Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_StartActionPerformed(evt);
            }
        });
        getContentPane().add(button_Start);
        button_Start.setBounds(290, 550, 150, 70);

        label_Background_Startgame.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/background_startgame.jpg"))); // NOI18N
        getContentPane().add(label_Background_Startgame);
        label_Background_Startgame.setBounds(0, 0, 700, 700);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button_StartMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_StartMouseEntered
        this.button_Start.setIcon(new ImageIcon(getClass().getResource("/Images/start_button_hover.png")));
    }//GEN-LAST:event_button_StartMouseEntered

    private void button_StartMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_StartMouseExited
        this.button_Start.setIcon(new ImageIcon(getClass().getResource("/Images/start_button.png")));
    }//GEN-LAST:event_button_StartMouseExited

    private void button_StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_StartActionPerformed
        this.choosePlayingTypeForm.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_button_StartActionPerformed

    
    
    
    private void button_LoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_LoginMouseEntered
       GUI_StartGameScreen.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button_hover.png")));
    }//GEN-LAST:event_button_LoginMouseEntered

    private void button_LoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_LoginMouseExited
        GUI_StartGameScreen.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button.png")));
    }//GEN-LAST:event_button_LoginMouseExited

    private void button_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LoginActionPerformed
        login();
    }//GEN-LAST:event_button_LoginActionPerformed

    
    
    
    private void button_Accept_NextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseEntered
        if (GUI_StartGameScreen.isAccept == 0 || (GUI_StartGameScreen.isAccept == 1 && isPlayNPC == 1))
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button_hover.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button_hover.png")));
    }//GEN-LAST:event_button_Accept_NextMouseEntered

    private void button_Accept_NextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseExited
        if (GUI_StartGameScreen.isAccept == 0 || (GUI_StartGameScreen.isAccept == 1 && isPlayNPC == 1))
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button.png")));
    }//GEN-LAST:event_button_Accept_NextMouseExited

    private void button_Accept_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_Accept_NextActionPerformed
        if (isPlayNPC == 0)
            accept_next();
        else if (isPlayNPC == 1)
            accept_next_withNPC();
    }//GEN-LAST:event_button_Accept_NextActionPerformed

    
    
    
    
    private void button_ExitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_ExitMouseEntered
        this.button_Exit.setIcon(new ImageIcon(getClass().getResource("/Images/exit_button_hover.png")));
    }//GEN-LAST:event_button_ExitMouseEntered

    private void button_ExitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_ExitMouseExited
        this.button_Exit.setIcon(new ImageIcon(getClass().getResource("/Images/exit_button.png")));
    }//GEN-LAST:event_button_ExitMouseExited

    
    
    
    
    
    private void button_DoneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_DoneMouseEntered
        this.button_Done.setIcon(new ImageIcon(getClass().getResource("/Images/done_button_hover.png")));
    }//GEN-LAST:event_button_DoneMouseEntered

    private void button_DoneMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_DoneMouseExited
        this.button_Done.setIcon(new ImageIcon(getClass().getResource("/Images/done_button.png")));
    }//GEN-LAST:event_button_DoneMouseExited

    private void button_DoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_DoneActionPerformed
        this.infoForm.hide();
    }//GEN-LAST:event_button_DoneActionPerformed

    
 
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.out.println("Exit-" + solve.getUser().getUserName());
        //GUI_StartGameScreen.server.send("Exit-" + solve.getUser().getUserName());
    }//GEN-LAST:event_formWindowClosed

    private void txt_UsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_UsernameKeyPressed
       
        if (evt.getKeyCode() == 10)
            login();
    }//GEN-LAST:event_txt_UsernameKeyPressed

    private void txt_UsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_UsernameKeyTyped
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9') && ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) && (c != KeyEvent.VK_BACK_SPACE)) {
            evt.consume(); // consume non-numbers
        }
    }//GEN-LAST:event_txt_UsernameKeyTyped

    
    
    private void button_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ExitActionPerformed
        System.out.println("Exit-" + solve.getUser().getUserName());
        if (isPlayNPC == 0) {
             GUI_StartGameScreen.server.send("Exit-" + solve.getUser().getUserName());
             GUI_StartGameScreen.server.close();
        }
        playForm.dispose();
    }//GEN-LAST:event_button_ExitActionPerformed

    private void loginFormWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_loginFormWindowClosing
        System.out.println("Exit-" + solve.getUser().getUserName());
        try {
            GUI_StartGameScreen.server.send("Exit-" + solve.getUser().getUserName());
            GUI_StartGameScreen.server.close();
        }
        catch (Exception ex){}
    }//GEN-LAST:event_loginFormWindowClosing

    private void playFormWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_playFormWindowClosing
        System.out.println("Exit-" + solve.getUser().getUserName());
        if (isPlayNPC == 0) {
            GUI_StartGameScreen.server.send("Exit-" + solve.getUser().getUserName());
            //GUI_StartGameScreen.server.close();
        }
    }//GEN-LAST:event_playFormWindowClosing

    
    
    
    private void button_OnlineMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OnlineMouseEntered
        this.button_Online.setIcon(new ImageIcon(getClass().getResource("/Images/online_button_hover.png")));
    }//GEN-LAST:event_button_OnlineMouseEntered

    private void button_OnlineMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_OnlineMouseExited
        this.button_Online.setIcon(new ImageIcon(getClass().getResource("/Images/online_button.png")));
    }//GEN-LAST:event_button_OnlineMouseExited

    private void button_OnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_OnlineActionPerformed
        GUI_StartGameScreen.loginForm.setVisible(true);
        GUI_StartGameScreen.choosePlayingTypeForm.setVisible(false);

        new Thread(() -> {
            synchronized(GUI_StartGameScreen.this) {
                server = Server.connect("localhost", 1996);
                dataFromServer = server.receive();
                step = new DataReceivedAnalysis().resultAfterAnalysis(dataFromServer);
                System.out.println(dataFromServer);

                // Khi ket noi thanh cong ta hien ta man hinh login cho user nhap ten, con dang ket noi ta se an di

                playOnline();

                txt_Username.setVisible(true); button_Login.setVisible(true);
                label_Username_Login.setVisible(true); label_Message.setVisible(false);
            }
        }).start();
    }//GEN-LAST:event_button_OnlineActionPerformed

    
    private void button_NPCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_NPCMouseEntered
       this.button_NPC.setIcon(new ImageIcon(getClass().getResource("/Images/npc_button_hover.png")));
    }//GEN-LAST:event_button_NPCMouseEntered

    private void button_NPCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_NPCMouseExited
       this.button_NPC.setIcon(new ImageIcon(getClass().getResource("/Images/npc_button.png")));
    }//GEN-LAST:event_button_NPCMouseExited

    private void button_NPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_NPCActionPerformed
        GUI_StartGameScreen.playForm.setVisible(true);
        GUI_StartGameScreen.choosePlayingTypeForm.setVisible(false);
        GUI_StartGameScreen.npcManager = new NPCManager();
        GUI_StartGameScreen.isPlayNPC = 1;
        
        playOffline();
        accept_next_withNPC();
    }//GEN-LAST:event_button_NPCActionPerformed

    
    
    
  
    
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_StartGameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
      
        java.awt.EventQueue.invokeLater(() -> {
            new GUI_StartGameScreen().setVisible(true);
        });
        
}
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_Accept_Next;
    private javax.swing.JButton button_Done;
    private javax.swing.JButton button_Exit;
    private static javax.swing.JButton button_Login;
    private javax.swing.JButton button_NPC;
    private javax.swing.JButton button_Online;
    private javax.swing.JButton button_Start;
    private static javax.swing.JFrame choosePlayingTypeForm;
    private static javax.swing.JFrame infoForm;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label_AllMatches;
    private javax.swing.JLabel label_Background_Info;
    private javax.swing.JLabel label_Background_Login;
    private javax.swing.JLabel label_Background_Play;
    private javax.swing.JLabel label_Background_Startgame;
    private javax.swing.JLabel label_Card1;
    private javax.swing.JLabel label_Card10;
    private javax.swing.JLabel label_Card11;
    private javax.swing.JLabel label_Card12;
    private javax.swing.JLabel label_Card13;
    private javax.swing.JLabel label_Card14;
    private javax.swing.JLabel label_Card15;
    private javax.swing.JLabel label_Card16;
    private javax.swing.JLabel label_Card17;
    private javax.swing.JLabel label_Card2;
    private javax.swing.JLabel label_Card3;
    private javax.swing.JLabel label_Card4;
    private javax.swing.JLabel label_Card5;
    private javax.swing.JLabel label_Card6;
    private javax.swing.JLabel label_Card7;
    private javax.swing.JLabel label_Card8;
    private javax.swing.JLabel label_Card9;
    private static javax.swing.JLabel label_Introdution;
    private static javax.swing.JLabel label_Message;
    private static javax.swing.JLabel label_ScorePlayer0;
    private javax.swing.JLabel label_ScorePlayer1;
    private javax.swing.JLabel label_ScorePlayer2;
    private javax.swing.JLabel label_ScorePlayer3;
    private javax.swing.JLabel label_Username_Info;
    private static javax.swing.JLabel label_Username_Login;
    private javax.swing.JLabel label_WinMatches;
    private javax.swing.JLabel label_aboveline;
    private javax.swing.JLabel label_background_ChoosePlayingType;
    private javax.swing.JLabel label_belowline;
    private static javax.swing.JLabel label_sttPlay;
    private static javax.swing.JFrame loginForm;
    private static javax.swing.JFrame playForm;
    private static javax.swing.JTextField txt_Username;
    // End of variables declaration//GEN-END:variables
}
