package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookOverview extends JPanel {

    JTable bookOverwiew;
    DefaultTableModel tableModel;

    public BookOverview() {
        super();

        createBookOverview("Overview");
    }

    public void createBookOverview(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);

        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());


        String[] columnNames = {"ID",
                "Author",
                "Title",
                "Type",
                "Rating",
                "Delete"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // make read only fields except column 0,13,14
                return column == 5 ? true: false;
            }
        };
        this.bookOverwiew = new JTable(tableModel);

        Object [][] books = getBooks();
        for (Object[] book : books) {
            tableModel.addRow(book);
        }
        bookOverwiew.setRowHeight(30);
        bookOverwiew.getColumn("ID").setMaxWidth(25);
        bookOverwiew.getColumn("Rating").setMaxWidth(40);
        bookOverwiew.getColumn("Type").setMaxWidth(35);
        bookOverwiew.getColumn("Delete").setMaxWidth(40);
        bookOverwiew.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        bookOverwiew.getColumn("Delete").setCellEditor(
                new ButtonEditor(new JCheckBox(), this));
        this.add(bookOverwiew.getTableHeader(), CENTER_ALIGNMENT);
        this.add(new JScrollPane(bookOverwiew), CENTER_ALIGNMENT);
    }

    public void addBook(Object[] book) {
        tableModel.addRow(book);
    }

    public void removeBook(int bookRow) {
        DefaultTableModel model = (DefaultTableModel) bookOverwiew.getModel();
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
