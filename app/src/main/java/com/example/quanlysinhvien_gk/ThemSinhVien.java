package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ThemSinhVien extends AppCompatActivity {

    private EditText edtHo, edtTen, edtTuoi, edtSoDienThoai;
    private Button btnThemSinhVien, btnHuy;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sinh_vien);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Tên Người Dùng");
        String userID = sharedPreferences.getString("userID", "Mã Người Dùng");

        // Tìm Fragment và truyền dữ liệu
        TopBarHomeIconFragment topBarFragment = (TopBarHomeIconFragment) getSupportFragmentManager().findFragmentById(R.id.topBar);
        if (topBarFragment != null) {
            topBarFragment.updateUserInfo(userName, userID);
        }

        edtHo = findViewById(R.id.edtHo);
        edtTen = findViewById(R.id.edtTen);
        edtTuoi = findViewById(R.id.edtTuoi);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnThemSinhVien = findViewById(R.id.btn_ThemSV);
        btnHuy = findViewById(R.id.btn_HuythemSV);

        db = FirebaseFirestore.getInstance();

        btnThemSinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ho = edtHo.getText().toString();
                String ten = edtTen.getText().toString();
                String tuoi = edtTuoi.getText().toString();
                String soDienThoai = edtSoDienThoai.getText().toString();

                if (ho.isEmpty() || ten.isEmpty() || tuoi.isEmpty() || soDienThoai.isEmpty()) {
                    Toast.makeText(ThemSinhVien.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy ID sinh viên mới
                getNextStudentId((newId) -> {
                    // Tạo dữ liệu để lưu vào Firestore
                    Map<String, Object> sinhVien = new HashMap<>();
                    sinhVien.put("id", newId);
                    sinhVien.put("ho", ho);
                    sinhVien.put("ten", ten);
                    sinhVien.put("tuoi", tuoi);
                    sinhVien.put("soDienThoai", soDienThoai);

                    // Thêm dữ liệu vào Firestore
                    db.collection("Student").document(newId).set(sinhVien)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ThemSinhVien.this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Quay lại trang trước
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ThemSinhVien.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
            }
        });

        btnHuy.setOnClickListener(v -> finish()); // Quay lại trang trước
    }

    // Hàm lấy ID sinh viên mới
    private void getNextStudentId(OnIdGeneratedListener listener) {
        db.collection("Student").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxId = 0;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getString("id");
                        if (id != null && id.startsWith("SV_")) {
                            try {
                                int currentId = Integer.parseInt(id.substring(3)); // Bỏ "SV_" và lấy số
                                if (currentId > maxId) {
                                    maxId = currentId;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // Tạo ID mới với số lớn nhất + 1
                    int newId = maxId + 1;
                    String formattedId = String.format("SV_%08d", newId); // SV_00000001
                    listener.onIdGenerated(formattedId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy ID mới: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Interface để trả về ID mới
    private interface OnIdGeneratedListener {
        void onIdGenerated(String newId);
    }
}

