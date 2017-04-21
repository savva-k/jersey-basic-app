package com.imsavva.jerseybasicsample.beans;

/**
 * Book POJO.
 *
 * @author Savva Kodeikin
 */
public class Book {

    private int id;
    private String author;
    private String title;
    private Integer year;

    public Book() {
    }

    public Book(String author, String title, int year) {
        this.author = author;
        this.title = title;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return String.format("Book [id=%s, author=%s, title=%s, year=%s]", id, author, title, year);
    }
}
