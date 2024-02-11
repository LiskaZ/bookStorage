package bookstore.db;


import bookstore.MainProgram;
import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;
import org.apache.poi.ss.formula.functions.T;

import javax.swing.*;
import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

public class BookDAO extends AbstractDAO<Book> implements IDAO<Book> {

    public BookDAO() {
        super(new Book());
    }

    public Vector<Book> findBooksByKeyword(String keyword) {

        Vector<Book> searchedBooks = new Vector<>();
        DBConnection c = MainProgram.getDBConnection();
        String sql = "SELECT * FROM books " +
                "LEFT JOIN books_keywords on books_keywords.fkbooks = books.id " +
                "LEFT JOIN keywords on keywords.id = books_keywords.fkkeywords " +
                "WHERE keywords.keyword = \"" + keyword + "\"";
        if (!keyword.isEmpty()) {
            try {
                ResultSet res = c.selectQuery(sql);
                while (res.next()) {
                    Book instance = new Book();
                    if(instance != null && readFromResultSet(res, instance, true))
                    {
                        searchedBooks.add(instance);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return searchedBooks;
    }

    @Override
    public boolean store(Book obj)
    {
        super.store(obj);
        boolean result = true;

        Iterator<Keyword> iter = obj.getKeywords().iterator();

        while (result && iter.hasNext())
        {
            DBConnection c = MainProgram.getDBConnection();
            Keyword kw = iter.next();
            String sql = "INSERT INTO books_keywords VALUES(" + obj.getID() + ", " + kw.getID() + ");";
            result = c.query(sql);
        }

        return result;
    }

}
