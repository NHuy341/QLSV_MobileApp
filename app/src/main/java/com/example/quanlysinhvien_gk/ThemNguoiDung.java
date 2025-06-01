package com.example.quanlysinhvien_gk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ThemNguoiDung extends AppCompatActivity {
    private EditText etHo, etTen, etTuoi, etSoDienThoai;
    private RadioGroup rgTrangThai;
    private Button btnAddUser, btnGoBack;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nguoi_dung);

        // Ánh xạ view
        etHo = findViewById(R.id.Ho_NguoiDungMoi);
        etTen = findViewById(R.id.Ten_NguoiDungMoi);
        etTuoi = findViewById(R.id.edit_Tuoi_NguoiDungMoi);
        etSoDienThoai = findViewById(R.id.editTextPhone_NguoiDungMoi);
        rgTrangThai = findViewById(R.id.trangThaiMoi);
        btnAddUser = findViewById(R.id.btn_ThemNguoiDungMoi);
        btnGoBack = findViewById(R.id.btn_QuayLai);

        db = FirebaseFirestore.getInstance();

        // Xử lý nút "Thêm Người Dùng"
        btnAddUser.setOnClickListener(v -> addUser());

        // Xử lý nút "Quay Lại"
        btnGoBack.setOnClickListener(v -> finish());
    }

    private void addUser() {
        String ho = etHo.getText().toString();
        String ten = etTen.getText().toString();
        String tuoi = etTuoi.getText().toString();
        String soDienThoai = etSoDienThoai.getText().toString();
        boolean trangThai = rgTrangThai.getCheckedRadioButtonId() == R.id.button_BinhThuong;

        if (ho.isEmpty() || ten.isEmpty() || tuoi.isEmpty() || soDienThoai.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Bước 1: Lấy tất cả các ID hiện tại trong Firestore
        db.collection("User").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxId = 0;

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String currentId = doc.getId();
                        if (currentId.startsWith("GV_")) {
                            try {
                                int numericPart = Integer.parseInt(currentId.substring(3));
                                if (numericPart > maxId) {
                                    maxId = numericPart;
                                }
                            } catch (NumberFormatException e) {
                                // Ignore invalid IDs
                            }
                        }
                    }

                    // Bước 2: Tạo ID mới
                    int newId = maxId + 1;
                    String generatedId = String.format("GV_%08d", newId);

                    // Bước 3: Thêm người dùng mới với ID vừa tạo
                    Map<String, Object> newUser = new HashMap<>();
                    newUser.put("ho", ho);
                    newUser.put("ten", ten);
                    newUser.put("tuoi", tuoi);
                    newUser.put("soDienThoai", soDienThoai);
                    newUser.put("trangThai", trangThai);

                    db.collection("User").document(generatedId).set(newUser)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Thêm người dùng thành công!", Toast.LENGTH_SHORT).show();
                                finish(); // Quay lại Activity trước đó
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi thêm người dùng!", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy danh sách người dùng!", Toast.LENGTH_SHORT).show();
                });
    }
}
