package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookOverview extends JPanel
        implements ListSelectionListener {

    private final DBConnection db;

    private JList list;
    private DefaultListModel listModel;
    private JScrollPane panel;


    public BookOverview() {
        super(new BorderLayout());

        this.db = new DBConnection();
        createBookOverview("Overview");

    }

    public void createBookOverview(String text) {
        this.panel = new JScrollPane();
        panel.setPreferredSize(new Dimension(250, 80));

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);
        title.setSize(100,20);

        panel.add(title);
        getBooks();

        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        panel.add(listScrollPane);
    }

    private void getBooks() {
        listModel = new DefaultListModel();
        ResultSet books = db.getBooks();
        try {
            while (books.next()) {
                listModel.addElement(books.getString("author"));
                System.out.println(books.getString("author"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    public JComponent getPanel() {
        return panel;
    }
}
