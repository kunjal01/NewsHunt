package com.example.android.newsappproject;

public class News {

    private String section;
    private String title;
    private String time;
    private String author;
    private String url;


    public News(String section, String title, String time, String author, String url) {
        this.section = section;
        this.title = title;
        this.time = time;
        this.author = author;
        this.url = url;
    }

    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}

