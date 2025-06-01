package com.example.quanlysinhvien_gk;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemDiemChungChiChoSinhVien extends AppCompatActivity {

    private Spinner spinnerCertificateType;
    private EditText etStudentId, etStudentName, etStudentScore;
    private Button btnSave, btnCancel;
    private FirebaseFirestore db;
    private List<String> certificateTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_diem_chung_chi_cho_sinh_vien);

        // Ánh xạ View
        spinnerCertificateType = findViewById(R.id.tv_score_student_edit_subject);
        etStudentId = findViewById(R.id.tv_score_student_edit_student_id);
        etStudentName = findViewById(R.id.tv_score_student_edit_student_name);
        etStudentScore = findViewById(R.id.edit_text_score_student_edit_score);
        btnSave = findViewById(R.id.button_scoreave_student_s);
        btnCancel = findViewById(R.id.button_Huy);

        db = FirebaseFirestore.getInstance();
        certificateTypes = new ArrayList<>();

        // Load các loại chứng chỉ vào Spinner
        loadCertificateTypes();

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> saveCertificate());

        // Xử lý nút Hủy
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadCertificateTypes() {
        db.collection("Certificates")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String certificateType = document.getString("loaiChungChi");
                        if (certificateType != null && !certificateTypes.contains(certificateType)) {
                            certificateTypes.add(certificateType);
                        }
                    }
                    // Đưa dữ liệu vào Spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, certificateTypes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCertificateType.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải chứng chỉ!", Toast.LENGTH_SHORT).show());
    }

    private void saveCertificate() {
        String selectedCertificate = spinnerCertificateType.getSelectedItem().toString();
        String studentId = etStudentId.getText().toString().trim();
        String studentName = etStudentName.getText().toString().trim();
        String studentScore = etStudentScore.getText().toString().trim();

        if (selectedCertificate.isEmpty() || studentId.isEmpty() || studentName.isEmpty() || studentScore.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo mới dữ liệu chứng chỉ trong Firestore
        Map<String, Object> certificateData = new HashMap<>();
        certificateData.put("loaiChungChi", selectedCertificate);
        certificateData.put("id", studentId);
        certificateData.put("tenSinhVien", studentName);
        certificateData.put("diem", studentScore);

        db.collection("Certificates")
                .add(certificateData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm chứng chỉ thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại Activity trước
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi thêm chứng chỉ!", Toast.LENGTH_SHORT).show());
    }
}

