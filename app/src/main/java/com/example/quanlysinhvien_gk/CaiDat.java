package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;

public class CaiDat extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    private EditText etLastName, etFirstName, etAge, etPhone;
    private Button btnSaveChanges, btnBack, btnUpdateAvatar;
    private ImageView ivAvatar;
    private FirebaseFirestore db;

    private String userID, collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy ID người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userID = sharedPreferences.getString("userID", null);

        if (userID == null) {
            Toast.makeText(this, "Không thể xác định người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Xác định Collection dựa trên ID
        collectionName = getCollectionName(userID);

        if (collectionName == null) {
            Toast.makeText(this, "ID không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tham chiếu các View
        etLastName = findViewById(R.id.etLastName);
        etFirstName = findViewById(R.id.etFirstName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);
        btnUpdateAvatar = findViewById(R.id.btnUpdateAvatar);

        // Ẩn nút "Lưu Thay Đổi" nếu là SV
        if (userID.startsWith("SV")) {
            btnSaveChanges.setVisibility(Button.GONE); // Ẩn nút
        }

        // Tải dữ liệu người dùng từ Firestore
        loadUserData();

        // Xử lý nút lưu thay đổi
        btnSaveChanges.setOnClickListener(v -> saveUserData());

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút chọn ảnh đại diện
        btnUpdateAvatar.setOnClickListener(v -> selectAvatar());
    }

    /**
     * Xác định Collection dựa trên 2 chữ cái đầu của userID.
     */
    private String getCollectionName(String userID) {
        if (userID.startsWith("GV")) {
            return "User";
        } else if (userID.startsWith("SV")) {
            return "Student";
        } else if (userID.startsWith("AD")) {
            return "Admin";
        } else {
            return null;
        }
    }

    private void loadUserData() {
        DocumentReference userDoc = db.collection(collectionName).document(userID);
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Hiển thị dữ liệu người dùng
                etLastName.setText(documentSnapshot.getString("ho"));
                etFirstName.setText(documentSnapshot.getString("ten"));
                etAge.setText(documentSnapshot.getString("tuoi"));
                etPhone.setText(documentSnapshot.getString("soDienThoai"));

                // Đặt ảnh mặc định nếu không tải từ Firestore
                ivAvatar.setImageResource(R.drawable.avatar);
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi tải dữ liệu!", Toast.LENGTH_SHORT).show());
    }

    private void saveUserData() {
        // Lấy dữ liệu từ form
        String lastName = etLastName.getText().toString();
        String firstName = etFirstName.getText().toString();
        String age = etAge.getText().toString();
        String phone = etPhone.getText().toString();

        // Cập nhật Firestore
        DocumentReference userDoc = db.collection(collectionName).document(userID);
        userDoc.update(
                        "ho", lastName,
                        "ten", firstName,
                        "tuoi", age,
                        "soDienThoai", phone
                ).addOnSuccessListener(aVoid -> Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show());
    }

    private void selectAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                ivAvatar.setImageBitmap(selectedImage); // Hiển thị ảnh đã chọn
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
