package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ChinhSuaThongTinSinhVien extends AppCompatActivity {
    private EditText edtHo, edtTen, edtTuoi, edtSoDienThoai;
    private Button btnSua, btnXoa,btnBack;
    private FirebaseFirestore db;
    private String id,currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_thong_tin_sinh_vien);

        edtHo = findViewById(R.id.edit_Ho);
        edtTen = findViewById(R.id.edit_ten);
        edtTuoi = findViewById(R.id.edit_Tuoi2);
        edtSoDienThoai = findViewById(R.id.editTextPhone2);
        btnSua = findViewById(R.id.individualButtonUpdate);
        btnXoa = findViewById(R.id.ButtonDeleteSinhVien);
        btnBack = findViewById(R.id.ButtonGoBack_DSSV);

        db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu từ intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        edtHo.setText(intent.getStringExtra("ho"));
        edtTen.setText(intent.getStringExtra("ten"));
        edtTuoi.setText(intent.getStringExtra("tuoi"));
        edtSoDienThoai.setText(intent.getStringExtra("soDienThoai"));

        currentUserID = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("userID", null);

        // Ẩn nút Sửa nếu người dùng là SV
        if (currentUserID != null && currentUserID.startsWith("SV")) {
            btnSua.setVisibility(View.GONE);
            btnXoa.setVisibility(View.GONE);
        }

        // Xử lý sửa
        btnSua.setOnClickListener(v -> {
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("ho", edtHo.getText().toString());
            updatedData.put("ten", edtTen.getText().toString());
            updatedData.put("tuoi", edtTuoi.getText().toString());
            updatedData.put("soDienThoai", edtSoDienThoai.getText().toString());

            db.collection("Student").document(id).update(updatedData).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại trang trước
            });
        });

        // Xử lý xóa
        btnXoa.setOnClickListener(v -> {
            db.collection("Student").document(id).delete().addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại trang trước
            });
        });

        // Quay lại danh sách sinh viên
        btnBack.setOnClickListener(view -> finish());
    }
}

