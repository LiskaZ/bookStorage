package bookstore.db;

import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

import java.util.Vector;

public class KeywordDAO extends AbstractDAO<Keyword> implements IDAO<Keyword> {

    public KeywordDAO() { super(new Keyword());}

    @Override
    public boolean store(Keyword obj) {
        Vector<Keyword> allKeywords = loadAll();
        Keyword keywordFromDB = allKeywords.stream()
                .filter(keyword -> obj.getKeyword().equals(keyword.getKeyword()))
                .findAny()
                .orElse(null);
        if (keywordFromDB != null) {
            obj.setID(keywordFromDB.getID());
            return true;
        } else {
            return super.store(obj);
        }
    };
}
