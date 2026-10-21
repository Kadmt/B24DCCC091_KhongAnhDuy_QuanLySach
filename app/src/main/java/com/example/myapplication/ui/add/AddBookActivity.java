package com.example.myapplication.ui.add;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Book;
import com.example.myapplication.data.repository.BookRepository;
import com.example.myapplication.databinding.ActivityAddBookBinding;

import java.util.Locale;

/**
 * Màn hình Thêm sách mới vào thư viện cá nhân.
 */
public class AddBookActivity extends AppCompatActivity {

    private ActivityAddBookBinding binding;
    private BookRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = BookRepository.getInstance();

        initCategorySpinner();
        initRatingBar();
        setupListeners();
    }

    private void initCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.book_categories, android.R.layout.simple_spinner_dropdown_item
        );
        binding.spCategory.setAdapter(adapter);
    }

    private void initRatingBar() {
        updateRatingLabel(binding.rbRating.getRating());
        binding.rbRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> updateRatingLabel(rating));
    }

    private void updateRatingLabel(float rating) {
        binding.tvRatingValue.setText(String.format(Locale.getDefault(), "%.1f / 5 sao", rating));
    }

    private void setupListeners() {
        binding.btnBackHeader.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveBook());
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu và lưu sách mới vào repository.
     */
    private void saveBook() {
        String title = binding.edtTitle.getText().toString().trim();
        String author = binding.edtAuthor.getText().toString().trim();
        String category = binding.spCategory.getSelectedItem() != null ?
                binding.spCategory.getSelectedItem().toString() : "Khác";

        // Xác định tình trạng đọc từ RadioGroup
        String status = Book.STATUS_UNREAD;
        int checkedId = binding.rgStatus.getCheckedRadioButtonId();
        if (checkedId == R.id.rbRead) {
            status = Book.STATUS_READ;
        } else if (checkedId == R.id.rbReading) {
            status = Book.STATUS_READING;
        }

        float rating = binding.rbRating.getRating();
        String note = binding.edtNote.getText().toString().trim();

        // Kiểm tra dữ liệu bắt buộc không được để trống
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(author)) {
            binding.edtTitle.setError(getString(R.string.error_empty_title));
            binding.edtAuthor.setError(getString(R.string.error_empty_author));
            Toast.makeText(this, R.string.error_empty_field, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title)) {
            binding.edtTitle.setError(getString(R.string.error_empty_title));
            binding.edtTitle.requestFocus();
            Toast.makeText(this, R.string.error_empty_title, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(author)) {
            binding.edtAuthor.setError(getString(R.string.error_empty_author));
            binding.edtAuthor.requestFocus();
            Toast.makeText(this, R.string.error_empty_author, Toast.LENGTH_SHORT).show();
            return;
        }

        Book newBook = new Book(title, author, category, status, rating, note);
        repository.addBook(newBook);

        Toast.makeText(this, R.string.add_book_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
