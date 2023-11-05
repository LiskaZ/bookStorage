package bookstore;

import javax.swing.*;
import java.awt.*;

import static bookstore.util.BookStoreComponents.*;

public class BookSearch extends JPanel {
    private JButton searchBtn = new JButton("Search");
    private JTextField searchable = createTextField();
    private JTable result = new JTable();
    private JScrollPane scrollPane = new JScrollPane(result);

    public BookSearch() {
        super();

        createBookSearch("Search books");
        addSearch();
    }

    private void addSearch() {
        searchBtn.addActionListener(e -> System.out.println("SUCHE!"));
    }

    public void createBookSearch(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel title = createTitle(text);

        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(120,20));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

        searchPanel.add(searchable);
        searchPanel.add(searchBtn);

        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());
        this.add(searchPanel);
        this.add(scrollPane);
    }
}
