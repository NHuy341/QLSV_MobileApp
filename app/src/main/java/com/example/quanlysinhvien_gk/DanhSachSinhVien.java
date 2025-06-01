package com.example.quanlysinhvien_gk;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.Adapter.SinhVienAdapter;
import com.example.quanlysinhvien_gk.model.SinhVien;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSachSinhVien extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SinhVienAdapter adapter;
    private List<SinhVien> sinhVienList = new ArrayList<>();
    private List<SinhVien> filteredList = new ArrayList<>();
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_sinh_vien);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", "Tên Người Dùng");
        String userID = sharedPreferences.getString("userID", "Mã Người Dùng");

        // Tìm Fragment và truyền dữ liệu
        TopBarHomeIconFragment topBarFragment = (TopBarHomeIconFragment) getSupportFragmentManager().findFragmentById(R.id.topBar);
        if (topBarFragment != null) {
            topBarFragment.updateUserInfo(userName, userID);
        }

        recyclerView = findViewById(R.id.recycler_view_students);
        searchView = findViewById(R.id.studentSearchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SinhVienAdapter(this, filteredList );
        recyclerView.setAdapter(adapter);

        if (userID.startsWith("SV")) {
            findViewById(R.id.Button_themSV).setVisibility(Button.GONE); // Ẩn nút
        }

        db = FirebaseFirestore.getInstance();
        loadSinhVien();

        // Xử lý nút Thêm Sinh Viên
        findViewById(R.id.Button_themSV).setOnClickListener(v -> {
            // Hiển thị Dialog chọn "Thủ công" hoặc "Bằng tệp"
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm Sinh Viên")
                    .setItems(new String[]{"Thủ công", "Bằng tệp"}, (dialog, which) -> {
                        if (which == 0) {
                            // Thêm thủ công
                            Intent intent = new Intent(DanhSachSinhVien.this, ThemSinhVien.class);
                            startActivity(intent);
                        } else if (which == 1) {
                            // Thêm bằng tệp
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/vnd.ms-excel"); // Định dạng file Excel
                            startActivityForResult(Intent.createChooser(intent, "Chọn tệp Excel"), 100);
                        }
                    })
                    .show();
        });

        findViewById(R.id.studentButtonExport).setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachSinhVien.this, XuatDanhSachSinhVien.class);
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSinhVien(query); // Lọc dữ liệu khi nhấn submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSinhVien(newText); // Lọc dữ liệu khi thay đổi nội dung
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSinhVien(); // Gọi lại hàm này mỗi khi Activity được mở lại
    }

    private void loadSinhVien() {
        db.collection("Student").get().addOnSuccessListener(queryDocumentSnapshots -> {
            sinhVienList.clear(); // Xóa danh sách cũ
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                SinhVien sv = doc.toObject(SinhVien.class);
                sv.setId(doc.getId());
                sinhVienList.add(sv);
            }
            filteredList.clear();
            filteredList.addAll(sinhVienList); // Hiển thị toàn bộ danh sách ban đầu
            adapter.notifyDataSetChanged();
        });
    }
    private void filterSinhVien(String query) {
        query = query.toLowerCase();
        filteredList.clear(); // Xóa danh sách cũ
        for (SinhVien sv : sinhVienList) {
            if (sv.getHo().toLowerCase().contains(query) ||
                    sv.getTen().toLowerCase().contains(query) ||
                    sv.getId().toLowerCase().contains(query) ||
                    sv.getSoDienThoai().toLowerCase().contains(query)) {
                filteredList.add(sv); // Thêm sinh viên khớp với từ khóa
            }
        }
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                processFile(fileUri); // Xử lý tệp Excel
            }
        }
    }

    // Hàm xử lý tệp
    private void processFile(Uri fileUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            if (inputStream != null) {
                // Đọc dữ liệu từ tệp Excel
                readExcelFile(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi đọc tệp: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void readExcelFile(InputStream inputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            List<Map<String, Object>> sinhVienListFromFile = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    // Bỏ qua dòng tiêu đề
                    continue;
                }

                String id = "SV_" + String.format("%08d", (int) row.getCell(0).getNumericCellValue());
                String ho = row.getCell(1).getStringCellValue();
                String ten = row.getCell(2).getStringCellValue();
                String tuoi = String.valueOf((int) row.getCell(3).getNumericCellValue());
                String soDienThoai = row.getCell(4).getStringCellValue();

                Map<String, Object> sinhVien = new HashMap<>();
                sinhVien.put("id", id);
                sinhVien.put("ho", ho);
                sinhVien.put("ten", ten);
                sinhVien.put("tuoi", tuoi);
                sinhVien.put("soDienThoai", soDienThoai);

                sinhVienListFromFile.add(sinhVien);
            }

            // Lưu vào Firestore
            saveStudentsToFirestore(sinhVienListFromFile);

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xử lý tệp: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm lưu danh sách sinh viên vào Firestore
    private void saveStudentsToFirestore(List<Map<String, Object>> sinhVienList) {
        for (Map<String, Object> sinhVien : sinhVienList) {
            String id = (String) sinhVien.get("id");
            db.collection("Student").document(id).set(sinhVien)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Thêm sinh viên từ tệp thành công!", Toast.LENGTH_SHORT).show();
                        loadSinhVien(); // Load lại danh sách
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi thêm sinh viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
