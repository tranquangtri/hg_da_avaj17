package game.server.database;

import game.server.entity.User;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.InputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TRANQUANGTRUNG
 */
public class DataConnection {
    private Connection con;
    private String URL;
    private String USER;
    private String PASS;
    
    public void setConnection(Connection con){
        this.con = con;
    }
    public DataConnection() {
        con = null;
        Properties properties = new Properties();
        
        try {
            InputStream inputStream = DataConnection.class.getClass().getResourceAsStream("/config/info.properties");
            properties.load(inputStream);
            URL  = properties.getProperty("url");
            USER = properties.getProperty("user");
            PASS = properties.getProperty("password");
            System.out.println(URL);
            // Dang ky driver
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = (Connection)DriverManager.getConnection(URL, USER, PASS);
        }
        catch (IOException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Connection getConnection() {
        return this.con;
    }
    
    public User getUserIfHaving(String userName) {
        try {
            Statement st = (Statement) con.createStatement();
            String query = "select * from UserPlayer where namePlayer = '" + userName + "';";
            ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                return new User(rs.getString(1), rs.getInt(2), rs.getInt(3), 0);
            }
        }
        catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return new User();
    }
    
    public Boolean insert(String userName, int matches, int winMatches) {
        try {
            Statement st = (Statement) con.createStatement();
            String query = "insert into UserPlayer(namePlayer, allMatches, winMatches) values('" +
                            userName + "', " + Integer.toString(matches) + "," + Integer.toString(winMatches) + ");" ;
            st.executeUpdate(query);
            st.close();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
    
    public Boolean update(String userName, int matches, int winMatches) {
        try {
            Statement st = (Statement) con.createStatement();
            String query = "update UserPlayer set allMatches = " + 
                            Integer.toString(matches) +  ", winMatches = " + Integer.toString(winMatches) +
                            " where namePlayer = '" + userName + "';";
            st.executeUpdate(query);
            st.close();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
    
    public Boolean delete(String userName) {
        try {
            Statement st = (Statement) con.createStatement();
            String query = "delete from UserPlayer where namePlayer = '" + userName + "';";
            st.executeUpdate(query);
            st.close();
            return true;
        }
        catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            return false;
        }
    }
    
    public void freeConnection() {
        try {
            con.close();
        }
        catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

