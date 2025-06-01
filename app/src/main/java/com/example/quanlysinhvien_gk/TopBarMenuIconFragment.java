package com.example.quanlysinhvien_gk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TopBarMenuIconFragment extends Fragment {

    private TextView tvUserName, tvUserID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar_menu_icon, container, false);

        // Tham chiếu các TextView
        tvUserName = view.findViewById(R.id.txtNameGV);
        tvUserID = view.findViewById(R.id.txtIDGV);

        return view;
    }

    // Phương thức công khai để cập nhật thông tin
    public void updateUserInfo(String userName, String userID) {
        if (tvUserName != null && tvUserID != null) {
            tvUserName.setText(userName);
            tvUserID.setText(userID);
        }
    }
}
