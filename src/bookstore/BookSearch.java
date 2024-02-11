package bookstore;

import bookstore.dataobjects.Book;
import bookstore.db.BookDAO;
import bookstore.util.BookUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Vector;

import static bookstore.util.BookStoreComponents.*;

public class BookSearch extends JPanel {
    Vector<Book> booksByKeyword = null;

    private final JButton searchBtn = new JButton("Search");
    private final JTextField searchable = createTextField();
    private final String[] columnNames = { "ID", "Author", "Title", "Type", "Rating" };
    DefaultTableModel tableModel= new DefaultTableModel(columnNames, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }
    };
    private JTable bookTable = new JTable(tableModel);
    private final JScrollPane scrollPane = new JScrollPane(bookTable);
    private final JPanel bookDetailsPanel = new JPanel(new GridLayout(1, 0));
    private final CardLayout cardLayout = new CardLayout();

    public BookSearch() {
        super();

        createBookSearch("Search books");
        addSearch();
    }

    private void addSearch() {
        searchBtn.addActionListener(e -> {
            BookDAO bookDAO = new BookDAO();
            while(tableModel.getRowCount() > 0)
            {
                tableModel.removeRow(0);
            }
            bookDetailsPanel.removeAll();
            bookDetailsPanel.revalidate();
            bookDetailsPanel.repaint();
            this.booksByKeyword = bookDAO.findBooksByKeyword(searchable.getText());
            for (Book book : booksByKeyword) {
                Object [] bookRow = new Object[]{book.getID(), book.getAuthor(), book.getTitle(), BookUtil.getType(book.getType()), book.getRating()};
                tableModel.addRow(bookRow);
            }
        });
    }

    public void createBookSearch(String text) {
        this.setLayout(new BorderLayout(0,0));
        add(createTitle(text), BorderLayout.PAGE_START);
        add(createContentPanel(), BorderLayout.CENTER);
        renderBookTable();
    }

    private void renderBookTable() {
        bookTable.setRowHeight(30);
        bookTable.getColumn("ID").setMaxWidth(25);
        bookTable.getColumn("Rating").setMaxWidth(40);
        bookTable.getColumn("Type").setMaxWidth(35);


        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        bookTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        bookTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayBookDetails(selectedRow);
                }
            }
        });
    }

    private void displayBookDetails(int rowIndex) {
        Book book = booksByKeyword.get(rowIndex);

        bookDetailsPanel.removeAll();
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel("Title: " + book.getTitle()));
        detailsPanel.add(new JLabel("Author: " + book.getAuthor()));
        detailsPanel.add(new JLabel("Description: " + book.getDescription()));
        detailsPanel.add(new JLabel("Language: " + BookUtil.getLanguage(book.getLanguage())));
        detailsPanel.add(new JLabel("Type: " + BookUtil.getType(book.getType())));
        detailsPanel.add(new JLabel("Rating: " + book.getRating()));
        bookDetailsPanel.add(detailsPanel, "detailsPanel");
        cardLayout.show(bookDetailsPanel, "detailsPanel");
        bookDetailsPanel.revalidate();
        bookDetailsPanel.repaint();
    }


    private JPanel createSearchPanel(){
        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(120,25));
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));

        searchPanel.add(searchable);
        searchPanel.add(searchBtn);
        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(e -> {
            try {
                exportSelectedBooksToExcel();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        searchPanel.add(exportButton);

        return searchPanel;
    }


    private JPanel createContentPanel(){
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        contentPanel.add(createSearchPanel(), BorderLayout.PAGE_START);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        bookDetailsPanel.setLayout(cardLayout);
        contentPanel.add(bookDetailsPanel, BorderLayout.PAGE_END);

        return contentPanel;
    }

    private void exportSelectedBooksToExcel() throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Create Excel file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xlsx", "xls");
        fileChooser.setFileFilter(filter);
        String defaultFilename = "books.xlsx";
        fileChooser.setSelectedFile(new File(defaultFilename));
        int userSelection  = fileChooser.showSaveDialog(this);
        if (userSelection  == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String selectedDirectory = selectedFile.getParent();
            String filename = selectedFile.getName();
            String excelFilePath = selectedDirectory + File.separator + filename;
            if (!excelFilePath.endsWith(".xlsx")) {
                excelFilePath+=  ".xlsx";
            }
            writeBooksToExcel(excelFilePath);
            JOptionPane.showMessageDialog(this, "Excel file saved successfully to: " + excelFilePath);
        }
    }

    private void writeBooksToExcel( String filePath) throws FileNotFoundException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Books");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Author", "Title", "Description", "Language", "Type", "Rating"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Populate data rows
        for (int i = 0; i < this.booksByKeyword.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Book book = this.booksByKeyword.get(i);
            row.createCell(0).setCellValue(book.getTitle());
            row.createCell(1).setCellValue(book.getAuthor());
            row.createCell(2).setCellValue(book.getDescription());
            row.createCell(2).setCellValue(BookUtil.getLanguage(book.getLanguage()));
            row.createCell(2).setCellValue(BookUtil.getType(book.getType()));
            row.createCell(5).setCellValue(book.getRating());
        }


        // Write the workbook to the file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
