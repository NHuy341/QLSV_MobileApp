package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.Adapter.UserAdapter;
import com.example.quanlysinhvien_gk.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DanhSachNguoiDung extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private List<User> filteredList = new ArrayList<>();
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nguoi_dung);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Tên Người Dùng");
        String userID = sharedPreferences.getString("userID", "Mã Người Dùng");

        // Tìm Fragment và truyền dữ liệu
        TopBarHomeIconFragment topBarFragment = (TopBarHomeIconFragment) getSupportFragmentManager().findFragmentById(R.id.topBar);
        if (topBarFragment != null) {
            topBarFragment.updateUserInfo(userName, userID);
        }

        searchView = findViewById(R.id.userSearchView);
        recyclerView = findViewById(R.id.userRecycleView);
        Button btnAddUser = findViewById(R.id.Button_themUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (userID.startsWith("SV")) {
            btnAddUser.setVisibility(Button.GONE); // Ẩn nút
        }

        db = FirebaseFirestore.getInstance();
        fetchUsers();

        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachNguoiDung.this, ThemNguoiDung.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return false;
            }
        });
    }

    private void fetchUsers() {
        db.collection("User").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setId(doc.getId());
                            userList.add(user);
                        }
                    }
                    filteredList.clear();
                    filteredList.addAll(userList); // Sao chép toàn bộ dữ liệu
                    userAdapter = new UserAdapter(filteredList, user -> {
                        Intent intent = new Intent(DanhSachNguoiDung.this, SuaThongTinNguoiDung.class);
                        intent.putExtra("id", user.getId());
                        intent.putExtra("ho", user.getHo());
                        intent.putExtra("ten", user.getTen());
                        intent.putExtra("tuoi", user.getTuoi());
                        intent.putExtra("soDienThoai", user.getSoDienThoai());
                        intent.putExtra("trangThai", user.getTrangThai());
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(userAdapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(DanhSachNguoiDung.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show()
                );
    }

    private void filterUsers(String query) {
        if (TextUtils.isEmpty(query)) {
            filteredList.clear();
            filteredList.addAll(userList); // Hiển thị toàn bộ danh sách nếu không có tìm kiếm
        } else {
            filteredList.clear();
            for (User user : userList) {
                if (user.getHo().toLowerCase().contains(query.toLowerCase()) ||
                        user.getTen().toLowerCase().contains(query.toLowerCase()) ||
                        user.getId().toLowerCase().contains(query.toLowerCase()) ||
                        user.getSoDienThoai().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged(); // Cập nhật lại adapter
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetchUsers(); // Tải lại dữ liệu khi quay lại Activity chính
    }
}

