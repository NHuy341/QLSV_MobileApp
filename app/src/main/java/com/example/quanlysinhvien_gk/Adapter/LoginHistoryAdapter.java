package com.example.quanlysinhvien_gk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.R;
import com.example.quanlysinhvien_gk.model.LoginHistory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LoginHistoryAdapter extends RecyclerView.Adapter<LoginHistoryAdapter.ViewHolder> {

    private List<LoginHistory> loginHistoryList;

    public LoginHistoryAdapter(List<LoginHistory> loginHistoryList) {
        this.loginHistoryList = loginHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_login, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginHistory history = loginHistoryList.get(position);
        holder.tvUserID.setText(history.getUserID());

        // Convert timestamp to readable date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String date = sdf.format(history.getTimestamp());
        holder.tvTimestamp.setText(date);
    }

    @Override
    public int getItemCount() {
        return loginHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserID, tvTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserID = itemView.findViewById(R.id.tvUserID);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }
}

