package bookstore;

import bookstore.db.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MainProgram extends JPanel {

    Logger logger = LoggerFactory.getLogger(MainProgram.class);
    private static DBConnection conn = null;



    public MainProgram() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon();

        BookOverview bo = new BookOverview();
        bo.setOpaque(true);
        tabbedPane.addTab("Overview", icon, bo,
                "Show all books in database");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        bo.setPreferredSize(new Dimension(700, 700));

        BookSearch bs = new BookSearch();
        tabbedPane.addTab("Search Books", icon, bs,
                "Search books in database");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        BookRegistration br = new BookRegistration(bo);
        JComponent panel3 = br.createBookRegistration("Add Book");
        tabbedPane.addTab("Add Book", icon, panel3,
                "Add a new book to the database");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);


        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        filler.setFont(new Font("TimesRoman", Font.BOLD, 20));
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon() {
        java.net.URL imgURL = getClass().getResource("images/book.png");
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            logger.error("Couldn't find file: " + "images/book.png");
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("BookStorage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/3-frame.getSize().width, dim.height/9-frame.getSize().height);
        frame.add(new MainProgram(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            try {
                createAndShowGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static DBConnection getDBConnection()
    {
        if(null == conn)
        {
            conn = new DBConnection();
        }
        return conn;
    }
}