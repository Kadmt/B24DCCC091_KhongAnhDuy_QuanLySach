package com.example.myapplication.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Book;
import com.example.myapplication.data.repository.BookRepository;
import com.example.myapplication.databinding.ActivityBookDetailBinding;
import com.example.myapplication.ui.main.MainActivity;

import java.util.Locale;

/**
 * Màn hình Chi tiết sách, cho phép đổi đánh giá sao, đánh dấu đã đọc hoặc xóa cuốn sách.
 */
public class BookDetailActivity extends AppCompatActivity {

    private ActivityBookDetailBinding binding;
    private BookRepository repository;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = BookRepository.getInstance();

        getIntentData();
        setupListeners();
    }

    private void getIntentData() {
        long bookId = getIntent().getLongExtra(MainActivity.EXTRA_BOOK_ID, -1);
        if (bookId != -1) {
            currentBook = repository.getBookById(bookId);
        }

        if (currentBook == null && getIntent().hasExtra(MainActivity.EXTRA_BOOK)) {
            currentBook = (Book) getIntent().getSerializableExtra(MainActivity.EXTRA_BOOK);
        }

        if (currentBook != null) {
            displayBookDetails(currentBook);
        } else {
            finish();
        }
    }

    /**
     * Hiển thị thông tin chi tiết của cuốn sách lên giao diện.
     */
    private void displayBookDetails(Book book) {
        Context context = this;
        binding.tvTitle.setText(book.getTitle());
        binding.tvAuthor.setText(getString(R.string.author_prefix) + book.getAuthor());
        binding.tvCategory.setText(getString(R.string.category_prefix) + book.getCategory());

        updateStatusUI(book.getReadingStatus());

        // Đánh giá sao
        binding.rbRating.setRating(book.getRating());
        updateRatingLabel(book.getRating());

        // Ghi chú
        String note = book.getNote();
        if (note != null && !note.trim().isEmpty()) {
            binding.tvNote.setText(note);
        } else {
            binding.tvNote.setText(R.string.no_note_placeholder);
        }

        // Kiểm tra nút Đánh dấu đã đọc
        if (Book.STATUS_READ.equalsIgnoreCase(book.getReadingStatus())) {
            binding.btnMarkAsRead.setEnabled(false);
            binding.btnMarkAsRead.setText(R.string.already_read_label);
        } else {
            binding.btnMarkAsRead.setEnabled(true);
            binding.btnMarkAsRead.setText(R.string.mark_as_read_button);
        }
    }

    private void updateStatusUI(String status) {
        binding.tvStatus.setText(status);
        if (Book.STATUS_READ.equalsIgnoreCase(status)) {
            binding.tvStatus.setBackgroundResource(R.drawable.bg_status_read);
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_read_text));
        } else if (Book.STATUS_READING.equalsIgnoreCase(status)) {
            binding.tvStatus.setBackgroundResource(R.drawable.bg_status_reading);
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_reading_text));
        } else {
            binding.tvStatus.setBackgroundResource(R.drawable.bg_status_unread);
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.status_unread_text));
        }
    }

    private void updateRatingLabel(float rating) {
        binding.tvRatingText.setText(String.format(Locale.getDefault(), "%.1f / 5 sao", rating));
    }

    private void setupListeners() {
        binding.btnBackHeader.setOnClickListener(v -> finish());
        binding.btnBack.setOnClickListener(v -> finish());

        // Cho phép người dùng chỉnh lại đánh giá trực tiếp trên RatingBar
        binding.rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser && currentBook != null) {
                currentBook.setRating(rating);
                repository.updateRating(currentBook.getId(), rating);
                updateRatingLabel(rating);
                Toast.makeText(this, getString(R.string.rating_updated, rating), Toast.LENGTH_SHORT).show();
            }
        });

        // Nút Đánh dấu đã đọc
        binding.btnMarkAsRead.setOnClickListener(v -> markAsRead());

        // Nút Xóa sách
        binding.btnDeleteBook.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    /**
     * Chuyển tình trạng đọc của sách sang "Đã đọc".
     */
    private void markAsRead() {
        if (currentBook != null) {
            currentBook.setReadingStatus(Book.STATUS_READ);
            repository.markAsRead(currentBook.getId());

            updateStatusUI(Book.STATUS_READ);
            binding.btnMarkAsRead.setEnabled(false);
            binding.btnMarkAsRead.setText(R.string.already_read_label);

            Toast.makeText(this, R.string.mark_as_read_success, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hiển thị hộp thoại xác nhận trước khi xóa sách.
     */
    private void showDeleteConfirmationDialog() {
        if (currentBook == null) return;

        String message = String.format(getString(R.string.dialog_delete_book_message), currentBook.getTitle());

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_book_title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_confirm_delete, (dialog, which) -> deleteBook())
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    /**
     * Xóa cuốn sách khỏi danh sách và quay trở lại màn hình chính.
     */
    private void deleteBook() {
        if (currentBook != null) {
            repository.deleteBook(currentBook.getId());
            Toast.makeText(this, R.string.delete_book_success, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
