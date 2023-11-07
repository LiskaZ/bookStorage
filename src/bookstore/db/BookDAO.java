package bookstore.db;


import bookstore.MainProgram;
import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.daobase.AbstractDAO;
import bookstore.db.daobase.IDAO;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}
