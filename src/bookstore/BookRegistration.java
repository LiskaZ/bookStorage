package bookstore;

import bookstore.db.DBConnection;
import bookstore.starrating.StarRating;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookRegistration implements ActionListener {

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

    int filledForm = 0;

    public BookRegistration(BookOverview bo) {
        this.db = new DBConnection();
        this.bo = bo;
    }

    public JComponent createBookRegistration(String text) {
        JPanel panel = new JPanel(false);

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.BOLD, 20));
        title.setLocation(150, 50);


        JLabel author = new JLabel("Authors name");
        author.setFont(new Font("Arial", Font.PLAIN, 20));
        author.setSize(100, 20);
        author.setLocation(100, 100);

        this.tauthor = new JTextField();
        tauthor.setFont(new Font("Arial", Font.PLAIN, 15));
        tauthor.setSize(100, 16);
        tauthor.setLocation(200, 100);

        JLabel title1 = new JLabel("Title");
        title1.setFont(new Font("Arial", Font.PLAIN, 20));
        title1.setSize(100, 20);
        title1.setLocation(100, 150);

        this.ttitle = new JTextField();
        ttitle.setFont(new Font("Arial", Font.PLAIN, 15));
        ttitle.setSize(100, 16);
        ttitle.setLocation(200, 150);

        JLabel description = new JLabel("Description");
        description.setFont(new Font("Arial", Font.PLAIN, 20));
        description.setSize(100, 20);
        description.setLocation(100, 200);

        this.tdescription = new JTextField();
        tdescription.setFont(new Font("Arial", Font.PLAIN, 15));
        tdescription.setSize(100, 16);
        tdescription.setLocation(200, 200);

        JLabel language = new JLabel("Language");
        language.setFont(new Font("Arial", Font.PLAIN, 20));
        language.setSize(100, 20);
        language.setLocation(100, 250);

        this.tlanguage = new JComboBox<>(languages);
        tlanguage.setFont(new Font("Arial", Font.PLAIN, 15));
        tlanguage.setSize(60, 16);
        tlanguage.setLocation(200, 250);

        JLabel type = new JLabel("Type");
        type.setFont(new Font("Arial", Font.PLAIN, 20));
        type.setSize(100, 20);
        type.setLocation(100, 300);

        this.ttype = new JComboBox<>(types);
        ttype.setFont(new Font("Arial", Font.PLAIN, 15));
        ttype.setSize(100, 16);
        ttype.setLocation(200, 300);

        JLabel rating = new JLabel("Rating");
        rating.setFont(new Font("Arial", Font.PLAIN, 20));
        rating.setSize(100, 20);
        rating.setLocation(100, 350);

        this.trating = new StarRating();

        JLabel key1 = new JLabel("Keyword 1");
        key1.setFont(new Font("Arial", Font.PLAIN, 20));
        key1.setSize(100, 20);
        key1.setLocation(100, 200);

        this.tkey1 = new JTextField();
        tkey1.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey1.setSize(100, 16);
        tkey1.setLocation(200, 200);

        JLabel key2 = new JLabel("Keyword 2");
        key2.setFont(new Font("Arial", Font.PLAIN, 20));
        key2.setSize(100, 20);
        key2.setLocation(100, 200);

        this.tkey2 = new JTextField();
        tkey2.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey2.setSize(100, 16);
        tkey2.setLocation(200, 200);

        JLabel key3 = new JLabel("Keyword 3");
        key3.setFont(new Font("Arial", Font.PLAIN, 20));
        key3.setSize(100, 20);
        key3.setLocation(100, 200);

        this.tkey3 = new JTextField();
        tkey3.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey3.setSize(100, 16);
        tkey3.setLocation(200, 200);

        JLabel key4 = new JLabel("Keyword 4");
        key4.setFont(new Font("Arial", Font.PLAIN, 20));
        key4.setSize(100, 16);
        key4.setLocation(100, 200);

        this.tkey4 = new JTextField();
        tkey4.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey4.setSize(100, 16);
        tkey4.setLocation(200, 200);

        JLabel key5 = new JLabel("Keyword 5");
        key5.setFont(new Font("Arial", Font.PLAIN, 20));
        key5.setSize(100, 20);
        key5.setLocation(100, 200);

        this.tkey5 = new JTextField();
        tkey5.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey5.setSize(100, 16);
        tkey5.setLocation(200, 200);

        JLabel key6 = new JLabel("Keyword 6");
        key6.setFont(new Font("Arial", Font.PLAIN, 20));
        key6.setSize(100, 20);
        key6.setLocation(100, 200);

        this.tkey6 = new JTextField();
        tkey6.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey6.setSize(100, 16);
        tkey6.setLocation(200, 200);

        JLabel key7 = new JLabel("Keyword 7");
        key7.setFont(new Font("Arial", Font.PLAIN, 20));
        key7.setSize(100, 20);
        key7.setLocation(100, 200);

        this.tkey7 = new JTextField();
        tkey7.setFont(new Font("Arial", Font.PLAIN, 15));
        tkey7.setSize(100, 16);
        tkey7.setLocation(200, 200);

        this.sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(120, 450);
        sub.addActionListener(this);

        JButton reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(220, 450);
        reset.addActionListener(this);



        panel.setLayout(new GridLayout(0, 1));
        panel.add(title);
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


        return panel;
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

            if (filledForm == 6) {
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
                System.out.println("Action performed");
                cleanForm();
            } else {
                System.out.println("Please check your form input!");
            }
            filledForm = 0;
        } else {
            System.out.println(("Action deleted"));
            cleanForm();
        }
    }

    private int getKeyID(JTextField tkey) {;
        if (!tkey.getText().isEmpty()) {
            return db.insertKeyword(tkey.getText());
        } else {
            return 0;
        }
    }

    private void verifyIndex(JComboBox<String> comboBox) {
        if (comboBox.getSelectedIndex() == 0){
            comboBox.setBorder(new LineBorder(Color.red,1));
        } else {
            filledForm += 1;
            comboBox.setBorder(new LineBorder(Color.black,1));

        }
    }

    private void verifyStarIndex(StarRating starRating) {
        if (starRating.getStar() == 0) {
            starRating.setBorder(new LineBorder(Color.red, 1));
        } else {
            filledForm += 1;
            starRating.setBorder(new LineBorder(Color.black,1));
        }
    }

    private void verifyTextContent(JTextField textField) {
        if (textField.getText().isEmpty()){
            textField.setBorder(new LineBorder(Color.red,1));
        } else {
            filledForm += 1;
            textField.setBorder(new LineBorder(Color.black,1));
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
