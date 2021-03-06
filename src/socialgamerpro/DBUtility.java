/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialgamerpro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 *
 * @author Will This is the database connection utility
 */
public class DBUtility {

    // creating class variables used to connect to the database.
    // watch this youtube video to add the "connector" - https://www.youtube.com/watch?v=nW13FmTdkjc
    private Connection conn = null;
    private Statement stmt = null;
    private Statement stmtComment = null;
    private ResultSet resultSet = null;
    private ResultSet resultSetComment = null;
    private PreparedStatement ps = null;
    //ArrayList<String> commentList = new ArrayList<String>();

    private ObservableList<GamePlayed> gamesPlayedList = FXCollections.observableArrayList();

    // dbConnect will connect to the database on the local host
    public void dbConnect() throws SQLException {
        // conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Capstone2019", "root", "");
        conn = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/x8PTdSHvqZ", "x8PTdSHvqZ", "Pd67T8sc88");
    }

    public void dbClose() throws SQLException {
        this.conn.close();
    }

    // checkLogin will take the username and password entered as strings
    // I'm not super great at SQL but I got this to work so far.
    public boolean checkLogin(String inputUserName, String inputPassword) throws SQLException {
        String username = null;
        String password = null;

        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        resultSet = stmt.executeQuery("SELECT * FROM userLogin WHERE userName = '" + inputUserName + "'");

        //In order to read what the query returns, we have to use resultSet.next(), otherwise we get an error
        //Here I am assigning the correct password with the username
        if (resultSet.next()) {
            username = resultSet.getString("userName");
            password = resultSet.getString("userPassword");
        } else {
            System.out.println("Username does not exist!");
        }

        //checking to see if the user entered password equals the password stored in the database
        return inputPassword.equals(password);

    }

    public ResultSet getUserInfo(String inputUserName) throws SQLException {

        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        resultSet = stmt.executeQuery("SELECT * FROM userLogin WHERE userName = '" + inputUserName + "'");
        return resultSet;
    }

    public void createNewAccount(String userName, String password, String firstName, String lastName, String email, LocalDate dob, File image, String bio) throws SQLException, FileNotFoundException, IOException {

        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("INSERT INTO `userLogin`(`userName`, `userPassword`, `Email`, `Dob`, `Image`, `firstName`, `lastName`, `Bio`) VALUES ('" + userName + "','" + password + "','" + email + "','" + dob + "','" + image + "','" + firstName + "','" + lastName + "','" + bio + "')");

        // Need to insert the Blob with a Prepared Statement.  This is done after the initial SQL INSERT INTO
        FileInputStream fis = new FileInputStream(image);
        ps = conn.prepareStatement("UPDATE userLogin SET Image = ? WHERE userName = ?");
        ps.setBinaryStream(1, (InputStream) fis, (int) image.length());
        ps.setString(2, userName);
        ps.execute();
        ps.close();

    }

    public Image loadProfilePicture(int userID) throws SQLException, IOException, NullPointerException {
        Image profilePic = new Image("userPic.png");
        String nullCheck = null;
        boolean isEmpty = true;
        stmt = conn.createStatement();
        resultSet = stmt.executeQuery("SELECT Image FROM userLogin WHERE userID = " + userID + "");

        //System.out.println("nullCheck: " + nullCheck.toString());
        if (resultSet.next()) {
            InputStream inputStream = resultSet.getBinaryStream("Image");
            if (inputStream != null) {

                //profilePic = new Image(inputStream);
                OutputStream outputStream = new FileOutputStream(new File("img.jpg"));
                byte[] content = new byte[5000000];
                int size = 0;

                while ((size = inputStream.read(content)) != -1) {
                    outputStream.write(content, 0, size);
                }
                profilePic = new Image("file:img.jpg");

                inputStream.close();
                outputStream.close();
            }
        }

        //outputStream.close();
        //Image profilePic = new Image("file:img.jpg");
//        System.out.println("image file test: " + profilePic);
//        System.out.println("image file test: " + resultSet.getString("Image"));
        return profilePic;
    }

    public void addGame(String gameTitle, int YearPublished, String genre) throws SQLException {

        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("INSERT INTO `GamesLibrary`(`Title`, `YearPublished`, `Genre`) VALUES ('" + gameTitle + "','" + YearPublished + "','" + genre + "')");

    }

    public void addGameToUserList(String gameTitle, int userID, int rating, String review) throws SQLException {
        int gameID = 999999;

        System.out.println("addGameToUserList parameters check: \ngame title: " + gameTitle + "\n userID: " + userID + ""
                + "\n rating: " + rating + "\n review: " + review);

        stmt = conn.createStatement();

        resultSet = stmt.executeQuery("SELECT gameID FROM GamesLibrary WHERE title = '" + gameTitle + "'");
        if (resultSet.next()) {
            gameID = resultSet.getInt("gameID");
        }
//        System.out.println("Game title passed = " + gameTitle);
//        System.out.println("gamesID = " + gameID);
//        System.out.println("userID = " + userID);
        stmt = conn.createStatement();

        stmt.execute("INSERT INTO `GamesPlayed` (`userID`, `gameID`, `rating`, `review`) VALUES ('" + userID + "','" + gameID + "','" + rating + "','" + review + "')");

//        stmt = conn.createStatement();
//        
//        stmt.executeUpdate("INSERT INTO `ReviewRating` (`userID`, `gameID`, `rating`, `reviewText`) VALUES ('" + userID + "','" + gameID + "','" + rating + "','" + review + "')");
    }

    public void editGameToUserList(int gamesPlayedID, int rating, String review) throws SQLException {

        if (review.matches("")) {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE GamesPlayed SET rating='" + rating + "' WHERE gamesPlayedID = '" + gamesPlayedID + "'");
        } else {
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE GamesPlayed SET rating='" + rating + "', review='" + review + "' WHERE gamesPlayedID = '" + gamesPlayedID + "'");
        }
    }

    public void removeGameFromUserList(int gamesPlayedID) throws SQLException {

        stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM GamesPlayed WHERE gamesPlayedID = '" + gamesPlayedID + "'");
    }

    //gets users for friends list, will eventually pull from follower table
    public ResultSet getUsers() throws SQLException {
        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        resultSet = stmt.executeQuery("SELECT userName FROM userLogin");
        return resultSet;
    }

    public ResultSet getFollowers(String userName) throws SQLException {
        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        resultSet = stmt.executeQuery("SELECT followingName FROM Followers WHERE userName = '" + userName + "'");
        return resultSet;
    }

    public ResultSet getGamesLibrary() throws SQLException {

        stmt = conn.createStatement();

        resultSet = stmt.executeQuery("SELECT Title FROM GamesLibrary");

        return resultSet;
    }

    public ObservableList<GamePlayed> getGamesPlayed(int userID) throws SQLException {
        String commentor = "";
        int gamesPlayedID;
        String gameTitle;
        int year;
        String genre;
        int rating;
        String review;
        //ArrayList<String> commentList = new ArrayList<String>();
        stmt = conn.createStatement();

//        resultSet = stmt.executeQuery("SELECT Title, YearPublished, Genre "
//                + "FROM GamesLibrary JOIN GamesPlayed "
//                + "ON GamesLibrary.gameID = GamesPlayed.gameID "
//                + "WHERE GamesPlayed.userID = " + userID + " ORDER BY Title");
        //SAVE
        resultSet = stmt.executeQuery("SELECT GamesLibrary.Title, GamesLibrary.YearPublished, GamesLibrary.Genre, GamesPlayed.gamesPlayedID, GamesPlayed.rating, GamesPlayed.review "
                + "FROM GamesLibrary JOIN GamesPlayed "
                + "ON GamesLibrary.gameID = GamesPlayed.gameID "
                + "WHERE GamesPlayed.userID = " + userID + " ORDER BY Title");

//        resultSet = stmt.executeQuery("SELECT GamesLibrary.Title, GamesLibrary.YearPublished, GamesLibrary.Genre, GamesPlayed.gamesPlayedID, GamesPlayed.rating, GamesPlayed.review, Comments.comment"
//                + "FROM GamesLibrary JOIN GamesPlayed "
//                + "ON GamesLibrary.gameID = GamesPlayed.gameID"
//                + "JOIN Comments on Comments.gameID = GamesLibrary.gameID "
//                + "WHERE GamesPlayed.userID = " + userID + " ORDER BY Title");
//        stmtComment = conn.createStatement();
//        resultSetComment = stmtComment.executeQuery("SELECT comment FROM Comments WHERE gameID = '" + gamesPlayedID + "'");
        //resultSetComment = stmtComment.executeQuery("SELECT Comments.comment, userLogin.userName FROM Comments JOIN userLogin WHERE GamesPlayed.gameID = " + " ");
        while (resultSet.next()) {
            gamesPlayedID = resultSet.getInt("gamesPlayedID");
            gameTitle = resultSet.getString("Title");
            year = resultSet.getInt("YearPublished");
            genre = resultSet.getString("Genre");
            rating = resultSet.getInt("rating");
            review = resultSet.getString("review");
            
            System.out.println("First gameplayedID: " + gamesPlayedID);
            
            ArrayList<String> commentList = new ArrayList<String>();
            
            stmtComment = conn.createStatement();
            resultSetComment = stmtComment.executeQuery("SELECT * FROM Comments WHERE gameID = '" + gamesPlayedID + "'");
            while (resultSetComment.next()) {
                commentList.add(resultSetComment.getString("userName") + " says: " + resultSetComment.getString("comment"));
                System.out.println("comment from resultset: " + resultSetComment.getString("comment"));
                System.out.println("adding comment to arrayList");
                System.out.println("gamePlayedID: " + gamesPlayedID);

            }

            GamePlayed game = new GamePlayed(gamesPlayedID, gameTitle, year, genre, rating, review, commentList, userID);
            
//            System.out.println(game.getGameTitle());
//            System.out.println(game.getYear());
//            System.out.println(game.getGenre());
//            System.out.println("GAME ADDED TO LIST");
            gamesPlayedList.add(game);
            
            
        }
//        for (String commentl : commentList) {
//            System.out.println(" commentList: " + commentl);
//        }
        return gamesPlayedList;
    }

    public void addComment(String comment, int gamePlayedID, int userID, String userName) throws SQLException {
        //System.out.print("addComment pressed no statement yet\n");
        stmtComment = conn.createStatement();
        //System.out.print("addComment statement");

        stmtComment.executeUpdate("INSERT INTO `Comments`(`gameID`, `userID`, `userName`, `comment`) VALUES ('" + gamePlayedID + "', '" + userID + "',  '" + userName + "','" + comment + "')");
        System.out.println("addComment method called");
        System.out.print("addComment method called \n");

    }

    //This method will insert a new entry into the followers table
    public void addFollow(String userName, String followUser, int userID, int followID) throws SQLException {
        stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO `Followers`(`userID`, `userName`, `followingID`, `followingName`) VALUES ('" + userID + "','" + userName + "','" + followID + "','" + followUser + "')");
    }

    public void deleteFollow(String userName, String followUser, int userID, int followID) throws SQLException {
        stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM Followers WHERE userName = '" + userName + "' AND followingName = '" + followUser + "'");
    }

//method for inserting msg into the databse
    public void insertMsg(String sender, String receiver, String msg) throws SQLException {
//        dbConnect();
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("INSERT INTO `messenging`(`msgSender`, `msgReceiver`, `msgContent`) VALUES ('" + sender + "','" + receiver + "','" + msg + "')");

    }

    //method for getting msgs
    public ResultSet getMsg(String receiver, boolean check) throws SQLException {

//        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        if (check) {
            resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "' ORDER by msgID desc limit 3");
        } else {
            resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "'");
        }
        return resultSet;
    }

    public ResultSet getMsgsforSpecificUser(String receiver, String sender, boolean check) throws SQLException {

        stmt = conn.createStatement();
        if (check) {
            if (sender.equalsIgnoreCase("all")) {
                resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "' ORDER by msgID desc limit 3");
            } else {
                resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "' and msgSender = '" + sender + "' ORDER by msgID desc limit 3");
            }
        } else {
            if (sender.equalsIgnoreCase("all")) {
                resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "'");
            } else {
                resultSet = stmt.executeQuery("SELECT * FROM messenging where msgReceiver = '" + receiver + "' and msgSender = '" + sender + "'");
            }
        }
        return resultSet;
    }

    //method for getting msgs
    public void deleteMsgs(String receiver) throws SQLException {

        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("DELETE FROM messenging where msgReceiver = '" + receiver + "'");

    }

    public void updateInfo(String user, String name, String bio) throws SQLException {

        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("UPDATE userLogin SET firstName='" + name + "',lastName='',Bio='" + bio + "' WHERE userName = '" + user + "'");

    }

    public void updateProfilePicture(String userName, File image) throws SQLException, FileNotFoundException, IOException {

        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        // stmt.executeUpdate("UPDATE userLogin SET Image VALUES ('" + image + "') WHERE userName = '" + userName + "'");

        // Need to insert the Blob with a Prepared Statement.  This is done after the initial SQL INSERT INTO
        FileInputStream fis = new FileInputStream(image);
        ps = conn.prepareStatement("UPDATE userLogin SET Image = ? WHERE userName = ?");
        ps.setBinaryStream(1, (InputStream) fis, (int) image.length());
        ps.setString(2, userName);
        ps.execute();
        ps.close();

    }

    public void updateEmail(String user, String password) throws SQLException {

        dbConnect();
        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        stmt.executeUpdate("UPDATE userLogin SET userPassword='" + password + "' WHERE userName = '" + user + "'");

    }

    public boolean checkEmail(String inputUserName, String inputEmail) throws SQLException {
        String username = null;
        String email = null;

        //first have to creat a statement
        stmt = conn.createStatement();
        // this runs the SQL query - notice the extra single quotes around the string.  Don't forget those.
        resultSet = stmt.executeQuery("SELECT * FROM userLogin WHERE userName = '" + inputUserName + "'");

        //In order to read what the query returns, we have to use resultSet.next(), otherwise we get an error
        //Here I am assigning the correct password with the username
        if (resultSet.next()) {
            username = resultSet.getString("userName");
            email = resultSet.getString("Email");
        } else {
            System.out.println("Username/Email does not exist!");
        }

        //checking to see if the user entered password equals the password stored in the database
        return inputEmail.equals(email);

    }
    
    public String getUserName(int userID) throws SQLException{
        
        String userName = "";
        stmt = conn.createStatement();
        resultSet = stmt.executeQuery("SELECT userName FROM userLogin WHERE userID = '" + userID + "'");
        
        while(resultSet.next()){
            userName = resultSet.getString("userName");
        }
 
        return userName;
    }

    // constructor
    public DBUtility() {
    }
;
}
