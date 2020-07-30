package com.teamwork.teamwork;

public class PostData {
    int gif_id, article_id, users_user_id;
    String imageUrl, title, article, lastName, firstName, userImage, flagged,
            dateCreated;

    public PostData() {

    }

    public PostData(int gif_id, int article_id, int users_user_id, String imageUrl, String title, String article, String lastName, String firstName, String userImage, String flagged, String dateCreated) {
        this.gif_id = gif_id;
        this.article_id = article_id;
        this.users_user_id = users_user_id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.article = article;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userImage = userImage;
        this.flagged = flagged;
        this.dateCreated = dateCreated;
    }

    public PostData(int gif_id, int article_id, int users_user_id, String imageUrl, String title, String article, String flagged, String dateCreated) {
        this.gif_id = gif_id;
        this.article_id = article_id;
        this.users_user_id = users_user_id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.article = article;
        this.flagged = flagged;
        this.dateCreated = dateCreated;
    }

    public PostData(int article_id, int users_user_id, String title, String article, String lastName, String firstName, String userImage, String flagged, String dateCreated) {
        this.article_id = article_id;
        this.users_user_id = users_user_id;
        this.title = title;
        this.article = article;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userImage = userImage;
        this.flagged = flagged;
        this.dateCreated = dateCreated;
    }

    public int getGif_id() {
        return gif_id;
    }

    public void setGif_id(int gif_id) {
        this.gif_id = gif_id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getUsers_user_id() {
        return users_user_id;
    }

    public void setUsers_user_id(int users_user_id) {
        this.users_user_id = users_user_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFlagged() {
        return flagged;
    }

    public void setFlagged(String flagged) {
        this.flagged = flagged;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
