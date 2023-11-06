package bookstore.isbn;

public class ISBNBook {

    public ISBNBook(String title, String author, String isbn)
    {
        this.title = title;
        this.author = author;
        this.ISBN = isbn;
    }

    public String toString()
    {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + ISBN;
    }

    public boolean isValid() {
        return !title.isEmpty() || !author.isEmpty();
    }

    public String title;
    public String author;
    public String ISBN;
}
