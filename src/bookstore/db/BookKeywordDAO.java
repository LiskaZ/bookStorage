package bookstore.db;

import bookstore.dataobjects.Book;
import bookstore.dataobjects.BookKeyword;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

import java.util.Vector;

public class BookKeywordDAO extends AbstractDAO<BookKeyword> implements IDAO<BookKeyword> {

    public BookKeywordDAO() { super(new BookKeyword());}

}
