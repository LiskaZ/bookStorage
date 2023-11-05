package bookstore.db;


import bookstore.MainProgram;
import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

public class BookDAO extends AbstractDAO<Book> implements IDAO<Book> {

    public BookDAO() {
        super(new Book());
    }

}
