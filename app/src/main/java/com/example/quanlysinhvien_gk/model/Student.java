package com.example.quanlysinhvien_gk.model;
public class Student {
    private String id; // Mã sinh viên
    private String name; // Tên sinh viên
    private String score; // Điểm đạt được

    public Student() {
    }

    // Constructor đầy đủ tham số
    public Student(String id, String name, String score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

