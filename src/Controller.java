import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private TextField authorField, nameField, priceField, publisherField, searchField, yearField;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> colAuthor, colName, colPublisher;

    @FXML
    private TableColumn<Book, Integer> colId, colYear;

    @FXML
    private TableColumn<Book, Double> colPrice;

    private ObservableList<Book> bookList;
    private DatabaseHandler databaseHandler; // Khai báo DatabaseHandler

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        databaseHandler = new DatabaseHandler(); // Khởi tạo DatabaseHandler
        loadBooks(); // Tải sách từ cơ sở dữ liệu

        // Thêm sự kiện khi nhấn vào hàng
        booksTable.setOnMouseClicked(this::handleBookSelection);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(new ActionEvent()); // Gọi lại phương thức tìm kiếm
        });
        
    }

    private void loadBooks() {
        bookList = databaseHandler.getAllBooks();
        booksTable.setItems(bookList);
    }

    // Xử lý khi người dùng nhấn vào sách trong danh sách
    private void handleBookSelection(MouseEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Điền thông tin vào các trường
            nameField.setText(selectedBook.getName());
            authorField.setText(selectedBook.getAuthor());
            publisherField.setText(selectedBook.getPublisher());
            yearField.setText(String.valueOf(selectedBook.getYear()));
            priceField.setText(String.valueOf(selectedBook.getPrice()));
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        String name = nameField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = Integer.parseInt(yearField.getText());
        double price = Double.parseDouble(priceField.getText());

        Book newBook = new Book(0, name, author, publisher, year, price); // ID sẽ được tự động sinh
        databaseHandler.addBook(newBook); // Thêm vào cơ sở dữ liệu
        bookList.add(newBook); // Cập nhật danh sách hiển thị
        clearFields();
        loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu
    }

    @FXML
    void handleDelete(ActionEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            databaseHandler.deleteBook(selectedBook.getId()); // Xóa khỏi cơ sở dữ liệu
            bookList.remove(selectedBook); // Cập nhật danh sách hiển thị
            loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().toLowerCase();
    
        if (keyword.isEmpty()) {
            // Nếu ô tìm kiếm rỗng, tải lại toàn bộ danh sách sách
            loadBooks();
        } else {
            // Tìm kiếm theo từ khóa
            ObservableList<Book> filteredList = bookList.filtered(book -> 
                book.getName().toLowerCase().contains(keyword) || 
                book.getAuthor().toLowerCase().contains(keyword) || 
                book.getPublisher().toLowerCase().contains(keyword) || 
                String.valueOf(book.getYear()).contains(keyword) // Tìm kiếm theo năm
            );
            booksTable.setItems(filteredList);
        }
    }
    

    @FXML
    void handleUpdate(ActionEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setName(nameField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPublisher(publisherField.getText());
            selectedBook.setYear(Integer.parseInt(yearField.getText()));
            selectedBook.setPrice(Double.parseDouble(priceField.getText()));
            databaseHandler.updateBook(selectedBook); // Cập nhật cơ sở dữ liệu
            booksTable.refresh(); // Cập nhật giao diện
            clearFields();
            loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu
        }
    }

    private void clearFields() {
        nameField.clear();
        authorField.clear();
        publisherField.clear();
        yearField.clear();
        priceField.clear();
    }
}
