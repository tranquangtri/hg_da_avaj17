package game.client.core;

import game.client.entity.Card;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class MyThread {
    private Thread thr;
    
}


class Command_Hello implements ICommand{
    final private String message;
    public Command_Hello(String message){
        this.message = message;
    }
    @Override
    public void excuteConsoleMode() {
        System.out.println("Server: " + message );
    }

    @Override
    public void excuteGuiMode() {

    }
}


public class GUI_StartGameScreen extends javax.swing.JFrame {

    private static String dataFromServer; 
    private static Server server;
    private static Solve solve;
    private static int step;
    private static Commands commands;
    private static int isAccept;
    
    private static ArrayList<JLabel> cardsPlayingScreen = null;
    private static ArrayList<JLabel> cardsPlayedScreen = null;
    
    private int g = 0;
   
    public GUI_StartGameScreen() {
        initComponents();
        
        this.settingPlayedCard();
        this.settingPlayingCard();
        
        // Set thong tin ban dau cho giao dien ./.
        
        GUI_StartGameScreen.isAccept = 0;
        
        GUI_StartGameScreen.txt_Username.setVisible(false);
        GUI_StartGameScreen.label_Username_Login.setVisible(false);
        GUI_StartGameScreen.button_Login.setVisible(false);
        
        // Set thong tin ban dau cho xu li
        
        GUI_StartGameScreen.solve = new Solve();
        GUI_StartGameScreen.dataFromServer = "";
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////           CAC HAM VIET THEM DE HO TRO XU LI SU KIEN           ////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    private void settingPlayingCard() {
        cardsPlayingScreen = new ArrayList<>();
        
        cardsPlayingScreen.add(label_Card1);
        cardsPlayingScreen.add(label_Card2);cardsPlayingScreen.add(label_Card3);cardsPlayingScreen.add(label_Card4);
        cardsPlayingScreen.add(label_Card5);cardsPlayingScreen.add(label_Card6);cardsPlayingScreen.add(label_Card7);
        cardsPlayingScreen.add(label_Card8);cardsPlayingScreen.add(label_Card9);cardsPlayingScreen.add(label_Card10);
        cardsPlayingScreen.add(label_Card11);cardsPlayingScreen.add(label_Card12);cardsPlayingScreen.add(label_Card13);
       
        
        for (int i = 0; i < cardsPlayingScreen.size(); ++i) {
            cardsPlayingScreen.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    
                    if (step == 2 && solve.getCardExchange().getCards().size() != 3) {
                        int index = findingIndexLabelInArray((JLabel)e.getSource());
                        Card card = solve.getCard(index);
                        solve.getCardExchange().getCards().add(card);
                        cardsPlayedScreen.get(g++).setIcon(new ImageIcon(getClass().getResource(imagePath(card.getValue(), card.getType()))));

                        //sap xep lai bai
                        sortCard(index);
                        solve.sortCard(index);
                    }
                }

            });
        }
    }
    
    private void settingPlayedCard() {
        cardsPlayedScreen = new ArrayList<>();
        cardsPlayedScreen.add(label_Card14);cardsPlayedScreen.add(label_Card15);
        cardsPlayedScreen.add(label_Card16);cardsPlayedScreen.add(label_Card17);
        
        
    }
    
    private static void receiveDataAndAnalysis() { // Nhan du lieu tu server va phan tich xem yeu cau cua server la gi
        dataFromServer = server.receive();
        commands.excute(new Command_Hello(dataFromServer));
        step = new DataReceivedAnalysis().resultAfterAnalysis(dataFromServer);
        System.out.println(step);
    }
    
    private String imagePath(int value, int type) {
        return  "/Images/" + value + type + ".png";
    }

    private void receiveCardAndShowScreen() {
        for (int i = 0; i < solve.getCards().getCards().size(); ++i) {
            String imagePath = this.imagePath(solve.getCards().getCards().get(i).getValue(), 
                                              solve.getCards().getCards().get(i).getType());
            cardsPlayingScreen.get(i).setIcon(new ImageIcon(getClass().getResource(imagePath)));
        }
    }
    
    private int findingIndexLabelInArray(JLabel label) {
        for (int i = 0; i < cardsPlayingScreen.size(); ++i)
            if (cardsPlayingScreen.get(i).equals(label))
                return i;
        return -1;
    }
    
    private void sortCard(int index) {
        int N = cardsPlayingScreen.size() - 1;
        for (int i = index; i < N; ++i)
            cardsPlayingScreen.get(i).setIcon(cardsPlayingScreen.get(i + 1).getIcon());
        cardsPlayingScreen.get(N).setIcon(null);
        cardsPlayingScreen.get(N).setEnabled(false);
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginForm = new javax.swing.JFrame();
        txt_Username = new javax.swing.JTextField();
        label_Username_Login = new javax.swing.JLabel();
        label_Message = new javax.swing.JLabel();
        button_Login = new javax.swing.JButton();
        label_Background_Login = new javax.swing.JLabel();
        infoForm = new javax.swing.JFrame();
        label_Username_Info = new javax.swing.JLabel();
        label_WinMatches = new javax.swing.JLabel();
        label_AllMatches = new javax.swing.JLabel();
        button_Done = new javax.swing.JButton();
        label_Background_Info = new javax.swing.JLabel();
        playForm = new javax.swing.JFrame();
        label_sttPlay = new javax.swing.JLabel();
        label_Introdution = new javax.swing.JLabel();
        label_Score = new javax.swing.JLabel();
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
        button_Accept_Next = new javax.swing.JButton();
        button_Exit = new javax.swing.JButton();
        label_Background_Play = new javax.swing.JLabel();
        button_Start = new javax.swing.JButton();
        label_Background_Startgame = new javax.swing.JLabel();

        loginForm.setTitle("Login");
        loginForm.setMinimumSize(new java.awt.Dimension(400, 400));
        loginForm.getContentPane().setLayout(null);
        loginForm.getContentPane().add(txt_Username);
        txt_Username.setBounds(80, 160, 250, 30);

        label_Username_Login.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        label_Username_Login.setForeground(new java.awt.Color(255, 255, 0));
        label_Username_Login.setText("Username");
        loginForm.getContentPane().add(label_Username_Login);
        label_Username_Login.setBounds(80, 130, 110, 20);

        label_Message.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_Message.setForeground(new java.awt.Color(255, 255, 0));
        label_Message.setText("Connecting server.....");
        loginForm.getContentPane().add(label_Message);
        label_Message.setBounds(140, 200, 200, 15);

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
        infoForm.getContentPane().setLayout(null);

        label_Username_Info.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        label_Username_Info.setForeground(new java.awt.Color(255, 255, 255));
        label_Username_Info.setText("Username");
        infoForm.getContentPane().add(label_Username_Info);
        label_Username_Info.setBounds(140, 110, 110, 30);

        label_WinMatches.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_WinMatches.setForeground(new java.awt.Color(255, 255, 255));
        label_WinMatches.setText("Win matches: number");
        infoForm.getContentPane().add(label_WinMatches);
        label_WinMatches.setBounds(140, 160, 140, 15);

        label_AllMatches.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_AllMatches.setForeground(new java.awt.Color(255, 255, 255));
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
        button_Done.setBounds(130, 240, 100, 40);

        label_Background_Info.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/background_info.png"))); // NOI18N
        infoForm.getContentPane().add(label_Background_Info);
        label_Background_Info.setBounds(0, -6, 400, 410);

        playForm.setTitle("Play");
        playForm.setMinimumSize(new java.awt.Dimension(700, 700));
        playForm.getContentPane().setLayout(null);

        label_sttPlay.setFont(new java.awt.Font("Tahoma", 2, 80)); // NOI18N
        label_sttPlay.setForeground(new java.awt.Color(255, 255, 0));
        playForm.getContentPane().add(label_sttPlay);
        label_sttPlay.setBounds(590, 170, 60, 90);

        label_Introdution.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_Introdution.setForeground(new java.awt.Color(255, 255, 0));
        label_Introdution.setText("Click \"Accept\" to start game");
        playForm.getContentPane().add(label_Introdution);
        label_Introdution.setBounds(480, 470, 210, 17);

        label_Score.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        label_Score.setForeground(new java.awt.Color(255, 255, 0));
        label_Score.setText("SCORE: 0");
        playForm.getContentPane().add(label_Score);
        label_Score.setBounds(50, 110, 150, 30);

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
        button_Accept_Next.setBounds(530, 310, 140, 60);

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
        playForm.getContentPane().add(button_Exit);
        button_Exit.setBounds(530, 380, 140, 60);

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
        this.loginForm.setVisible(true);
        this.hide();
    }//GEN-LAST:event_button_StartActionPerformed

    
    
    
    private void button_LoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_LoginMouseEntered
       GUI_StartGameScreen.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button_hover.png")));
    }//GEN-LAST:event_button_LoginMouseEntered

    private void button_LoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_LoginMouseExited
        GUI_StartGameScreen.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button.png")));
    }//GEN-LAST:event_button_LoginMouseExited

    private void button_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LoginActionPerformed
        
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (GUI_StartGameScreen.txt_Username.getText().equals("")) {
                    GUI_StartGameScreen.label_Message.setText("Please fill usernam. Try new name");
                    return;
                }
                
                GUI_StartGameScreen.server.send("Username-" + GUI_StartGameScreen.txt_Username.getText());
                GUI_StartGameScreen.receiveDataAndAnalysis(); // Nhan du lieu tu server va phan tich xem do la thong tin gi (tra ra bien toan cuc step)

                if (GUI_StartGameScreen.step == -2) {
                    GUI_StartGameScreen.label_Message.setText("Username is duplicate. Try new name");
                    GUI_StartGameScreen.label_Message.setVisible(true);
                }
                else if (GUI_StartGameScreen.step == 0) {

                 // Set gia tri cho man hinh info

                    GUI_StartGameScreen.solve.getInforOfUser(dataFromServer);
                    label_Username_Info.setText(GUI_StartGameScreen.solve.getUser().getUserName());
                    label_WinMatches.setText("Win matches: " + GUI_StartGameScreen.solve.getUser().getWinMatches());
                    label_AllMatches.setText("All matches: " + GUI_StartGameScreen.solve.getUser().getMatches());

                    // Hien thi man hinh ke tiep va tat man hinh cu

                    playForm.setVisible(true);
                    infoForm.setVisible(true);
                    loginForm.hide();
                }
            } 
        };
        thread.start();
    }//GEN-LAST:event_button_LoginActionPerformed

    
    
    
    private void button_Accept_NextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseEntered
        if (GUI_StartGameScreen.isAccept == 0)
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button_hover.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button_hover.png")));
    }//GEN-LAST:event_button_Accept_NextMouseEntered

    private void button_Accept_NextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseExited
        if (GUI_StartGameScreen.isAccept == 0)
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button.png")));
    }//GEN-LAST:event_button_Accept_NextMouseExited

    private void button_Accept_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_Accept_NextActionPerformed

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (GUI_StartGameScreen.isAccept == 0) {
                    GUI_StartGameScreen.server.send("Accept"); // gui Accept chap nhan choi game
                    receiveDataAndAnalysis(); 

                    GUI_StartGameScreen.server.send("Devide card"); // Server gui tin la: cho chia bai, client gui tin yeu cau chia bai
                    receiveDataAndAnalysis();

                    GUI_StartGameScreen.solve.receivedCardFromServer(dataFromServer);
                    receiveCardAndShowScreen();

                    if (solve.getIsExchangeCard()) // Neu nhan duoc yeu cau trao doi bai tu server thi chuyen xuong isAccept = 1
                        label_Introdution.setText("Choose 3 cards to exchange");
                    else { // Neu khong nhan duoc yeu cau trao doi bai thi yeu cau server gui thu tu choi 
                        label_Introdution.setText("Waiting to start game");
                        server.send("SttPlay");
                    }
                }
                else if (GUI_StartGameScreen.isAccept == 1) {
                    if (solve.getCardExchange().getCards().size() != 3) {
                        label_Introdution.setText("Choose 3 cards");
                        return;
                    }

                    // set icon cho bai da gui di bang trong
                    for (int i = 0; i < cardsPlayedScreen.size(); ++i)
                        cardsPlayedScreen.get(i).setIcon(null);

                    // send data
                    label_Introdution.setText("Waiting to start game");
                    server.send(solve.exchnageCard());

                    // nhan data
                    receiveDataAndAnalysis();
                    solve.receiveSTTPlayAndExchangeCardIfHaving(dataFromServer);
                    

                    // cap nhat du lieu cho man hinh
                    for (int i = 10; i < 13; ++i) {
                        String imagePath = imagePath(solve.getCard(i).getValue(), solve.getCard(i).getType());
                        cardsPlayingScreen.get(i).setIcon(new ImageIcon(getClass().getResource(imagePath)));
                        cardsPlayingScreen.get(i).setEnabled(true);
                    }

                    label_sttPlay.setText(Integer.toString(solve.getUser().getSttPlay()));
                }

                GUI_StartGameScreen.isAccept += 1;
                button_Exit.setVisible(true);
            }
        };
        thread.start();
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
        this.server.close();
    }//GEN-LAST:event_formWindowClosed

    
  
    
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
        
        commands = new Commands(Commands.Mode.Console);  
        server = Server.connect("localhost", 1996);
        
        dataFromServer = server.receive();
        step = new DataReceivedAnalysis().resultAfterAnalysis(dataFromServer);
        commands.excute(new Command_Hello(dataFromServer));
        
        // Khi ket noi thanh cong ta hien ta man hinh login cho user nhap ten, con dang ket noi ta se an di
        
        txt_Username.setVisible(true); button_Login.setVisible(true);
        label_Username_Login.setVisible(true); label_Message.setVisible(false);
        
        while (true) {
            if (step >= 3) {
                receiveDataAndAnalysis();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_Accept_Next;
    private javax.swing.JButton button_Done;
    private javax.swing.JButton button_Exit;
    private static javax.swing.JButton button_Login;
    private javax.swing.JButton button_Start;
    private javax.swing.JFrame infoForm;
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
    private javax.swing.JLabel label_Introdution;
    private static javax.swing.JLabel label_Message;
    private javax.swing.JLabel label_Score;
    private javax.swing.JLabel label_Username_Info;
    private static javax.swing.JLabel label_Username_Login;
    private javax.swing.JLabel label_WinMatches;
    private javax.swing.JLabel label_sttPlay;
    private javax.swing.JFrame loginForm;
    private javax.swing.JFrame playForm;
    private static javax.swing.JTextField txt_Username;
    // End of variables declaration//GEN-END:variables
}
