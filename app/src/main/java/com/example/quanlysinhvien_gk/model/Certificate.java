package com.example.quanlysinhvien_gk.model;
public class Certificate {
    private String certificateType; // Loại chứng chỉ (IELTS, APTIC, ...)
    private String studentId;       // Mã sinh viên sở hữu chứng chỉ
    private String studentName;     // Tên sinh viên sở hữu chứng chỉ
    private String score;           // Điểm đạt được

    // Constructor không tham số (cần cho Firestore)
    public Certificate() {
    }

    // Constructor đầy đủ tham số
    public Certificate(String certificateType, String studentId, String studentName, String score) {
        this.certificateType = certificateType;
        this.studentId = studentId;
        this.studentName = studentName;
        this.score = score;
    }

    // Getter và Setter
    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

