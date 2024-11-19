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
    private DatabaseHandler databaseHandler; 

    @FXML
    public void initialize() { // Phương thức khởi tạo sẽ được gọi tự động khi lớp được khởi tạo.
        // Gán giá trị cho các cột của TableView bằng cách sử dụng PropertyValueFactory.
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        databaseHandler = new DatabaseHandler(); // Khởi tạo đối tượng DatabaseHandler để kết nối với cơ sở dữ liệu.
        loadBooks(); // Tải danh sách sách từ cơ sở dữ liệu.

        // Thêm sự kiện khi nhấn vào hàng trong TableView.
        booksTable.setOnMouseClicked(this::handleBookSelection);

        // Thêm listener cho ô tìm kiếm, gọi lại phương thức tìm kiếm khi người dùng nhập vào ô tìm kiếm.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearch(new ActionEvent()); // Gọi phương thức tìm kiếm khi ô tìm kiếm thay đổi.
        });
    }

    private void loadBooks() { // Phương thức tải sách từ cơ sở dữ liệu.
        bookList = databaseHandler.getAllBooks(); // Lấy danh sách tất cả sách từ cơ sở dữ liệu.
        booksTable.setItems(bookList); // Đặt danh sách sách vào TableView.
    }

    // Xử lý khi người dùng nhấn vào sách trong danh sách.
    private void handleBookSelection(MouseEvent event) {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem(); // Lấy sách đã chọn từ TableView.
        if (selectedBook != null) { // Kiểm tra xem có sách nào được chọn hay không.
            // Điền thông tin vào các trường nhập liệu.
            nameField.setText(selectedBook.getName());
            authorField.setText(selectedBook.getAuthor());
            publisherField.setText(selectedBook.getPublisher());
            yearField.setText(String.valueOf(selectedBook.getYear()));
            priceField.setText(String.valueOf(selectedBook.getPrice()));
        }
    }

    @FXML
    void handleAdd(ActionEvent event) { // Phương thức xử lý khi nhấn nút thêm.
        String name = nameField.getText(); // Lấy tên sách từ ô nhập liệu.
        String author = authorField.getText(); // Lấy tên tác giả từ ô nhập liệu.
        String publisher = publisherField.getText(); // Lấy nhà xuất bản từ ô nhập liệu.
        int year = Integer.parseInt(yearField.getText()); // Lấy năm từ ô nhập liệu và chuyển đổi thành kiểu số nguyên.
        double price = Double.parseDouble(priceField.getText()); // Lấy giá sách từ ô nhập liệu và chuyển đổi thành kiểu số thực.

        Book newBook = new Book(0, name, author, publisher, year, price); // Tạo đối tượng Book mới, ID sẽ được tự động sinh.
        databaseHandler.addBook(newBook); // Thêm sách mới vào cơ sở dữ liệu.
        bookList.add(newBook); // Cập nhật danh sách hiển thị với sách mới.
        clearFields(); // Xóa các trường nhập liệu.
        loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu để đảm bảo tính đồng bộ.
    }

    @FXML
    void handleDelete(ActionEvent event) { // Phương thức xử lý khi nhấn nút xóa.
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem(); // Lấy sách đã chọn từ TableView.
        if (selectedBook != null) { // Kiểm tra xem có sách nào được chọn hay không.
            databaseHandler.deleteBook(selectedBook.getId()); // Xóa sách khỏi cơ sở dữ liệu.
            bookList.remove(selectedBook); // Cập nhật danh sách hiển thị bằng cách xóa sách đã chọn.
            loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu.
        }
    }

    @FXML
    void handleSearch(ActionEvent event) { // Phương thức xử lý tìm kiếm.
        String keyword = searchField.getText().toLowerCase(); // Lấy từ khóa tìm kiếm và chuyển đổi thành chữ thường.
    
        if (keyword.isEmpty()) { // Kiểm tra xem ô tìm kiếm có rỗng không.
            loadBooks(); // Nếu ô tìm kiếm rỗng, tải lại toàn bộ danh sách sách.
        } else {
            // Tìm kiếm theo từ khóa trong các thuộc tính của sách.
            ObservableList<Book> filteredList = bookList.filtered(book -> 
                book.getName().toLowerCase().contains(keyword) || // Tìm kiếm theo tên sách.
                book.getAuthor().toLowerCase().contains(keyword) || // Tìm kiếm theo tác giả.
                book.getPublisher().toLowerCase().contains(keyword) || // Tìm kiếm theo nhà xuất bản.
                String.valueOf(book.getYear()).contains(keyword) // Tìm kiếm theo năm.
            );
            booksTable.setItems(filteredList); // Cập nhật TableView với danh sách sách đã lọc.
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) { // Phương thức xử lý khi nhấn nút cập nhật.
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem(); // Lấy sách đã chọn từ TableView.
        if (selectedBook != null) { // Kiểm tra xem có sách nào được chọn hay không.
            // Cập nhật các thuộc tính của sách với giá trị từ các trường nhập liệu.
            selectedBook.setName(nameField.getText());
            selectedBook.setAuthor(authorField.getText());
            selectedBook.setPublisher(publisherField.getText());
            selectedBook.setYear(Integer.parseInt(yearField.getText()));
            selectedBook.setPrice(Double.parseDouble(priceField.getText()));
            databaseHandler.updateBook(selectedBook); // Cập nhật sách trong cơ sở dữ liệu.
            booksTable.refresh(); // Cập nhật giao diện TableView.
            clearFields(); // Xóa các trường nhập liệu.
            loadBooks(); // Tải lại danh sách sách từ cơ sở dữ liệu.
        }
    }

    private void clearFields() { // Phương thức xóa các trường nhập liệu.
        nameField.clear(); // Xóa tên sách.
        authorField.clear(); // Xóa tên tác giả.
        publisherField.clear(); // Xóa nhà xuất bản.
        yearField.clear(); // Xóa năm xuất bản.
        priceField.clear(); // Xóa giá sách.
    }
}