package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.Adapter.StudentAdapter;
import com.example.quanlysinhvien_gk.model.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DiemTheoChungChi extends AppCompatActivity {

    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private List<Student> studentList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diem_theo_chung_chi);

        rvStudents = findViewById(R.id.score_student_list_view);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        studentList = new ArrayList<>();

        String certificateName = getIntent().getStringExtra("certificateName");
        if (certificateName != null) {
            loadStudents(certificateName);
        } else {
            Toast.makeText(this, "Không tìm thấy chứng chỉ!", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.button_add).setOnClickListener(v -> {
            Intent intent = new Intent(DiemTheoChungChi.this, ThemDiemChungChiChoSinhVien.class);
            startActivity(intent);
        });

    }

    private void loadStudents(String certificateName) {
        db.collection("Certificates")
                .whereEqualTo("loaiChungChi", certificateName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getString("id");
                        String name = document.getString("tenSinhVien");
                        String score = document.getString("diem");

                        studentList.add(new Student(id, name, score));
                    }
                    adapter = new StudentAdapter(studentList, student -> {
                        Intent intent = new Intent(DiemTheoChungChi.this, ChinhSuaChungChi.class);
                        intent.putExtra("studentId", student.getId());
                        intent.putExtra("studentName", student.getName());
                        intent.putExtra("certificateType", certificateName); // Tên chứng chỉ
                        intent.putExtra("currentScore", student.getScore());
                        startActivity(intent);
                    });
                    rvStudents.setAdapter(adapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải danh sách sinh viên!", Toast.LENGTH_SHORT).show());
    }
}
