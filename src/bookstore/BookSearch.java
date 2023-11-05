package bookstore;

import javax.swing.*;
import java.awt.*;

import static bookstore.util.BookStoreComponents.createTextField;

public class BookSearch extends JPanel {
    private JButton searchBtn = new JButton("Search");
    private JTextField searchable = createTextField();



    public BookSearch() {
        super();

        createBookSearch("Search");
    };

    public void createBookSearch(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);

        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(120,20));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

        searchPanel.add(searchable);
        searchPanel.add(searchBtn);

        JTable result = new JTable();
        JScrollPane scrollPane = new JScrollPane(result);
        searchBtn.addActionListener(e -> System.out.println("SUCHE!"));


        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());
        this.add(searchPanel);
        this.add(scrollPane);
    }

}
