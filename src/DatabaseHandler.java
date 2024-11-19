import java.sql.*; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 
public class DatabaseHandler { 
    private Connection connection; 

    public DatabaseHandler() {
        connect(); // Gọi phương thức connect để thiết lập kết nối với cơ sở dữ liệu.
    }

    private void connect() { // Phương thức kết nối đến cơ sở dữ liệu.
        try {
            // Thay đổi username và password cho phù hợp với cấu hình MySQL của bạn
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_manager", "root", "12345678");
            // Kết nối đến cơ sở dữ liệu 'book_manager' trên localhost với tài khoản 'root' và mật khẩu '12345678'.
        } catch (SQLException e) { // Xử lý ngoại lệ nếu có lỗi trong quá trình kết nối.
            e.printStackTrace(); // In ra stack trace để theo dõi lỗi.
        }
    }

    public void addBook(Book book) { // Phương thức thêm sách vào cơ sở dữ liệu.
        String query = "INSERT INTO books (name, author, publisher, year, price) VALUES (?, ?, ?, ?, ?)"; // Câu lệnh SQL để thêm sách.
        try (PreparedStatement stmt = connection.prepareStatement(query)) { // Sử dụng PreparedStatement để thực hiện truy vấn.
            stmt.setString(1, book.getName()); // Gán tên sách vào tham số thứ nhất.
            stmt.setString(2, book.getAuthor()); // Gán tác giả vào tham số thứ hai.
            stmt.setString(3, book.getPublisher()); // Gán nhà xuất bản vào tham số thứ ba.
            stmt.setInt(4, book.getYear()); // Gán năm xuất bản vào tham số thứ tư.
            stmt.setDouble(5, book.getPrice()); // Gán giá sách vào tham số thứ năm.
            stmt.executeUpdate(); // Thực thi câu lệnh cập nhật.
        } catch (SQLException e) { // Xử lý ngoại lệ nếu có lỗi trong quá trình thêm sách.
            e.printStackTrace(); // In ra stack trace để theo dõi lỗi.
        }
    }

    public void deleteBook(int id) { // Phương thức xóa sách khỏi cơ sở dữ liệu dựa trên ID.
        String query = "DELETE FROM books WHERE id = ?"; // Câu lệnh SQL để xóa sách.
        try (PreparedStatement stmt = connection.prepareStatement(query)) { // Sử dụng PreparedStatement để thực hiện truy vấn.
            stmt.setInt(1, id); // Gán ID của sách cần xóa vào tham số.
            stmt.executeUpdate(); // Thực thi câu lệnh xóa.
        } catch (SQLException e) { // Xử lý ngoại lệ nếu có lỗi trong quá trình xóa sách.
            e.printStackTrace(); // In ra stack trace để theo dõi lỗi.
        }
    }

    public ObservableList<Book> getAllBooks() { // Phương thức lấy danh sách tất cả sách từ cơ sở dữ liệu.
        ObservableList<Book> bookList = FXCollections.observableArrayList(); // Khởi tạo danh sách quan sát để lưu trữ sách.
        String query = "SELECT * FROM books"; // Câu lệnh SQL để lấy tất cả sách.
        try (Statement stmt = connection.createStatement(); // Tạo đối tượng Statement để thực hiện truy vấn.
             ResultSet rs = stmt.executeQuery(query)) { // Thực thi câu lệnh truy vấn và lưu kết quả vào ResultSet.
            while (rs.next()) { // Duyệt qua từng bản ghi trong ResultSet.
                // Tạo đối tượng Book từ các trường trong ResultSet và thêm vào danh sách sách.
                Book book = new Book(
                    rs.getInt("id"), // Lấy ID sách.
                    rs.getString("name"), // Lấy tên sách.
                    rs.getString("author"), // Lấy tác giả.
                    rs.getString("publisher"), // Lấy nhà xuất bản.
                    rs.getInt("year"), // Lấy năm xuất bản.
                    rs.getDouble("price") // Lấy giá sách.
                );
                bookList.add(book); // Thêm đối tượng Book vào danh sách sách.
            }
        } catch (SQLException e) { // Xử lý ngoại lệ nếu có lỗi trong quá trình truy vấn.
            e.printStackTrace(); // In ra stack trace để theo dõi lỗi.
        }
        return bookList; // Trả về danh sách sách.
    }

    public void updateBook(Book book) { // Phương thức cập nhật thông tin sách trong cơ sở dữ liệu.
        String query = "UPDATE books SET name = ?, author = ?, publisher = ?, year = ?, price = ? WHERE id = ?"; // Câu lệnh SQL để cập nhật sách.
        try (PreparedStatement stmt = connection.prepareStatement(query)) { // Sử dụng PreparedStatement để thực hiện truy vấn.
            stmt.setString(1, book.getName()); // Gán tên sách vào tham số thứ nhất.
            stmt.setString(2, book.getAuthor()); // Gán tác giả vào tham số thứ hai.
            stmt.setString(3, book.getPublisher()); // Gán nhà xuất bản vào tham số thứ ba.
            stmt.setInt(4, book.getYear()); // Gán năm xuất bản vào tham số thứ tư.
            stmt.setDouble(5, book.getPrice()); // Gán giá sách vào tham số thứ năm.
            stmt.setInt(6, book.getId()); // Gán ID sách vào tham số thứ sáu.
            stmt.executeUpdate(); // Thực thi câu lệnh cập nhật.
        } catch (SQLException e) { // Xử lý ngoại lệ nếu có lỗi trong quá trình cập nhật sách.
            e.printStackTrace(); // In ra stack trace để theo dõi lỗi.
        }
    }
}
