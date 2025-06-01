package com.example.quanlysinhvien_gk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.R;
import com.example.quanlysinhvien_gk.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public UserAdapter(List<User> userList, OnItemClickListener onItemClickListener) {
        this.userList = userList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Kiểm tra trạng thái và xử lý nếu null
        Boolean status = user.getTrangThai();
        if (status == null) {
            status = false; // Giá trị mặc định nếu status là null
        }

        holder.tvUserName.setText(user.getHo() + " " + user.getTen());
        holder.tvUserId.setText(user.getId());
        holder.tvStatus.setText(status ? "Bình Thường" : "Đã Khóa");

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserId, tvStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvStatus = itemView.findViewById(R.id.tv_user_status);
        }
    }
}


