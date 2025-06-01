package com.example.quanlysinhvien_gk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.R;
import com.example.quanlysinhvien_gk.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    public StudentAdapter(List<Student> studentList, OnStudentClickListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getName());
        holder.tvStudentId.setText(student.getId());
        holder.tvStudentScore.setText("Điểm: " + student.getScore());
        holder.itemView.setOnClickListener(v -> listener.onStudentClick(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentId, tvStudentScore;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentScore = itemView.findViewById(R.id.tvStudentScore);
        }
    }
}

