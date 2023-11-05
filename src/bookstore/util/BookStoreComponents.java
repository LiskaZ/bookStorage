package bookstore.util;

import javax.swing.*;
import java.awt.*;

public class BookStoreComponents {

    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        btn.setSize(100, 20);
        btn.setLocation(220, 450);
        return btn;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setSize(100, 20);
        label.setLocation(100, 200);
        return label;
    }

    public static JTextField createTextField() {
        JTextField textfield = new JTextField();
        textfield.setFont(new Font("Arial", Font.PLAIN, 15));
        textfield.setSize(100, 16);
        textfield.setLocation(200, 200);
        return textfield;
    }

    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("TimesRoman", Font.BOLD, 20));
        label.setLocation(150, 50);
        return label;
    }
}
