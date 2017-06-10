package game.server;

import game.server.database.DataConnection;
import game.server.entity.User;

import java.util.ArrayList;
import java.util.List;

class UserManager{
    private final List<User> users;
    private int strIndex;
    
    public UserManager(){
        users = new ArrayList<>();
        strIndex = 0;
    }

    public int getStrIndex() {
        return strIndex;
    }

    public void setStrIndex(int strIndex) {
        if (strIndex != -1)
            this.strIndex = strIndex;
    }
    
    public User get(int index){
        return this.users.get(index);
    }
    
    public int size(){
        return users.size();
    }
    
    
    public void add(User user){
        users.add(user);
    }
    
    private Boolean isDuplicateRegisterUserName(String userName) {
        for (int i = 0; i < this.users.size(); ++i)
            if (userName.equals(this.users.get(i).getUserName()))
                return true;
        return false;
    } // Kiem tra user co bi trung khong
    
    public String login(String userName){
        DataConnection con = new DataConnection();
        User user = con.getUserIfHaving(userName);
        String dataSendClient = "";

        if (user.isEmptyUser()) {
            if (con.insert(userName, 0, 0) != false) {} // tao user moi nhung user do da co nguoi ta
            else
                return "Duplicate username";
            user = new User(userName, 0, 0, 0);
        }
        else if (isDuplicateRegisterUserName(userName)) // dang nhap user da co nhung da co nguoi lay xai truoc
            return "Duplicate username";

        dataSendClient =  userName + " " + user.getMatches() + " " + user.getWinMatches() + "-should click 'ACCEPT' to start game" ;

        this.users.add(user);
        con.freeConnection();
        return dataSendClient;
    }
    
    public void sortSTTPlay(int startIndex) {
        if (startIndex != -1) {
            for (int i = 0; i != size();) {
                if (startIndex == size())
                    startIndex = 0;
                try {
                    users.get(startIndex++).setSttPlay(i++);
                }
                catch (Exception ex) {break;}
            }
        }
    }
    
    
}
