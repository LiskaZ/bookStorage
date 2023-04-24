package components;

import components.starrating.StarRating;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookRegistration implements ActionListener {

    private JLabel author;
    private JTextField tauthor;
    private JLabel booktitle;
    private JTextField tbooktitle;
    private JLabel bookDescription;
    private JTextField tbookDescription;
    private JLabel language;
    private JComboBox tlanguage;
    private JLabel type;
    private JComboBox ttype;
    private JLabel rating;
    private JButton sub;
    private JButton reset;
    private StarRating starRating;

    private String languages[]
            = { "select language", "german", "english" };
    private String types[]
            = { "select type", "intimate", "great ones", "better ones", "funny or magic" };

    int filledForm = 0;

    public JComponent createBookRegistration(String text) {
        JPanel panel = new JPanel(false);

        JLabel title = new JLabel(text);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 20));
        title.setLocation(150, 50);


        this.author = new JLabel("Authors name");
        author.setFont(new Font("Arial", Font.PLAIN, 20));
        author.setSize(100, 20);
        author.setLocation(100, 100);

        this.tauthor = new JTextField();
        tauthor.setFont(new Font("Arial", Font.PLAIN, 15));
        tauthor.setSize(100, 20);
        tauthor.setLocation(200, 100);

        this.booktitle = new JLabel("Title");
        booktitle.setFont(new Font("Arial", Font.PLAIN, 20));
        booktitle.setSize(100, 20);
        booktitle.setLocation(100, 150);

        this.tbooktitle = new JTextField();
        tbooktitle.setFont(new Font("Arial", Font.PLAIN, 15));
        tbooktitle.setSize(100, 20);
        tbooktitle.setLocation(200, 150);

        this.bookDescription = new JLabel("Description");
        bookDescription.setFont(new Font("Arial", Font.PLAIN, 20));
        bookDescription.setSize(100, 20);
        bookDescription.setLocation(100, 200);

        this.tbookDescription = new JTextField();
        tbookDescription.setFont(new Font("Arial", Font.PLAIN, 15));
        tbookDescription.setSize(100, 20);
        tbookDescription.setLocation(200, 200);

        this.language = new JLabel("Language");
        language.setFont(new Font("Arial", Font.PLAIN, 20));
        language.setSize(100, 20);
        language.setLocation(100, 250);

        this.tlanguage = new JComboBox(languages);
        tlanguage.setFont(new Font("Arial", Font.PLAIN, 15));
        tlanguage.setSize(60, 20);
        tlanguage.setLocation(200, 250);

        this.type = new JLabel("Type");
        type.setFont(new Font("Arial", Font.PLAIN, 20));
        type.setSize(100, 20);
        type.setLocation(100, 300);

        this.ttype = new JComboBox(types);
        ttype.setFont(new Font("Arial", Font.PLAIN, 15));
        ttype.setSize(100, 20);
        ttype.setLocation(200, 300);

        this.rating = new JLabel("Rating");
        rating.setFont(new Font("Arial", Font.PLAIN, 20));
        rating.setSize(100, 20);
        rating.setLocation(100, 350);

        this.starRating = new StarRating();

        this.sub = new JButton("Submit");
        sub.setFont(new Font("Arial", Font.PLAIN, 15));
        sub.setSize(100, 20);
        sub.setLocation(120, 450);
        sub.addActionListener(this);

        this.reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 15));
        reset.setSize(100, 20);
        reset.setLocation(220, 450);
        reset.addActionListener(this);



        panel.setLayout(new GridLayout(0, 1));
        panel.add(title);
        panel.add(author);
        panel.add(tauthor);
        panel.add(booktitle);
        panel.add(tbooktitle);
        panel.add(bookDescription);
        panel.add(tbookDescription);
        panel.add(language);
        panel.add(tlanguage);
        panel.add(type);
        panel.add(ttype);
        panel.add(rating);
        panel.add(starRating);
        panel.add(sub);
        panel.add(reset);


        return panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == sub ) {

            verifyTextContent(tauthor);
            verifyTextContent(tbooktitle);
            verifyTextContent(tbookDescription);
            verifyStarIndex(starRating);
            verifyIndex(ttype);
            verifyIndex(tlanguage);

            if (filledForm == 6) {
                JOptionPane.showMessageDialog(null, String.format("Buch %s der Datenbank hinzugefügt.", tbooktitle.getText()));
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

    private void verifyIndex(JComboBox comboBox) {
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
        tbooktitle.setText(def);
        tbookDescription.setText(def);
        starRating.starsReset();
        ttype.setSelectedIndex(0);
        tlanguage.setSelectedIndex(0);
    }
}
