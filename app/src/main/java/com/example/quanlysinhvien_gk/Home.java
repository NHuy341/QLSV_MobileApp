package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Home extends AppCompatActivity {

    private CardView btnClassList, btnUserList, btnLoginHistory, btnCertificates, btnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        btnClassList = findViewById(R.id.btn_DanhSachLop);
        btnUserList = findViewById(R.id.btn_DanhSachNguoiDung);
        btnLoginHistory = findViewById(R.id.btn_LichSuDangNhap);
        btnCertificates = findViewById(R.id.btn_ChungChi);
        btnAccount = findViewById(R.id.btn_TaiKhoan);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString("userRole", null);
        String userName = sharedPreferences.getString("userName", "Tên Người Dùng");
        String userID = sharedPreferences.getString("userID", "Mã Người Dùng");

        TopBarMenuIconFragment topBarFragment = (TopBarMenuIconFragment) getSupportFragmentManager().findFragmentById(R.id.topBar);
        if (topBarFragment != null) {
            topBarFragment.updateUserInfo(userName, userID);
        }

        // Kiểm tra nếu userRole là null
        if (userRole == null) {
            Log.e("HomeActivity", "userRole is null! Redirecting to Login.");
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        // Ẩn tất cả các nút trước
        btnClassList.setVisibility(View.GONE);
        btnUserList.setVisibility(View.GONE);
        btnLoginHistory.setVisibility(View.GONE);
        btnCertificates.setVisibility(View.GONE);
        btnAccount.setVisibility(View.GONE);

        // Hiển thị các nút theo vai trò
        if (userRole.equals("Admin")) {
            btnClassList.setVisibility(View.VISIBLE);
            btnUserList.setVisibility(View.VISIBLE);
            btnLoginHistory.setVisibility(View.VISIBLE);
            btnCertificates.setVisibility(View.VISIBLE);
            btnAccount.setVisibility(View.VISIBLE);
        } else if (userRole.equals("User")) {
            btnClassList.setVisibility(View.VISIBLE);
            btnCertificates.setVisibility(View.VISIBLE);
            btnAccount.setVisibility(View.VISIBLE);
        } else if (userRole.equals("Student")) {
            btnClassList.setVisibility(View.VISIBLE);
            btnAccount.setVisibility(View.VISIBLE);
        }

        // Sự kiện khi bấm vào các CardView
        btnClassList.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, DanhSachSinhVien.class);
            startActivity(intent);
        });

        btnUserList.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, DanhSachNguoiDung.class);
            startActivity(intent);
        });

        btnLoginHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, LichSuDangNhap.class);
            startActivity(intent);
        });

        btnCertificates.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, ChungChi.class);
            startActivity(intent);
        });

        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, CaiDat.class);
            startActivity(intent);
        });
    }
}