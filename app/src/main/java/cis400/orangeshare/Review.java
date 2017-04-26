package cis400.orangeshare;

/**
 * Created by Brian on 4/26/2017.
 */

public class Review {
    String username, title, description, uid;
    Boolean eatAgain;
    double rating;

    public Review() {
        // empty constructor
    }

    public Review(String username, String title, String description, String uid, Boolean eatAgain, double rating) {
        this.username = username;
        this.title = title;
        this.description = description;
        this.uid = uid;
        this.eatAgain = eatAgain;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getEatAgain() {
        return eatAgain;
    }

    public void setEatAgain(Boolean eatAgain) {
        this.eatAgain = eatAgain;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
