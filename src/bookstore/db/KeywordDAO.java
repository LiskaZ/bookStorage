package bookstore.db;


import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

public class KeywordDAO extends AbstractDAO<Keyword> implements IDAO<Keyword> {

    public KeywordDAO() { super(new Keyword());}

    @Override
    public boolean store(Keyword obj) {
        //TODO getMethode für Keywords, wenn bereits in DB vorhanden
        return super.store(obj);
    }
}
