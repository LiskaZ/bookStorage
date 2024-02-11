package bookstore;

import bookstore.db.BookDAO;
import bookstore.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private final BookOverview bo;


    private String bookID;
    private int bookRow;


    private boolean isPushed;

    public ButtonEditor(JCheckBox checkBox, BookOverview bookOverview) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isPushed = true;
                getCellEditorValue();
            }
        });
        DBConnection db = new DBConnection();
        bo = bookOverview;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        String bookTitle = table.getValueAt(row, column - 4).toString();
        bookID = (value == null) ? "" : value.toString();
        bookRow = row;
        button.setText("x");
        isPushed = true;

        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            BookDAO bookstore = new BookDAO();
            bookstore.remove(parseInt(bookID));
            String title = bo.bookOverview.getValueAt(bookRow, 2).toString();
            bo.removeBook(bookRow);
            JOptionPane.showMessageDialog(button, title + " removed");
        }
        isPushed = false;
        return new String(bookID);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
