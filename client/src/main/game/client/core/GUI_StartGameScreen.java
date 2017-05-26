package game.client.core;

import game.client.entity.Cards;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.Dimension;


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
    private static Cards cards;
    private static int step;
    private static Commands commands;
    private boolean isAccept;
    
    private ArrayList<JLabel> cardInScreen = null;
   
    public GUI_StartGameScreen() {
        initComponents();
        
        // Set thong tin ban dau cho giao dien ./.
        
        this.isAccept = false;
        
        this.txt_Username.setVisible(false);
        this.label_Username_Login.setVisible(false);
        this.button_Login.setVisible(false);
        
        // Set thong tin ban dau cho xu li
        
        this.solve = new Solve();
        this.cards = null;
        this.dataFromServer = "";
    }
    
    private void generalSolving(String dataInput) {
        server.send(solve.solvingForClient(step, dataInput, dataFromServer, cards));
        dataFromServer = server.receive();
        commands.excute(new Command_Hello(dataFromServer));
        step = DataReceivedAnalysis.resultAfterAnalysis(dataFromServer);
    }

    
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
        label_Card = new javax.swing.JLabel();
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
        label_Message.setBounds(150, 200, 220, 15);

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

        label_Card.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/101.png"))); // NOI18N
        playForm.getContentPane().add(label_Card);
        label_Card.setBounds(80, 514, 100, 140);

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
        button_Accept_Next.setBounds(530, 530, 140, 60);

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
        button_Exit.setBounds(530, 600, 140, 60);

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
       this.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button_hover.png")));
    }//GEN-LAST:event_button_LoginMouseEntered

    private void button_LoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_LoginMouseExited
        this.button_Login.setIcon(new ImageIcon(getClass().getResource("/Images/login_button.png")));
    }//GEN-LAST:event_button_LoginMouseExited

    private void button_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_LoginActionPerformed
        while (true) {
            this.generalSolving(this.txt_Username.getText());
            
            if (this.step == -2) {
                this.label_Message.setText("Username is duplicate. Try new name");
                this.label_Message.setVisible(true);
            }
            else if (this.step == 0) {
                
                // Set gia tri cho man hinh info
                
                this.solve.getInfoOfUserFromServer(dataFromServer);
                this.label_Username_Info.setText(this.solve.getUser().getUserName());
                this.label_WinMatches.setText("Win matches: " + this.solve.getUser().getWinMatches());
                this.label_AllMatches.setText("All matches: " + this.solve.getUser().getMatches());
                
                // Hien thi man hinh ke tiep va tat man hinh cu
                
                this.playForm.setVisible(true);
                this.infoForm.setVisible(true);
                this.loginForm.hide();
                return;
            }
        }
    }//GEN-LAST:event_button_LoginActionPerformed

    
    
    
    private void button_Accept_NextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseEntered
        if (this.isAccept == false)
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button_hover.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button_hover.png")));
    }//GEN-LAST:event_button_Accept_NextMouseEntered

    private void button_Accept_NextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_Accept_NextMouseExited
        if (this.isAccept == false)
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/accept_button.png")));
        else
        this.button_Accept_Next.setIcon(new ImageIcon(getClass().getResource("/Images/next_button.png")));
    }//GEN-LAST:event_button_Accept_NextMouseExited

    private void button_Accept_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_Accept_NextActionPerformed
        this.isAccept = true;
        this.button_Exit.setVisible(true);
        
        this.generalSolving(""); // Khong truyen du lieu
        this.generalSolving("");
        
        this.cardInScreen = new ArrayList<>();
        this.cardInScreen.add(new JLabel());
        this.cardInScreen.get(0).setIcon(new ImageIcon(getClass().getResource("/Images/100.png")));
        this.cardInScreen.get(0).setPreferredSize(new Dimension(100, 140));
        this.playForm.add(this.cardInScreen.get(0));
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_StartGameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_StartGameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_StartGameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_StartGameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_StartGameScreen().setVisible(true);
            }
        });
        
        commands = new Commands(Commands.Mode.Console);  
        server = Server.connect("localhost", 1996);
        
        dataFromServer = server.receive();
        step = DataReceivedAnalysis.resultAfterAnalysis(dataFromServer);
        commands.excute(new Command_Hello(dataFromServer));
        
        // Khi ket noi thanh cong ta hien ta man hinh login cho user nhap ten, con dang ket noi ta se an di
        
        txt_Username.setVisible(true);
        button_Login.setVisible(true);
        label_Username_Login.setVisible(true);
        label_Message.setVisible(false);
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
    private javax.swing.JLabel label_Card;
    private static javax.swing.JLabel label_Message;
    private javax.swing.JLabel label_Username_Info;
    private static javax.swing.JLabel label_Username_Login;
    private javax.swing.JLabel label_WinMatches;
    private javax.swing.JFrame loginForm;
    private javax.swing.JFrame playForm;
    private static javax.swing.JTextField txt_Username;
    // End of variables declaration//GEN-END:variables
}
