package bookstore;

import bookstore.dataobjects.Book;
import bookstore.dataobjects.Keyword;
import bookstore.db.BookDAO;
import bookstore.db.DBConnection;
import bookstore.db.KeywordDAO;
import bookstore.isbn.ISBNBook;
import bookstore.isbn.OpenLibraryJSONQuery;
import bookstore.starrating.StarRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutionException;

import static bookstore.util.BookStoreComponents.*;

public class BookRegistration implements ActionListener {
    Logger logger = LoggerFactory.getLogger(BookRegistration.class);

    private JTextField tauthor = createTextField();
    private JTextField ttitle  = createTextField();
    private JTextField tisbn  = createTextField();
    private JTextField tdescription  = createTextField();
    private JComboBox<String> tlanguage;
    private JComboBox<String> ttype;
    private StarRating trating;
    private JTextField tkey1  = createTextField();
    private JTextField tkey2  = createTextField();
    private JTextField tkey3  = createTextField();
    private JTextField tkey4  = createTextField();
    private JTextField tkey5  = createTextField();
    private JTextField tkey6  = createTextField();
    private JTextField tkey7  = createTextField();
    private JButton sub;
    private JButton reset;

    private JButton isbnQuery;

    private final DBConnection db;
    private final BookOverview bo;

    private final String[] languages
            = { "select language", "german", "english" };
    private final String[] types
            = { "select type", "intimate", "great ones", "better ones", "funny or magic" };

    public BookRegistration(BookOverview bo) {
        this.db = new DBConnection();
        this.bo = bo;
    }

    public JComponent createBookRegistration(String text) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel title = createTitle(text);
        mainPanel.add(title, BorderLayout.PAGE_START);

        mainPanel.add(createForm(), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        this.sub = createButton("Submit");
        sub.addActionListener(e -> actionPerformed(e));

        this.reset = createButton("Reset");
        reset.addActionListener(e -> cleanForm());

        this.isbnQuery = createButton("Load From ISBN");
        isbnQuery.addActionListener(e -> loadDataFromISBN());

        actionPanel.add(sub);
        actionPanel.add(reset);
        actionPanel.add(isbnQuery);

        mainPanel.add(actionPanel, BorderLayout.PAGE_END);

        return mainPanel;
    }

    private JPanel createForm() {

        JPanel panel = new JPanel(false);
        GridLayout layout = new GridLayout(0, 1);
        panel.setLayout(layout);

        JLabel author = createLabel("Authors name");
        JLabel title1 = createLabel("Title");
        JLabel isbn = createLabel("ISBN");
        JLabel description = createLabel("Description");
        JLabel language = createLabel("Language");
        JLabel type = createLabel("Type");
        JLabel rating = createLabel("Rating");
        JLabel key1 = createLabel("Keyword 1");
        JLabel key2 = createLabel("Keyword 2");
        JLabel key3 = createLabel("Keyword 3");
        JLabel key4 = createLabel("Keyword 4");
        JLabel key5 = createLabel("Keyword 5");
        JLabel key6 = createLabel("Keyword 6");
        JLabel key7 = createLabel("Keyword 7");

        this.tlanguage = new JComboBox<>(languages);
        tlanguage.setFont(new Font("Arial", Font.PLAIN, 15));
        tlanguage.setSize(60, 16);
        tlanguage.setLocation(200, 250);

        type.setLocation(100, 300);

        this.ttype = new JComboBox<>(types);
        ttype.setFont(new Font("Arial", Font.PLAIN, 15));
        ttype.setSize(100, 16);
        ttype.setLocation(200, 300);

        rating.setLocation(100, 350);
        this.trating = new StarRating();

        this.tisbn.addKeyListener(new KeyListener() {

            private boolean pressedHere = false;
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressedHere = true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER && pressedHere) {
                    pressedHere = false;
                    loadDataFromISBN(((JTextField)e.getSource()).getText());
                }
            }
        });

        panel.add(isbn);
        panel.add(tisbn);
        panel.add(author);
        panel.add(tauthor);
        panel.add(title1);
        panel.add(ttitle);
        panel.add(description);
        panel.add(tdescription);
        panel.add(language);
        panel.add(tlanguage);
        panel.add(type);
        panel.add(ttype);
        panel.add(rating);
        panel.add(trating);
        panel.add(key1);
        panel.add(tkey1);
        panel.add(key2);
        panel.add(tkey2);
        panel.add(key3);
        panel.add(tkey3);
        panel.add(key4);
        panel.add(tkey4);
        panel.add(key5);
        panel.add(tkey5);
        panel.add(key6);
        panel.add(tkey6);
        panel.add(key7);
        panel.add(tkey7);

        return panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (verifyTextContent(tauthor)
            & verifyTextContent(ttitle)
            & verifyTextContent(tdescription)
            & verifyStarIndex(trating)
            & verifyIndex(ttype)
            & verifyIndex(tlanguage)) {

            Book newBook = addBookToDatabase();
            addBookToOverview(newBook);
            JOptionPane.showMessageDialog(null, String.format("Book %s added to database.", ttitle.getText()));
            cleanForm();

        } else {
            logger.info("Submit action aborted. Please check your form input!");
        }
    }

    private void addBookToOverview(Book book) {
        Object [] bookRow = new Object[]{book.getID(), tauthor.getText(), ttitle.getText(), ttype.getSelectedIndex(), trating.getStar(), book.getID()};
        bo.addBook(bookRow);
    }

    private Book addBookToDatabase() {
        BookDAO bookstore = new BookDAO();
        Book book = new Book(tauthor.getText(),
                ttitle.getText(),
                tdescription.getText(),
                tlanguage.getSelectedIndex(),
                ttype.getSelectedIndex(),
                trating.getStar(),
                getKey(tkey1),
                getKey(tkey2),
                getKey(tkey3),
                getKey(tkey4),
                getKey(tkey5),
                getKey(tkey6),
                getKey(tkey7));
        bookstore.store(book);
        return book;
    }

    private Keyword getKey(JTextField tkey) {
        if (!tkey.getText().isEmpty()) {
            KeywordDAO keywordDAO = new KeywordDAO();
            Keyword keyword = new Keyword(tkey.getText());
            keywordDAO.store(keyword);
            return keyword;
        } else {
            return null;
        }
    }

    private boolean verifyIndex(JComboBox<String> comboBox) {
        if (comboBox.getSelectedIndex() == 0){
            comboBox.setBorder(new LineBorder(Color.red,1));
            return false;
        } else {
            comboBox.setBorder(new LineBorder(Color.black,1));
            return true;
        }
    }

    private boolean verifyStarIndex(StarRating starRating) {
        if (starRating.getStar() == 0) {
            starRating.setBorder(new LineBorder(Color.red, 1));
            return false;

        } else {
            starRating.setBorder(new LineBorder(Color.black,1));
            return true;
        }
    }

    private boolean verifyTextContent(JTextField textField) {
        if (textField.getText().isEmpty()){
            textField.setBorder(new LineBorder(Color.red,1));
            return false;
        } else {
            textField.setBorder(new LineBorder(Color.black,1));
            return true;
        }
    }

    private void cleanForm() {
        String def = "";
        tauthor.setText(def);
        tauthor.setBorder(new LineBorder(Color.black,1));
        ttitle.setText(def);
        ttitle.setBorder(new LineBorder(Color.black,1));
        tdescription.setText(def);
        tdescription.setBorder(new LineBorder(Color.black,1));
        trating.starsReset();
        trating.setBorder(new LineBorder(Color.black,1));
        ttype.setSelectedIndex(0);
        ttype.setBorder(new LineBorder(Color.black,1));
        tlanguage.setSelectedIndex(0);
        tlanguage.setBorder(new LineBorder(Color.black,1));
        tkey1.setText(def);
        tkey2.setText(def);
        tkey3.setText(def);
        tkey4.setText(def);
        tkey5.setText(def);
        tkey6.setText(def);
        tkey7.setText(def);
    }

    private void loadDataFromISBN() {
        loadDataFromISBN(tisbn.getText());
    }

    private void loadDataFromISBN(String isbn) {
        if(isValidISBN(isbn)){
            executeLoadDataInOwnThread(isbn);
        }
        else {
            JOptionPane.showMessageDialog(tisbn,
                    "A valid ISBN consists only of numbers and is either 10 or 13 chars long",
                    "ISBN Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidISBN(String isbn){
        return isbn.matches("[0-9]{10}") || isbn.matches("[0-9]{13}");
    }

    private void executeLoadDataInOwnThread(String isbn)
    {
        SwingWorker w = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                return OpenLibraryJSONQuery.queryBook(isbn);
            }

            protected void done()
            {
                try {
                    evaluateISBNLoadResult((ISBNBook)get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        w.execute();
    }

    private  void evaluateISBNLoadResult(ISBNBook b)
    {
        if(b.isValid()) {
            tauthor.setText(b.author);
            ttitle.setText(b.title);
        }
        else {
            JOptionPane.showMessageDialog(tisbn,
                    String.format("No data for the book with ISBN \"%s\" found.", b.ISBN),
                    "Data Load Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
