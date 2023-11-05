package bookstore;

import bookstore.db.DBConnection;
import bookstore.starrating.StarRating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static bookstore.util.BookStoreComponents.*;

public class BookRegistration implements ActionListener {
    Logger logger = LoggerFactory.getLogger(BookRegistration.class);

    private JTextField tauthor = createTextField();
    private JTextField ttitle  = createTextField();
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
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(0, 1));

        JLabel title = createTitle(text);
        panel.add(title);

        createForm(panel);

        this.sub = createButton("Submit");
        sub.addActionListener(e -> actionPerformed(e));

        this.reset = createButton("Reset");
        reset.addActionListener(e -> cleanForm());

        panel.add(sub);
        panel.add(reset);

        return panel;
    }

    private void createForm(JPanel panel) {
        JLabel author = createLabel("Authors name");
        JLabel title1 = createLabel("Title");
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
    }

    public void actionPerformed(ActionEvent e)
    {
        if (verifyTextContent(tauthor)
            & verifyTextContent(ttitle)
            & verifyTextContent(tdescription)
            & verifyStarIndex(trating)
            & verifyIndex(ttype)
            & verifyIndex(tlanguage)) {

            long id = addBookToDatabase();
            addBookToOverview(id);
            JOptionPane.showMessageDialog(null, String.format("Book %s added to database.", ttitle.getText()));
            cleanForm();

        } else {
            logger.info("Submit action aborted. Please check your form input!");
        }
    }

    private void addBookToOverview(long id) {
        Object [] bookRow = new Object[]{id, tauthor.getText(), ttitle.getText(), ttype.getSelectedIndex(), trating.getStar()};
        bo.addBook(bookRow);
    }

    private long addBookToDatabase() {
        return db.insertBook(tauthor.getText(),
                ttitle.getText(),
                tdescription.getText(),
                tlanguage.getSelectedIndex(),
                ttype.getSelectedIndex(),
                trating.getStar(),
                getKeyID(tkey1),
                getKeyID(tkey2),
                getKeyID(tkey3),
                getKeyID(tkey4),
                getKeyID(tkey5),
                getKeyID(tkey6),
                getKeyID(tkey7));
    }

    private int getKeyID(JTextField tkey) {
        if (!tkey.getText().isEmpty()) {
            return db.insertKeyword(tkey.getText());
        } else {
            return 0;
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
}
