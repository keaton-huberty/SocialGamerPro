/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialgamerpro;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 *
 * @author Will
 */
public class GamePlayed {

    private String gameTitle;
    private int year;
    private String genre;
    private int rating;
    private String review;
    private Button btnReview;

    public GamePlayed(String gameTitle, int year, String genre, int rating, String review) {
        this.gameTitle = gameTitle;
        this.year = year;
        this.genre = genre;
        this.rating = rating;
        this.review = review;
        this.btnReview = new Button("Read Review");
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

}
