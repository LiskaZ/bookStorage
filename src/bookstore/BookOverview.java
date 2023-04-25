package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookOverview extends JPanel {

    public BookOverview() {
        super();

        createBookOverview("Overview");
    }

    public void createBookOverview(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));

        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());



        String[] columnNames = {"ID",
                "Author",
                "Title",
                "Type",
                "Rating"};

        JTable table = new JTable(getBooks(), columnNames);
        table.setRowHeight(30);
        table.getColumn("ID").setMaxWidth(25);
        table.getColumn("Rating").setMaxWidth(40);
        table.getColumn("Type").setMaxWidth(35);

        this.add(table.getTableHeader(), CENTER_ALIGNMENT);
        this.add(table, CENTER_ALIGNMENT);
    }

    private Object[][] getBooks() {
        DBConnection db = new DBConnection();
        ArrayList<String[]> rows = new ArrayList<>();

        ResultSet books = db.getBooks();
        try {
            while (books.next()) {
                String[] row = {books.getString("id"),
                        books.getString("author"),
                        books.getString("title"),
                        books.getString("type"),
                        books.getString("rating")};
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
