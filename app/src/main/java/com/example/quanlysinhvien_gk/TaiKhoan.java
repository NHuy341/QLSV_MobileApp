package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysinhvien_gk.R;

public class TaiKhoan extends AppCompatActivity {

    private Button btn_Back_Home; // Declare the button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);


        btn_Back_Home = findViewById(R.id.btn_Back_Home);


        btn_Back_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}
