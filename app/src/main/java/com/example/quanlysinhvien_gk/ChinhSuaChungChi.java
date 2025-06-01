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

public class ChinhSuaChungChi extends AppCompatActivity {

    private EditText etCertificateType, etStudentId, etStudentName, etStudentScore;
    private Button btnSave,btnCancel;
    private FirebaseFirestore db;
    private String studentId, certificateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_chung_chi);

        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        etCertificateType = findViewById(R.id.tv_score_student_edit_subject);
        etStudentId = findViewById(R.id.tv_score_student_edit_student_id);
        etStudentName = findViewById(R.id.tv_score_student_edit_student_name);
        etStudentScore = findViewById(R.id.edit_text_score_student_edit_score);
        btnSave = findViewById(R.id.button_scoreave_student_s);
        btnCancel = findViewById(R.id.button_Huy);


        // Lấy dữ liệu từ Intent
        certificateType = getIntent().getStringExtra("certificateType");
        studentId = getIntent().getStringExtra("studentId");
        String studentName = getIntent().getStringExtra("studentName");
        String currentScore = getIntent().getStringExtra("currentScore");

        // Hiển thị dữ liệu
        etCertificateType.setText(certificateType);
        etStudentId.setText(studentId);
        etStudentName.setText(studentName);
        etStudentScore.setText(currentScore);

        // Lưu thay đổi
        btnSave.setOnClickListener(v -> saveChanges());

        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveChanges() {
        String newScore = etStudentScore.getText().toString().trim();
        if (newScore.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật điểm trong Firestore
        db.collection("Certificates")
                .whereEqualTo("id", studentId)
                .whereEqualTo("loaiChungChi", certificateType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference()
                                .update("diem", newScore)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tìm dữ liệu!", Toast.LENGTH_SHORT).show());
    }
}
