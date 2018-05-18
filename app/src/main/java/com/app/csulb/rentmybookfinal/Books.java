package com.app.csulb.rentmybookfinal;

// Attributes of books
public class Books {

    public String title, image, author, isbn;

    public Books(){}

    public Books(String title, String image, String author, String isbn) {
        this.title = title;
        this.image = image;
        this.author = author;
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}