package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static bookstore.util.BookStoreComponents.createTitle;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookOverview extends JPanel {

    private String[] columnNames = { "ID", "Author", "Title", "Type", "Rating", "Delete" };
    DefaultTableModel tableModel= new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5 ? true: false;
        }
    };
    JTable bookOverview = new JTable(tableModel);

    public BookOverview() {
        super();

        createBookOverview("Overview");
    }

    public void createBookOverview(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel title = createTitle(text);

        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());

        getBooksFromDataBase();
        renderBookTable();

        this.add(bookOverview.getTableHeader(), CENTER_ALIGNMENT);
        this.add(new JScrollPane(bookOverview), CENTER_ALIGNMENT);
    }

    private void renderBookTable() {
        bookOverview.setRowHeight(30);
        bookOverview.getColumn("ID").setMaxWidth(25);
        bookOverview.getColumn("Rating").setMaxWidth(40);
        bookOverview.getColumn("Type").setMaxWidth(35);
        bookOverview.getColumn("Delete").setMaxWidth(40);
        bookOverview.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        bookOverview.getColumn("Delete").setCellEditor(
                new ButtonEditor(new JCheckBox(), this));
    }

    private void getBooksFromDataBase() {
        Object [][] books = getBooks();
        for (Object[] book : books) {
            tableModel.addRow(book);
        }
    }

    public void addBook(Object[] book) {
        tableModel.addRow(book);
    }

    public void removeBook(int bookRow) {
        DefaultTableModel model = (DefaultTableModel) bookOverview.getModel();
        model.removeRow(bookRow);
    }

    private Object[][] getBooks() {
        DBConnection db = new DBConnection();
        ArrayList<Object[]> rows = new ArrayList<>();

        ResultSet books = db.getBooks();
        try {
            while (books.next()) {
                Object[] row = {
                        books.getString("id"),
                        books.getString("author"),
                        books.getString("title"),
                        books.getString("type"),
                        books.getString("rating"),
                        books.getString("id")
                };
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        return data;
    }
}
