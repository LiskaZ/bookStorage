package bookstore.dataobjects;

import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBField;

import java.util.Arrays;
import java.util.List;


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

    @DBFKEntity(name = "key1")
    private Keyword key1;

    @DBFKEntity(name = "key2")
    private Keyword key2;

    @DBFKEntity(name = "key3")
    private Keyword key3;

    @DBFKEntity(name = "key4")
    private Keyword key4;

    @DBFKEntity(name = "key5")
    private Keyword key5;

    @DBFKEntity(name = "key6")
    private Keyword key6;

    @DBFKEntity(name = "key7")
    private Keyword key7;

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
        this.key1 = new Keyword();
        this.key2 = new Keyword();
        this.key3 = new Keyword();
        this.key4 = new Keyword();
        this.key5 = new Keyword();
        this.key6 = new Keyword();
        this.key7 = new Keyword();
    }

    public Book(String author, String title, String description, int rating, int type, int language, Keyword key1, Keyword key2, Keyword key3, Keyword key4, Keyword key5, Keyword key6, Keyword key7)
    {
        this.rating = rating;
        this.type = type;
        this.language = language;
        this.author = author;
        this.title = title;
        this.description = description;
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
        this.key5 = key5;
        this.key6 = key6;
        this.key7 = key7;
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

    public Keyword getKey1() {
        return key1;
    }

    public void setKey1(Keyword key1) {
        this.key1 = key1;
    }

    public Keyword getKey2() {
        return key2;
    }

    public void setKey2(Keyword key2) {
        this.key2 = key2;
    }

    public Keyword getKey3() {
        return key3;
    }

    public void setKey3(Keyword key3) {
        this.key3 = key3;
    }

    public Keyword getKey4() {
        return key4;
    }

    public void setKey4(Keyword key4) {
        this.key4 = key4;
    }

    public Keyword getKey5() {
        return key5;
    }

    public void setKey5(Keyword key5) {
        this.key5 = key5;
    }

    public Keyword getKey6() {
        return key6;
    }

    public void setKey6(Keyword key6) {
        this.key6 = key6;
    }

    public Keyword getKey7() {
        return key7;
    }

    public void setKey7(Keyword key7) {
        this.key7 = key7;
    }

    public List<Keyword> getKeywords() {
        return Arrays.asList(getKey2(), getKey2(), getKey3(), getKey4(), getKey5(), getKey6(), getKey7());
    }
}
