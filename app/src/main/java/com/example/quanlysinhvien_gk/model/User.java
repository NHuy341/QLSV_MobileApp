package com.example.quanlysinhvien_gk.model;

public class User {
    private String id;
    private String ho;
    private String ten;
    private String tuoi;
    private String soDienThoai;

    private Boolean trangThai;

    public User() {
    }

    public User(String id, String ho, String ten, String tuoi, Boolean status) {
        this.id = id;
        this.ho = ho;
        this.ten = ten;
        this.tuoi = tuoi;
        this.trangThai = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTuoi() {
        return tuoi;
    }

    public void setTuoi(String tuoi) {
        this.tuoi = tuoi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Boolean getTrangThai() {
        return trangThai != null ? trangThai : false; // Trả về false nếu null
    }

    public void setTrangThai(Boolean status) {
        this.trangThai = status;
    }
}
