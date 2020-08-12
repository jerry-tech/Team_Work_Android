package com.teamwork.teamwork;

public class CommentData {

    int comment_id, article_article_id, gif_comment_id;
    String firstName, lastName, userImage, createdOn, comment;

    public CommentData() {

    }

    public CommentData(int comment_id, String comment, int article_article_id, String createdOn, String firstName, String lastName, String userImage) {
        this.comment_id = comment_id;
        this.comment = comment;
        this.article_article_id = article_article_id;
        this.createdOn = createdOn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;

    }

    public CommentData(int gif_comment_id, String comment, String createdOn, String firstName, String lastName, String userImage) {
        this.gif_comment_id = gif_comment_id;
        this.comment = comment;
        this.createdOn = createdOn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImage = userImage;

    }

    public int getGif_comment_id() {
        return gif_comment_id;
    }

    public void setGif_comment_id(int gif_comment_id) {
        this.gif_comment_id = gif_comment_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getArticle_article_id() {
        return article_article_id;
    }

    public void setArticle_article_id(int article_article_id) {
        this.article_article_id = article_article_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
