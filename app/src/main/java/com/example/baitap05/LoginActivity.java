package com.example.baitap05;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baitap05.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etFullName;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isRegisterMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Mặc định ẩn trường Họ tên (chỉ hiện khi Đăng ký)
        etFullName.setVisibility(View.GONE);

        // Nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            if (isRegisterMode) {
                // Đang ở mode đăng ký, nhấn nút này để quay lại mode đăng nhập
                isRegisterMode = false;
                etFullName.setVisibility(View.GONE);
                btnLogin.setText("ĐĂNG NHẬP");
                btnRegister.setText("TẠO TÀI KHOẢN MỚI");
                return;
            }

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            goToMain();
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Nút Đăng ký
        btnRegister.setOnClickListener(v -> {
            if (!isRegisterMode) {
                // Chuyển sang mode đăng ký
                isRegisterMode = true;
                etFullName.setVisibility(View.VISIBLE);
                btnLogin.setText("QUAY LẠI ĐĂNG NHẬP");
                btnRegister.setText("XÁC NHẬN ĐĂNG KÝ");
                return;
            }

            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            User user = new User(uid, email, name);
                            
                            db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        goToMain();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Lưu user thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        
        if (mAuth.getCurrentUser() != null) {
            goToMain();
        }
    }

    private void goToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
