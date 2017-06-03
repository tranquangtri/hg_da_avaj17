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

import game.server.database.DataConnection;
import game.server.entity.User;

import java.util.ArrayList;
import java.util.List;

class UserManager{
    private List<User> users;
    public UserManager(){
        users = new ArrayList<>();
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
        return dataSendClient;
    }
    public User get(int index){
        return this.users.get(index);
    }
    public int size(){
        return users.size();
    }
    public void sortSTTPlay(int startIndex) {
        if (startIndex != -1) {
            for (int i = 0; i != size();) {
                if (startIndex == size())
                    startIndex = 0;
                try {
                    get(startIndex++).setSttPlay(i++);
                }
                catch (Exception ex) {break;}
            }
        }

        for (int i = 0; i < size(); ++i)
            System.out.println(get(i).getSttPlay());
    }
}
