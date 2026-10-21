package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.ui.main.MainActivity;

/**
 * Màn hình Đăng nhập (Màn hình khởi chạy chính).
 * Xử lý xác thực tài khoản người dùng trước khi truy cập danh sách chi tiêu.
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản mẫu chuẩn theo yêu cầu đề bài
    private static final String SAMPLE_USERNAME = "admin";
    private static final String SAMPLE_PASSWORD = "123456";

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListeners();
    }

    /**
     * Thiết lập các sự kiện lắng nghe tương tác người dùng.
     */
    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    /**
     * Kiểm tra thông tin đăng nhập và xử lý điều hướng.
     */
    private void attemptLogin() {
        // Ẩn thông báo lỗi trước đó
        binding.tvErrorMessage.setVisibility(View.GONE);

        String username = binding.edtUsername.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (validateCredentials(username, password)) {
            navigateToMain();
        } else {
            showLoginError();
        }
    }

    /**
     * Kiểm tra tính hợp lệ của tài khoản mẫu.
     */
    private boolean validateCredentials(String username, String password) {
        return SAMPLE_USERNAME.equals(username) && SAMPLE_PASSWORD.equals(password);
    }

    /**
     * Hiển thị dòng thông báo lỗi màu đỏ.
     */
    private void showLoginError() {
        binding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * Chuyển sang MainActivity và đóng LoginActivity.
     */
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
