package com.example.quanlysinhvien_gk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlysinhvien_gk.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SuaThongTinNguoiDung extends AppCompatActivity {
    private EditText etHo, etTen, etTuoi, etSoDienThoai;
    private RadioGroup rgTrangThai;
    private Button btnSave, btnDelete,btnBack;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_thong_tin_nguoi_dung);

        etHo = findViewById(R.id.edit_Ho_NguoiDung);
        etTen = findViewById(R.id.edit_ten_NguoiDung);
        etTuoi = findViewById(R.id.edit_Tuoi_NguoiDung);
        etSoDienThoai = findViewById(R.id.editTextPhone_NguoiDung);
        rgTrangThai = findViewById(R.id.radioTrangThai);
        btnSave = findViewById(R.id.ButtonUpdate_NguoiDung);
        btnDelete = findViewById(R.id.ButtonDeleteNguoiDung);
        btnBack = findViewById(R.id.ButtonGoBack_DSND);

        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu từ Intent
        userId = getIntent().getStringExtra("id");
        String ho = getIntent().getStringExtra("ho");
        String ten = getIntent().getStringExtra("ten");
        String tuoi = getIntent().getStringExtra("tuoi"); // Tuổi là String
        String soDienThoai = getIntent().getStringExtra("soDienThoai");
        boolean trangThai = getIntent().getBooleanExtra("trangThai", true);

        // Hiển thị dữ liệu lên giao diện
        etHo.setText(ho);
        etTen.setText(ten);
        etTuoi.setText(tuoi);
        etSoDienThoai.setText(soDienThoai);
        if (trangThai) {
            rgTrangThai.check(R.id.RadioButtonBinhThuong);
        } else {
            rgTrangThai.check(R.id.RadioButtonDaKhoa);
        }

        btnSave.setOnClickListener(v -> updateUser());
        btnDelete.setOnClickListener(v -> deleteUser());
        btnBack.setOnClickListener(view -> finish());
    }



    private void updateUser() {
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("ho", etHo.getText().toString());
        updatedUser.put("ten", etTen.getText().toString());
        updatedUser.put("tuoi", etTuoi.getText().toString()); // Tuổi là String
        updatedUser.put("soDienThoai", etSoDienThoai.getText().toString());
        updatedUser.put("trangThai", rgTrangThai.getCheckedRadioButtonId() == R.id.RadioButtonBinhThuong);

        db.collection("User").document(userId).set(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }


    private void deleteUser() {
        db.collection("User").document(userId).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}

