package com.example.myapplication.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Lớp Book biểu diễn thông tin một cuốn sách trong thư viện cá nhân.
 * Hỗ trợ cả Serializable và Parcelable để đảm bảo tương thích truyền dữ liệu qua Intent.
 */
public class Book implements Serializable, Parcelable {

    public static final String STATUS_UNREAD = "Chưa đọc";
    public static final String STATUS_READING = "Đang đọc";
    public static final String STATUS_READ = "Đã đọc";

    private long id;
    private String title;
    private String author;
    private String category;
    private String readingStatus;
    private float rating;
    private String note;

    public Book() {
        this.readingStatus = STATUS_UNREAD;
        this.rating = 0.0f;
    }

    public Book(String title, String author, String category, String readingStatus, float rating, String note) {
        this(System.currentTimeMillis(), title, author, category, readingStatus, rating, note);
    }

    public Book(long id, String title, String author, String category, String readingStatus, float rating, String note) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.readingStatus = (readingStatus != null && !readingStatus.isEmpty()) ? readingStatus : STATUS_UNREAD;
        this.rating = rating;
        this.note = (note != null) ? note : "";
    }

    // --- Parcelable Implementation ---

    protected Book(Parcel in) {
        id = in.readLong();
        title = in.readString();
        author = in.readString();
        category = in.readString();
        readingStatus = in.readString();
        rating = in.readFloat();
        note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(category);
        dest.writeString(readingStatus);
        dest.writeFloat(rating);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // --- Getters & Setters ---

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isRead() {
        return STATUS_READ.equalsIgnoreCase(this.readingStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                Float.compare(book.rating, rating) == 0 &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(category, book.category) &&
                Objects.equals(readingStatus, book.readingStatus) &&
                Objects.equals(note, book.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, category, readingStatus, rating, note);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", readingStatus='" + readingStatus + '\'' +
                ", rating=" + rating +
                ", note='" + note + '\'' +
                '}';
    }
}
