/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialgamerpro;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

/**
 *
 * @author Will
 */
public class User {

    private int userID;
    private String userName;
    private String email;
    private String fName;
    private String lName;
    private String bio;
    private Image profilePic = new Image("userPic.png");
    private Dashboard dashboard;
    private Dashboard friendDashboard;
    //private ResultSet userInfo = null;

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the friendDashboard
     */
    public Dashboard getFriendDashboard() {
        return friendDashboard;
    }

    /**
     * @param friendDashboard the friendDashboard to set
     */
    public void setFriendDashboard(Dashboard friendDashboard) {
        this.friendDashboard = friendDashboard;
    }

    /**
     * @return the dashboard
     */
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * @param dashboard the dashboard to set
     */
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    /**
     * @return the profilePic
     */
    public Image getProfilePic() {
        return profilePic;
    }

    /**
     * @param profilePic the profilePic to set
     */
    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * @return the name
     */
    public String getName() {
        return userName;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.userName = name;
    }

    public User(ResultSet userInfo) throws SQLException {
        if (userInfo.next()) {
            this.userID = userInfo.getInt("userID");
            this.userName = userInfo.getString("userName");
            this.fName = userInfo.getString("firstName");
            this.lName = userInfo.getString("lastName");
            this.bio = userInfo.getString("Bio");
        }
        this.dashboard = new Dashboard(getUserID(), userName, fName, lName, bio);
    }

}
