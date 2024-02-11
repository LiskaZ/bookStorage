package bookstore.dataobjects;

import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBFKEntityList;
import bookstore.dataobjects.annotations.DBField;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;


@DBEntity(tableName = "Books")
public class Book extends DBObject{

    @Override
    public String toString()
    {
        return getTitle();
    }

    @DBField(name = "Author")
    private String author;

    @DBField(name = "title")
    private String title;

    @DBField(name = "description")
    private String description;

    @DBField(name = "language")
    private int language;

    @DBField(name = "type")
    private int type;

    @DBField(name = "rating")
    private int rating;

    public Vector<Keyword> getKeywords() {
        return keywords;
    }

    @DBFKEntityList(foreignType = Keyword.class)
    private Vector<Keyword> keywords;

    public String getTitle() {
        return title;
    }

    public Book()
    {
        this.rating = 0;
        this.type = 0;
        this.language = 0;
        this.author = "";
        this.title = "";
        this.description = "";
        this.keywords = new Vector<Keyword>();
    }

    public Book(String author, String title, String description, int rating, int type, int language, Vector<Keyword> keywords)
    {
        this.rating = rating;
        this.type = type;
        this.language = language;
        this.author = author;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
