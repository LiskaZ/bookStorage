package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookOverview extends JPanel {

    public BookOverview() {
        super();

        createBookOverview("Overview");
    }

    public void createBookOverview(String text) {
        this.setPreferredSize(new Dimension(250, 80));

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);
        title.setSize(100,20);
        this.add(title);

        JList<JLabel> list = new JList<>(getBooks());
        this.add(list);
    }

    private DefaultListModel<JLabel> getBooks() {
        DBConnection db = new DBConnection();

        DefaultListModel<JLabel> listModel = new DefaultListModel<>();
        ResultSet books = db.getBooks();
        try {
            while (books.next()) {
                JLabel title = new JLabel(books.getString("author"));
                title.setHorizontalAlignment(JLabel.CENTER);
                title.setFont(new Font("TimesRoman", Font.BOLD, 20));
                title.setSize(100,20);

                listModel.addElement(title);
                System.out.println(books.getString("author"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listModel;
    }
}
