package bookstore;

import bookstore.dataobjects.Book;
import bookstore.db.BookDAO;
import bookstore.util.BookUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import static bookstore.util.BookStoreComponents.createTitle;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

public class BookOverview extends JPanel {

    private final String[] columnNames = { "ID", "Author", "Title", "Type", "Rating", "Delete" };
    DefaultTableModel tableModel= new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }
    };
    JTable bookOverview = new JTable(tableModel);
    private boolean[] columnSortAscending;

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
        bookOverview.getColumn("ID").setMaxWidth(30);
        bookOverview.getColumn("Rating").setMaxWidth(40);
        bookOverview.getColumn("Type").setMaxWidth(70);
        bookOverview.getColumn("Delete").setMaxWidth(42);
        bookOverview.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        bookOverview.getColumn("Delete").setCellEditor(
                new ButtonEditor(new JCheckBox(), this));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookOverview.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        bookOverview.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bookOverview.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        columnSortAscending = new boolean[bookOverview.getColumnCount()];

        bookOverview.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = bookOverview.columnAtPoint(e.getPoint());
                if (columnIndex >= 0 && columnIndex <= 4) {
                    sortTable(columnIndex);
                }
            }
        });

        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        bookOverview.setRowSorter(sorter);
    }

    private void sortTable(int columnIndex) {
        columnSortAscending[columnIndex] = !columnSortAscending[columnIndex];

        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) bookOverview.getRowSorter();

        Comparator<Object> comparator = (o1, o2) -> {
            if (o1 instanceof Integer && o2 instanceof Integer) {
                return columnSortAscending[columnIndex] ? (Integer) o1 - (Integer) o2 : (Integer) o2 - (Integer) o1;
            } else {
                String s1 = o1.toString().toLowerCase();
                String s2 = o2.toString().toLowerCase();
                return columnSortAscending[columnIndex] ? s1.compareTo(s2) : s2.compareTo(s1);
            }
        };

        sorter.setComparator(columnIndex, comparator);
        sorter.sort();
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
        books.forEach(book -> {
                    Object[] row = {
                            book.getID(),
                            book.getAuthor(),
                            book.getTitle(),
                            BookUtil.getType(book.getType()),
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
