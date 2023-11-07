package bookstore.dataobjects;

import bookstore.dataobjects.annotations.DBEntity;
import bookstore.dataobjects.annotations.DBFKEntity;
import bookstore.dataobjects.annotations.DBField;

@DBEntity(tableName = "books_keywords")
public class BookKeyword extends DBObject{

    @DBFKEntity(name = "fkkeywords")
    private Keyword keyword;

    @DBFKEntity(name = "fkbooks")
    private Book book;

    public BookKeyword(Book b, Keyword k) {
        this.keyword = k;
        this.book = b;
    }

    public BookKeyword() {
        this.book = new Book();
        this.keyword = new Keyword();
    }
}
