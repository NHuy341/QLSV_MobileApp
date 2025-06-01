package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.txtUsername);
        etPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnSignIn);

        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Xác định collection
                String prefix = username.substring(0, 2);
                String userRole;
                String collection;
                if (prefix.equals("GV")) {
                    collection = "User";
                    userRole = "User";
                } else if (prefix.equals("SV")) {
                    collection = "Student";
                    userRole = "Student";
                } else if (prefix.equals("AD")) {
                    collection = "Admin";
                    userRole = "Admin";
                } else {
                    Toast.makeText(Login.this, "Tài khoản không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("LoginActivity", "Detected userRole: " + userRole);

                // Kiểm tra thông tin đăng nhập
                db.collection(collection).document(username).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String storedPassword = document.getString("soDienThoai");
                                    if (storedPassword != null && storedPassword.equals(password)) {
                                        String userName = document.getString("ho") + " " + document.getString("ten");
                                        String userID = username;

                                        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("userName", userName);
                                        editor.putString("userID", userID);
                                        editor.putString("userRole", userRole);
                                        editor.apply();

                                        // Lưu thông tin lịch sử đăng nhập vào Firestore
                                        Map<String, Object> loginData = new HashMap<>();
                                        loginData.put("userID", username);
                                        loginData.put("timestamp", System.currentTimeMillis()); // Thời gian hiện tại (millisecond)

                                        db.collection("HisLogin").add(loginData)
                                                .addOnSuccessListener(documentReference -> Log.d("LoginActivity", "Login history added"))
                                                .addOnFailureListener(e -> Log.e("LoginActivity", "Error adding login history", e));


                                        // Chuyển sang Home
                                        Intent intent = new Intent(Login.this, Home.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(Login.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Login.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}