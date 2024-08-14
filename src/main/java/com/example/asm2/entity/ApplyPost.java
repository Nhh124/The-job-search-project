package com.example.asm2.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "applypost")
public class ApplyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "name_cv")
    private String cvName;

    @Column(name = "status")
    private int status;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Constructors, getters, and setters

    public ApplyPost() {
    }

    // Constructor without ID (assuming ID is auto-generated)
    public ApplyPost(String createdAt, Recruitment recruitment, User user, String cvName, int status, String text) {
        this.createdAt = createdAt;
        this.recruitment = recruitment;
        this.user = user;
        this.cvName = cvName;
        this.status = status;
        this.text = text;
    }

    // Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCvName() {
        return cvName;
    }

    public void setCvName(String cvName) {
        this.cvName = cvName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Recruitment getRecruitment() {
        return recruitment;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}