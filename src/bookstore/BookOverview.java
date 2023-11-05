package bookstore;

import bookstore.dataobjects.Book;
import bookstore.db.BookDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import static bookstore.util.BookStoreComponents.createTitle;

import java.util.ArrayList;
import java.util.Vector;

public class BookOverview extends JPanel {

    private String[] columnNames = { "ID", "Author", "Title", "Type", "Rating", "Delete" };
    DefaultTableModel tableModel= new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5 ? true : false;
        }
    };
    JTable bookOverview = new JTable(tableModel);

    public BookOverview() {
        super();

        createBookOverview("Overview");
    }

    public void createBookOverview(String text) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JLabel title = createTitle(text);

        this.add(title, CENTER_ALIGNMENT);
        this.add(Box.createHorizontalGlue());

        getBooksFromDataBase();
        renderBookTable();

        this.add(bookOverview.getTableHeader(), CENTER_ALIGNMENT);
        this.add(new JScrollPane(bookOverview), CENTER_ALIGNMENT);
    }

    private void renderBookTable() {
        bookOverview.setRowHeight(30);
        bookOverview.getColumn("ID").setMaxWidth(25);
        bookOverview.getColumn("Rating").setMaxWidth(40);
        bookOverview.getColumn("Type").setMaxWidth(35);
        bookOverview.getColumn("Delete").setMaxWidth(40);
        bookOverview.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        bookOverview.getColumn("Delete").setCellEditor(
                new ButtonEditor(new JCheckBox(), this));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookOverview.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        bookOverview.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bookOverview.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    }

    private void getBooksFromDataBase() {
        Object [][] books = getBooks();
        for (Object[] book : books) {
            tableModel.addRow(book);
        }
    }

    public void addBook(Object[] book) {
        tableModel.addRow(book);
    }

    public void removeBook(int bookRow) {
        DefaultTableModel model = (DefaultTableModel) bookOverview.getModel();
        model.removeRow(bookRow);
    }

    private Object[][] getBooks() {
        ArrayList<Object[]> rows = new ArrayList<>();

        BookDAO bookstore = new BookDAO();
        Vector<Book> books = bookstore.loadAll();
        books.stream().forEach(book -> {
                    Object[] row = {
                            book.getID(),
                            book.getAuthor(),
                            book.getTitle(),
                            book.getType(),
                            book.getRating(),
                            book.getID()};
                    rows.add(row);
                }
        );
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        return data;
    }
}
