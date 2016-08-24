package com.vector.studynews.entity;

/**
 * Created by zhang on 2016/8/17.
 */
public class Article {
    private int id;
    private String title;
    private String author;
    private String photo;
    private int likenum;
    private int commentId;
    private int commentnum;
    private String source;
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Article(){

    }

    public Article(int id, String title, String author, String photo, int likenum, int commentId, int commentnum, String source, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.photo = photo;
        this.likenum = likenum;
        this.commentId = commentId;
        this.commentnum = commentnum;
        this.source = source;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", photo='" + photo + '\'' +
                ", likenum=" + likenum +
                ", commentId=" + commentId +
                ", commentnum=" + commentnum +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
