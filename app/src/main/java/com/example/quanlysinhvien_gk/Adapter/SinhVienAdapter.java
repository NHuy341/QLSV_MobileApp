package com.example.quanlysinhvien_gk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.ChinhSuaThongTinSinhVien;
import com.example.quanlysinhvien_gk.R;
import com.example.quanlysinhvien_gk.model.SinhVien;

import java.util.List;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.ViewHolder> {
    private List<SinhVien> sinhVienList;
    private Context context;

    public SinhVienAdapter(Context context, List<SinhVien> sinhVienList) {
        this.context = context;
        this.sinhVienList = sinhVienList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sinhvien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SinhVien sinhVien = sinhVienList.get(position);
        holder.tvHoTen.setText(sinhVien.getHo() + " " + sinhVien.getTen());
        holder.tvidSinhVien.setText(sinhVien.getId());

        // Xử lý click vào từng item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChinhSuaThongTinSinhVien.class);
            intent.putExtra("id", sinhVien.getId());
            intent.putExtra("ho", sinhVien.getHo());
            intent.putExtra("ten", sinhVien.getTen());
            intent.putExtra("tuoi", sinhVien.getTuoi());
            intent.putExtra("soDienThoai", sinhVien.getSoDienThoai());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sinhVienList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvidSinhVien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tv_student_name);
            tvidSinhVien = itemView.findViewById(R.id.tv_student_id);
        }
    }
}

