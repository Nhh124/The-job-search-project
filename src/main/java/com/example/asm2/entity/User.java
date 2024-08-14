package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name ="user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name ="address")
    private String address;

    @Column(name ="description")
    private String description;

    @Column(name ="image")
    private String image;

    @Column(name ="password")
    private String password;

    @Column(name ="phoneNumber")
    private String phoneNumber;

    @Column(name ="email")
    private String email;

    @Column(name ="status")
    private int status;


    /**
     * Khóa ngoại giữa 2 bảng User và Role.
     *
     * Mối quan hệ Many-to-One với entity Role thông qua trường role_id trong bảng donation.
     * Sử dụng chiến lược fetch LAZY để tải lazily thông tin Role khi cần thiết.
     * Sử dụng trường role_id để tham chiếu tới primary key trong bảng Role.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private Cv cv;

    public User(int id, String fullName, String address, String description, String image,
                String password, String phoneNumber, String email, int status, Role role, Cv cv) {
        Id = id;
        this.fullName = fullName;
        this.address = address;
        this.description = description;
        this.image = image;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = status;
        this.role = role;
        this.cv = cv;
    }

    public User() {
    }

    public Cv getCv() {
        return cv;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



}











