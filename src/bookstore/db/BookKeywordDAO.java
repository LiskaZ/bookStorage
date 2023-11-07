package bookstore.db;

import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

import java.util.Vector;

public class BookKeywordDAO extends AbstractDAO<Keyword> implements IDAO<Keyword> {

    public BookKeywordDAO() { super(new Keyword());}

}
