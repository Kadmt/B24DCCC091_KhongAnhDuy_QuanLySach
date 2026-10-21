package com.example.myapplication.data.repository;

import com.example.myapplication.data.model.Book;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit Test kiểm tra tính đúng đắn của BookRepository và các phép tính thống kê.
 */
public class BookRepositoryTest {

    private BookRepository repository;

    @Before
    public void setUp() {
        repository = BookRepository.getInstance();
        repository.resetToSampleData();
    }

    @Test
    public void initialData_hasThreeSampleBooks() {
        ArrayList<Book> books = repository.getBooks();
        assertEquals(3, books.size());

        Book book1 = books.get(0);
        assertEquals("Lập trình Android cơ bản", book1.getTitle());
        assertEquals("Nguyễn Văn A", book1.getAuthor());
        assertEquals("Công nghệ", book1.getCategory());
        assertEquals(Book.STATUS_READING, book1.getReadingStatus());
        assertEquals(4.0f, book1.getRating(), 0.001);

        Book book2 = books.get(1);
        assertEquals("Đắc nhân tâm", book2.getTitle());
        assertEquals(Book.STATUS_READ, book2.getReadingStatus());

        Book book3 = books.get(2);
        assertEquals("Dế Mèn phiêu lưu ký", book3.getTitle());
        assertEquals(Book.STATUS_UNREAD, book3.getReadingStatus());
    }

    @Test
    public void statistics_initialCountsAreCorrect() {
        assertEquals(3, repository.getTotalBooks());
        assertEquals(1, repository.getReadBooksCount()); // Đắc nhân tâm
        assertEquals(2, repository.getUnfinishedBooksCount()); // Đang đọc (1) + Chưa đọc (1)
    }

    @Test
    public void addBook_increasesTotalAndUnfinishedCounts() {
        Book newBook = new Book(
                "Nhà giả kim",
                "Paulo Coelho",
                "Văn học",
                Book.STATUS_UNREAD,
                4.5f,
                "Hành trình đi tìm kho báu."
        );
        repository.addBook(newBook);

        assertEquals(4, repository.getTotalBooks());
        assertEquals(1, repository.getReadBooksCount());
        assertEquals(3, repository.getUnfinishedBooksCount());
    }

    @Test
    public void markAsRead_updatesStatusAndCounts() {
        Book book3 = repository.getBooks().get(2); // Dế Mèn phiêu lưu ký
        assertEquals(Book.STATUS_UNREAD, book3.getReadingStatus());

        boolean success = repository.markAsRead(book3.getId());
        assertTrue(success);

        assertEquals(Book.STATUS_READ, book3.getReadingStatus());
        assertEquals(2, repository.getReadBooksCount());
        assertEquals(1, repository.getUnfinishedBooksCount());
    }

    @Test
    public void deleteBook_removesBookAndDecreasesTotal() {
        Book book2 = repository.getBooks().get(1); // Đắc nhân tâm
        long idToDelete = book2.getId();

        boolean success = repository.deleteBook(idToDelete);
        assertTrue(success);
        assertEquals(2, repository.getTotalBooks());
        assertEquals(0, repository.getReadBooksCount());
    }

    @Test
    public void updateRating_changesRatingSuccessfully() {
        Book book1 = repository.getBooks().get(0);
        repository.updateRating(book1.getId(), 5.0f);

        assertEquals(5.0f, book1.getRating(), 0.001);
    }
}
