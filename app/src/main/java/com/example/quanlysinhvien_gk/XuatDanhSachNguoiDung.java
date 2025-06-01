package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class XuatDanhSachNguoiDung extends AppCompatActivity {

    private Button btnQuayLai; // Declare the "Quay Lại" button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_danh_sach_nguoi_dung); // Make sure to use the correct layout resource

        // Initialize the "Quay Lại" button by finding it by ID
        btnQuayLai = findViewById(R.id.classroomExportButtonGoBack);

        // Set an OnClickListener to handle the click event
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
                // Finish the current activity and go back to the previous one
                finish(); // This will close the current activity and return to the previous one in the back stack
                // OR you can explicitly navigate to another activity using Intent, if needed
                // Intent intent = new Intent(XuatDanhSachNguoiDung.this, MainActivity.class);
                // startActivity(intent); // Uncomment if you need to go to a specific activity
            }
        });
    }
}
