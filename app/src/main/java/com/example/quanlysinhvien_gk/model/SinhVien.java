package com.example.quanlysinhvien_gk.model;

public class SinhVien {
    private String id;
    private String ho;
    private String ten;
    private String tuoi;
    private String soDienThoai;

    public SinhVien() {
        // Constructor mặc định cần thiết cho Firestore
    }

    public SinhVien(String id, String ho, String ten, String tuoi, String soDienThoai) {
        this.id = id;
        this.ho = ho;
        this.ten = ten;
        this.tuoi = tuoi;
        this.soDienThoai = soDienThoai;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHo() { return ho; }
    public void setHo(String ho) { this.ho = ho; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getTuoi() { return tuoi; }
    public void setTuoi(String tuoi) { this.tuoi = tuoi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}
