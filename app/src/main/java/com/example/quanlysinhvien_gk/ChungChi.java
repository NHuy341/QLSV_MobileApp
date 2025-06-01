package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.Adapter.CertificateAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChungChi extends AppCompatActivity {

    private RecyclerView rvCertificates;
    private CertificateAdapter adapter;
    private List<String> certificateList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chung_chi);

        rvCertificates = findViewById(R.id.listChungChi);
        rvCertificates.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        certificateList = new ArrayList<>();

        loadCertificates();

        findViewById(R.id.classroomButtonCreation).setOnClickListener(v -> {
            Intent intent = new Intent(ChungChi.this, ThemChungChi.class);
            startActivity(intent);
        });
    }

    private void loadCertificates() {
        db.collection("Certificates")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Set<String> uniqueCertificates = new HashSet<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String certificateType = document.getString("loaiChungChi");
                        if (certificateType != null) {
                            uniqueCertificates.add(certificateType);
                        }
                    }
                    certificateList.addAll(uniqueCertificates);
                    adapter = new CertificateAdapter(certificateList, certificateName -> {
                        Intent intent = new Intent(ChungChi.this, DiemTheoChungChi.class);
                        intent.putExtra("certificateName", certificateName);
                        startActivity(intent);
                    });
                    rvCertificates.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải chứng chỉ!", Toast.LENGTH_SHORT).show();
                });
    }

}
