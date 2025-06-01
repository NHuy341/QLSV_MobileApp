package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.Adapter.LoginHistoryAdapter;
import com.example.quanlysinhvien_gk.model.LoginHistory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LichSuDangNhap extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LoginHistoryAdapter adapter;
    private List<LoginHistory> loginHistoryList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_dang_nhap);

        recyclerView = findViewById(R.id.ListViewLSDN);
        Button btnBack = findViewById(R.id.btn_quaylai);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        loginHistoryList = new ArrayList<>();
        adapter = new LoginHistoryAdapter(loginHistoryList);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load data from Firestore
        db.collection("HisLogin").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userID = document.getString("userID");
                    long timestamp = document.getLong("timestamp");
                    loginHistoryList.add(new LoginHistory(userID, timestamp));
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Back button
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(LichSuDangNhap.this, Home.class);
            startActivity(intent);
            finish();
        });
    }
}

