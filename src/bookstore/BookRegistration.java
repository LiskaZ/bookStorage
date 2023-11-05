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

public class BookRegistration implements ActionListener {
    Logger logger = LoggerFactory.getLogger(BookRegistration.class);


    private JTextField tauthor;
    private JTextField ttitle;
    private JTextField tdescription;
    private JComboBox<String> tlanguage;
    private JComboBox<String> ttype;
    private StarRating trating;
    private JTextField tkey1;
    private JTextField tkey2;
    private JTextField tkey3;
    private JTextField tkey4;
    private JTextField tkey5;
    private JTextField tkey6;
    private JTextField tkey7;
    private JButton sub;
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

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);
        panel.add(title);

        createForm(panel);

        return panel;
    }

    private void createForm(JPanel panel) {
        JLabel author = createLabel("Authors name");
        JLabel title1 = createLabel("Title");
        JLabel description = createLabel("Description");
        JLabel language = createLabel("Language");
        JLabel key1 = createLabel("Keyword 1");
        JLabel key2 = createLabel("Keyword 2");
        JLabel key3 = createLabel("Keyword 3");
        JLabel key4 = createLabel("Keyword 4");
        JLabel key5 = createLabel("Keyword 5");
        JLabel key6 = createLabel("Keyword 6");
        JLabel key7 = createLabel("Keyword 7");

        this.tauthor = createTextField();
        this.ttitle = createTextField();
        this.tdescription = createTextField();
        this.tlanguage = new JComboBox<>(languages);

        tlanguage.setFont(new Font("Arial", Font.PLAIN, 15));
        tlanguage.setSize(60, 16);
        tlanguage.setLocation(200, 250);

        JLabel type = createLabel("Type");
        type.setLocation(100, 300);

        this.ttype = new JComboBox<>(types);
        ttype.setFont(new Font("Arial", Font.PLAIN, 15));
        ttype.setSize(100, 16);
        ttype.setLocation(200, 300);

        JLabel rating = createLabel("Rating");
        rating.setLocation(100, 350);
        this.trating = new StarRating();

        this.tkey1 = createTextField();
        this.tkey2 = createTextField();
        this.tkey3 = createTextField();
        this.tkey4 = createTextField();
        this.tkey5 = createTextField();
        this.tkey6 = createTextField();
        this.tkey7 = createTextField();
        this.sub = createButton("Submit");
        JButton reset = createButton("Reset");


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
        panel.add(sub);
        panel.add(reset);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        btn.setSize(100, 20);
        btn.setLocation(220, 450);
        btn.addActionListener(this);
        return btn;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setSize(100, 20);
        label.setLocation(100, 200);
        return label;
    }

    private JTextField createTextField() {
        JTextField textfield = new JTextField();
        textfield.setFont(new Font("Arial", Font.PLAIN, 15));
        textfield.setSize(100, 16);
        textfield.setLocation(200, 200);
        return textfield;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == sub ) {

            verifyTextContent(tauthor);
            verifyTextContent(ttitle);
            verifyTextContent(tdescription);
            verifyStarIndex(trating);
            verifyIndex(ttype);
            verifyIndex(tlanguage);

            if (verifyTextContent(tauthor) &&
            verifyTextContent(ttitle) &&
            verifyTextContent(tdescription) &&
            verifyStarIndex(trating) &&
            verifyIndex(ttype) &&
            verifyIndex(tlanguage)) {
                int keyID1 = getKeyID(tkey1);
                int keyID2 = getKeyID(tkey2);
                int keyID3 = getKeyID(tkey3);
                int keyID4 = getKeyID(tkey4);
                int keyID5 = getKeyID(tkey5);
                int keyID6 = getKeyID(tkey6);
                int keyID7 = getKeyID(tkey7);

                long id = db.insertBook(tauthor.getText(),
                        ttitle.getText(),
                        tdescription.getText(),
                        tlanguage.getSelectedIndex(),
                        ttype.getSelectedIndex(),
                        trating.getStar(),
                        keyID1,
                        keyID2,
                        keyID3,
                        keyID4,
                        keyID5,
                        keyID6,
                        keyID7);

                JOptionPane.showMessageDialog(null, String.format("Book %s added to database.", ttitle.getText()));
                Object [] bookRow = new Object[]{id, tauthor.getText(), ttitle.getText(), ttype.getSelectedIndex(), trating.getStar()};
                bo.addBook(bookRow);
                cleanForm();
            } else {
                logger.info("Please check your form input!");
            }
        } else {
            logger.info(("Action deleted"));
            cleanForm();
        }
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
        ttitle.setText(def);
        tdescription.setText(def);
        trating.starsReset();
        ttype.setSelectedIndex(0);
        tlanguage.setSelectedIndex(0);
        tkey1.setText(def);
        tkey2.setText(def);
        tkey3.setText(def);
        tkey4.setText(def);
        tkey5.setText(def);
        tkey6.setText(def);
        tkey7.setText(def);
    }
}
