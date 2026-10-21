package com.example.myapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Book;
import com.example.myapplication.data.repository.BookRepository;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.add.AddBookActivity;
import com.example.myapplication.ui.detail.BookDetailActivity;

import java.util.ArrayList;

/**
 * Màn hình Chính hiển thị danh sách sách và khối thống kê 3 chỉ số đọc sách.
 */
public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookClickListener {

    public static final String EXTRA_BOOK = "extra_book";
    public static final String EXTRA_BOOK_ID = "extra_book_id";

    private ActivityMainBinding binding;
    private BookRepository repository;
    private BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRepositoryAndAdapter();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách và khối thống kê mỗi khi quay lại màn hình
        loadData();
    }

    private void initRepositoryAndAdapter() {
        repository = BookRepository.getInstance();

        adapter = new BookAdapter(null, this);
        binding.rvBooks.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBooks.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnAddBook.setOnClickListener(v -> navigateToAddBook());
    }

    /**
     * Nạp danh sách sách và cập nhật khối thống kê (Tổng / Đã đọc / Chưa đọc xong).
     */
    private void loadData() {
        ArrayList<Book> books = repository.getBooks();
        adapter.updateData(books);

        int total = repository.getTotalBooks();
        int read = repository.getReadBooksCount();
        int unfinished = repository.getUnfinishedBooksCount();

        binding.tvStatistics.setText(getString(R.string.stats_format, total, read, unfinished));

        if (books.isEmpty()) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBookClick(Book book) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK, (Parcelable) book);
        intent.putExtra(EXTRA_BOOK_ID, book.getId());
        startActivity(intent);
    }

    private void navigateToAddBook() {
        Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
        startActivity(intent);
    }
}
