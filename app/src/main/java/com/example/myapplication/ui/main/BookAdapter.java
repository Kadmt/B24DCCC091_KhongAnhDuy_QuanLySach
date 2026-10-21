package com.example.myapplication.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Book;
import com.example.myapplication.databinding.ItemBookBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter quản lý hiển thị danh sách sách trong RecyclerView.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private List<Book> bookList;
    private final OnBookClickListener listener;

    public BookAdapter(List<Book> bookList, OnBookClickListener listener) {
        this.bookList = (bookList != null) ? new ArrayList<>(bookList) : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<Book> newBooks) {
        this.bookList = (newBooks != null) ? new ArrayList<>(newBooks) : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBookBinding binding = ItemBookBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bind(book, listener);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        private final ItemBookBinding binding;

        public BookViewHolder(@NonNull ItemBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Book book, final OnBookClickListener listener) {
            Context context = itemView.getContext();

            binding.tvTitle.setText(book.getTitle());
            binding.tvAuthor.setText(context.getString(R.string.author_prefix) + book.getAuthor());
            binding.tvCategory.setText(book.getCategory());
            binding.tvStatus.setText(book.getReadingStatus());

            // Định dạng màu sắc và badge tùy theo tình trạng đọc
            if (Book.STATUS_READ.equalsIgnoreCase(book.getReadingStatus())) {
                binding.tvStatus.setBackgroundResource(R.drawable.bg_status_read);
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_read_text));
            } else if (Book.STATUS_READING.equalsIgnoreCase(book.getReadingStatus())) {
                binding.tvStatus.setBackgroundResource(R.drawable.bg_status_reading);
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_reading_text));
            } else {
                binding.tvStatus.setBackgroundResource(R.drawable.bg_status_unread);
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.status_unread_text));
            }

            // Đánh giá sao
            binding.rbRating.setRating(book.getRating());
            binding.tvRatingText.setText(String.format(Locale.getDefault(), "%.1f", book.getRating()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBookClick(book);
                }
            });
        }
    }
}
