package bookstore;

import bookstore.dataobjects.Keyword;
import bookstore.db.BookDAO;
import bookstore.db.KeywordDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

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
        searchBtn.addActionListener(e -> {
            BookDAO bookDAO = new BookDAO();
            System.out.println(bookDAO.findBooksByKeyword(searchable.getText()));
        });
    }

    public void createBookSearch(String text) {
        this.setLayout(new BorderLayout(0,0));
        add(createTitle(text), BorderLayout.PAGE_START);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createSearchPanel(){
        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(120,25));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

        searchPanel.add(searchable);
        searchPanel.add(searchBtn);

        return searchPanel;
    }

    private JPanel createContentPanel(){
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(createSearchPanel(), BorderLayout.PAGE_START);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }
}
