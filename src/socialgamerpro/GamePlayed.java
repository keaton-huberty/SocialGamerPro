/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialgamerpro;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Will
 */
public class GamePlayed {


    
    private int gamesPlayedID;

    private String gameTitle;
    private int year;
    private String genre;
    private int rating;
    private String review;
    private Button btnReview;

    public GamePlayed(int gamesPlayedID, String gameTitle, int year, String genre, int rating, String review) {
        this.gamesPlayedID = gamesPlayedID;
        this.gameTitle = gameTitle;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
        this.review = review;
        this.btnReview = new Button("Read Review");

        btnReview.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                launchReviewWindow();
                System.out.println("REVIEW BUTTON WORKS! BUTTON WORKS!");
                System.out.println("Here is the review: \n" + review);
            }
        });
    }

    /**
     * @return the gameTitle
     */
    public String getGameTitle() {
        return gameTitle;
    }

    /**
     * @param gameTitle the gameTitle to set
     */
    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the review
     */
    public String getReview() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * @return the btnReview
     */
    public Button getBtnReview() {
        return btnReview;
    }

    /**
     * @param btnReview the btnReview to set
     */
    public void setBtnReview(Button btnReview) {
        this.btnReview = btnReview;
    }
    
        /**
     * @return the gamesPlayedID
     */
    public int getGamesPlayedID() {
        return gamesPlayedID;
    }

    /**
     * @param gamesPlayedID the gamesPlayedID to set
     */
    public void setGamesPlayedID(int gamesPlayedID) {
        this.gamesPlayedID = gamesPlayedID;
    }

    public void launchReviewWindow() {

        String comment = "";
        TextArea txtAreaReview = new TextArea();
        txtAreaReview.setPrefSize(200, 50);
        Label reviewText = new Label("Review of " + this.gameTitle + ":\n" + this.review);

        GridPane grid = new GridPane();

        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.getChildren().add(reviewText);

        Button btnAddComment = new Button("Add Comment");
        

        VBox sceneVBox = new VBox();
        sceneVBox.setPadding(new Insets(20, 20, 20, 20));
        sceneVBox.setSpacing(15);
        sceneVBox.getChildren().addAll(grid, txtAreaReview, btnAddComment);
      
        sceneVBox.setAlignment(Pos.TOP_CENTER);

        Stage addGameStage = new Stage();
        addGameStage.setTitle("Review of " + this.gameTitle);

        Scene addGameDashboard = new Scene(sceneVBox);

        addGameStage.setScene(addGameDashboard);
        addGameStage.setMinHeight(300);
        addGameStage.setMinWidth(300);
        //addGameStage.setResizable(false);

        addGameStage.show();

        btnAddComment.setOnAction((ActionEvent e) -> {

        });

    }

}
