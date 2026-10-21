package com.example.myapplication.data.repository;

import com.example.myapplication.data.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository quản lý danh sách sách trong bộ nhớ (In-Memory) sử dụng ArrayList<Book>.
 * Áp dụng mẫu thiết kế Singleton để chia sẻ dữ liệu đồng nhất giữa tất cả các Activity.
 */
public class BookRepository {

    private static BookRepository instance;
    private final ArrayList<Book> bookList;
    private long currentIdCounter = 1;

    private BookRepository() {
        bookList = new ArrayList<>();
        seedSampleData();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    /**
     * Nạp 3 cuốn sách mẫu chuẩn theo yêu cầu của đề bài.
     */
    private void seedSampleData() {
        bookList.clear();
        bookList.add(new Book(
                currentIdCounter++,
                "Lập trình Android cơ bản",
                "Nguyễn Văn A",
                "Công nghệ",
                Book.STATUS_READING,
                4.0f,
                "Sách giáo trình nền tảng về Activity, Intent và View."
        ));
        bookList.add(new Book(
                currentIdCounter++,
                "Đắc nhân tâm",
                "Dale Carnegie",
                "Kỹ năng sống",
                Book.STATUS_READ,
                5.0f,
                "Cuốn sách kinh điển về nghệ thuật ứng xử và giao tiếp."
        ));
        bookList.add(new Book(
                currentIdCounter++,
                "Dế Mèn phiêu lưu ký",
                "Tô Hoài",
                "Văn học",
                Book.STATUS_UNREAD,
                0.0f,
                "Tác phẩm văn học thiếu nhi nổi tiếng của nhà văn Tô Hoài."
        ));
    }

    /**
     * Lấy toàn bộ danh sách sách hiện có.
     */
    public ArrayList<Book> getBooks() {
        return bookList;
    }

    /**
     * Tìm sách theo ID.
     */
    public Book getBookById(long id) {
        for (Book book : bookList) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    /**
     * Thêm một cuốn sách mới vào danh sách.
     */
    public void addBook(Book book) {
        if (book != null) {
            if (book.getId() <= 0) {
                book.setId(currentIdCounter++);
            }
            bookList.add(0, book); // Thêm lên đầu danh sách để dễ quan sát
        }
    }

    /**
     * Xóa một cuốn sách khỏi danh sách theo ID.
     */
    public boolean deleteBook(long id) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getId() == id) {
                bookList.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Cập nhật tình trạng sách thành "Đã đọc".
     */
    public boolean markAsRead(long id) {
        Book book = getBookById(id);
        if (book != null) {
            book.setReadingStatus(Book.STATUS_READ);
            return true;
        }
        return false;
    }

    /**
     * Cập nhật đánh giá số sao của sách.
     */
    public boolean updateRating(long id, float rating) {
        Book book = getBookById(id);
        if (book != null) {
            book.setRating(rating);
            return true;
        }
        return false;
    }

    // --- Thống kê ---

    /**
     * Tổng số sách trong thư viện.
     */
    public int getTotalBooks() {
        return bookList.size();
    }

    /**
     * Số sách đã đọc xong.
     */
    public int getReadBooksCount() {
        int count = 0;
        for (Book book : bookList) {
            if (Book.STATUS_READ.equalsIgnoreCase(book.getReadingStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Số sách chưa đọc xong (Bao gồm cả 'Chưa đọc' và 'Đang đọc').
     */
    public int getUnfinishedBooksCount() {
        int count = 0;
        for (Book book : bookList) {
            if (!Book.STATUS_READ.equalsIgnoreCase(book.getReadingStatus())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Reset lại danh sách mẫu (tiện cho Unit Test).
     */
    public void resetToSampleData() {
        currentIdCounter = 1;
        seedSampleData();
    }
}
