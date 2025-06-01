package com.example.quanlysinhvien_gk;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysinhvien_gk.model.SinhVien;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class XuatDanhSachSinhVien extends AppCompatActivity {

    private TableLayout tableLayout;
    private Button btnXuatHinhAnh, btnXuatExcel, btnQuayLai;
    private FirebaseFirestore db;
    private List<SinhVien> sinhVienList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_danh_sach_sinh_vien);

        tableLayout = findViewById(R.id.studentTable);
        btnXuatHinhAnh = findViewById(R.id.studentExportButtonPhoto);
        btnXuatExcel = findViewById(R.id.studentExportButtonExcel);
        btnQuayLai = findViewById(R.id.studentExportButtonGoBack);

        db = FirebaseFirestore.getInstance();
        sinhVienList = new ArrayList<>();

        // Tải dữ liệu từ Firestore và hiển thị
        loadSinhVien();

        // Xử lý xuất hình ảnh
        btnXuatHinhAnh.setOnClickListener(v -> exportAsImage());

        // Xử lý xuất Excel
        btnXuatExcel.setOnClickListener(v -> exportAsExcel());

        // Xử lý quay lại
        btnQuayLai.setOnClickListener(v -> finish());
    }

    private void loadSinhVien() {
        db.collection("Student").get().addOnSuccessListener(queryDocumentSnapshots -> {
            sinhVienList.clear();
            tableLayout.removeAllViews();

            // Thêm hàng tiêu đề
            TableRow headerRow = new TableRow(this);
            headerRow.addView(createTextView("Họ"));
            headerRow.addView(createTextView("Tên"));
            headerRow.addView(createTextView("Tuổi"));
            headerRow.addView(createTextView("Số điện thoại"));
            tableLayout.addView(headerRow);

            // Thêm dữ liệu sinh viên
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                SinhVien sv = doc.toObject(SinhVien.class);
                sinhVienList.add(sv);

                TableRow row = new TableRow(this);
                row.addView(createTextView(sv.getHo()));
                row.addView(createTextView(sv.getTen()));
                row.addView(createTextView(sv.getTuoi()));
                row.addView(createTextView(sv.getSoDienThoai()));
                tableLayout.addView(row);
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        return textView;
    }

    private void exportAsImage() {
        try {
            // Chụp ảnh TableLayout
            Bitmap bitmap = Bitmap.createBitmap(tableLayout.getWidth(), tableLayout.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            tableLayout.draw(canvas);

            // Lưu ảnh vào bộ nhớ
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "DanhSachSinhVien.png");
            try (OutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Toast.makeText(this, "Xuất ảnh thành công: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void exportAsExcel() {
        try {
            // Tạo file Excel
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Danh Sách Sinh Viên");

            // Thêm hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Họ");
            headerRow.createCell(1).setCellValue("Tên");
            headerRow.createCell(2).setCellValue("Tuổi");
            headerRow.createCell(3).setCellValue("Số điện thoại");

            // Thêm dữ liệu sinh viên
            int rowNum = 1;
            for (SinhVien sv : sinhVienList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sv.getHo());
                row.createCell(1).setCellValue(sv.getTen());
                row.createCell(2).setCellValue(sv.getTuoi());
                row.createCell(3).setCellValue(sv.getSoDienThoai());
            }

            // Lưu file Excel
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DanhSachSinhVien.xlsx");
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
                workbook.close();
                Toast.makeText(this, "Xuất Excel thành công: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
