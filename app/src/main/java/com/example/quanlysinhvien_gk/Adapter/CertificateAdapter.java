package com.example.quanlysinhvien_gk.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlysinhvien_gk.R;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.CertificateViewHolder> {

    private List<String> certificateList;
    private OnCertificateClickListener listener;

    public interface OnCertificateClickListener {
        void onCertificateClick(String certificateName);
    }

    public CertificateAdapter(List<String> certificateList, OnCertificateClickListener listener) {
        this.certificateList = certificateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certificate, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        String certificateName = certificateList.get(position);
        holder.tvCertificateName.setText(certificateName);
        holder.itemView.setOnClickListener(v -> listener.onCertificateClick(certificateName));
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }

    public static class CertificateViewHolder extends RecyclerView.ViewHolder {
        TextView tvCertificateName;

        public CertificateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificateName = itemView.findViewById(R.id.tvCertificateName);
        }
    }
}
