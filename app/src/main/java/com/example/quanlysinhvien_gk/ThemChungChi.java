package com.example.quanlysinhvien_gk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ThemChungChi extends AppCompatActivity {

    private EditText etCertificateType;
    private Button btnAddCertificateType, btnBack;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_chung_chi);

        // Ánh xạ View
        etCertificateType = findViewById(R.id.textChungChi);
        btnAddCertificateType = findViewById(R.id.btn_ThemChungChi);
        btnBack = findViewById(R.id.btn_QuayLai);

        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Xử lý nút Thêm
        btnAddCertificateType.setOnClickListener(v -> addCertificateType());

        // Xử lý nút Quay Lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void addCertificateType() {
        String certificateType = etCertificateType.getText().toString().trim();

        if (certificateType.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập loại chứng chỉ!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra và thêm loại chứng chỉ vào Firestore
        db.collection("Certificates")
                .whereEqualTo("loaiChungChi", certificateType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // Nếu loại chứng chỉ chưa tồn tại, thêm mới
                        Map<String, Object> data = new HashMap<>();
                        data.put("loaiChungChi", certificateType);
                        db.collection("Certificates")
                                .add(data)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Thêm loại chứng chỉ thành công!", Toast.LENGTH_SHORT).show();
                                    finish(); // Quay lại Activity trước
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm loại chứng chỉ!", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Loại chứng chỉ đã tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi kết nối Firestore!", Toast.LENGTH_SHORT).show());
    }
}
