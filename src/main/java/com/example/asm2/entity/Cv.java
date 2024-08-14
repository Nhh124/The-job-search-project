package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="cv")
public class Cv {
    @Id
    private int id;

    @Column(name = "file_name")
    private String fileName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cv", cascade = CascadeType.ALL)
    List<User> users;

    @Column(name="user_id")
    private int userId;

    public Cv(int id, String fileName, List<User> users, int userId) {
        this.id = id;
        this.fileName = fileName;
        this.users = users;
        this.userId = userId;
    }

    public Cv() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
