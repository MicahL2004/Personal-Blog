package com.blog.model;

import java.sql.Timestamp;

/**
 * Model class representing a blog post.
 * Maps directly to the blog_posts table in the database.
 */
public class BlogPost {

    private int id;
    private String title;
    private String content;
    private String author;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public BlogPost() {}

    // Constructor for creating new posts (no ID yet)
    public BlogPost(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    // Full constructor
    public BlogPost(int id, String title, String content, String author,
                    Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    /**
     * Returns a preview of the content (first 200 characters).
     */
    public String getContentPreview() {
        if (content == null) return "";
        if (content.length() <= 200) return content;
        return content.substring(0, 200) + "...";
    }

    @Override
    public String toString() {
        return "BlogPost{id=" + id + ", title='" + title + "', author='" + author + "'}";
    }
}
