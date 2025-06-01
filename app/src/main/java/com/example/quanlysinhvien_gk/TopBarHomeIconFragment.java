package com.example.quanlysinhvien_gk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class TopBarHomeIconFragment extends Fragment {
    private TextView txtNameGV, txtIDGV;
    private ImageButton btnHome;

    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_bar_home_icon, container, false);

        txtNameGV = view.findViewById(R.id.txtNameGV);
        txtIDGV = view.findViewById(R.id.txtIDGV);
        btnHome = view.findViewById(R.id.btnHome);

        setEvent();
        return view;
    }

    private void setEvent() {
        btnHome.setOnClickListener(view -> {
            Intent mainActivity = new Intent(activity, Home.class);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivity);
            activity.finish();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
    }

    // Phương thức công khai để cập nhật thông tin tên và mã người dùng
    public void updateUserInfo(String userName, String userID) {
        if (txtNameGV != null && txtIDGV != null) {
            txtNameGV.setText(userName);
            txtIDGV.setText(userID);
        }
    }
}
