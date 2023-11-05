package bookstore;

import bookstore.db.DBConnection;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private final DBConnection db;
    private final BookOverview bo;


    private String bookID;
    private String bookTitle;
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
        this.db = new DBConnection();
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
        bookTitle = table.getValueAt(row, column-4).toString();
        bookID = (value == null) ? "" : value.toString();
        bookRow = row;
        button.setText("x");
        isPushed = true;

        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            db.deleteBook(bookID);
            bo.removeBook(bookRow);
            JOptionPane.showMessageDialog(button, bookTitle + " removed");
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
