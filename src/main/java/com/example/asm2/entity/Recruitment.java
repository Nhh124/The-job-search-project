package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recruitment")
public class Recruitment {

    @Id
    private int id;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "description")
    private String description;

    @Column(name="experience")
    private String experience;

    @Column(name="quantity")
    private int quantity;

    @Column(name="rank_job")
    private String rankJob;

    @Column(name="salary")
    private String salary;

    @Column(name="status")
    private int status;

    @Column(name="title")
    private String title;

    @Column(name="type")
    private String type;

    @Column(name="view")
    private int view;

    @Column(name="deadline")
    private String deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY)
    private List<ApplyPost> applyPosts;

    public Recruitment() {
    }

    public Recruitment(int id, String address, String createdAt, String description,
                       String experience, int quantity, String rankJob, String salary,
                       int status, String title, String type, int view, String deadline,
                       Company company, Category category) {
        this.id = id;
        this.address = address;
        this.createdAt = createdAt;
        this.description = description;
        this.experience = experience;
        this.quantity = quantity;
        this.rankJob = rankJob;
        this.salary = salary;
        this.status = status;
        this.title = title;
        this.type = type;
        this.view = view;
        this.deadline = deadline;
        this.company = company;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRankJob() {
        return rankJob;
    }

    public void setRankJob(String rankJob) {
        this.rankJob = rankJob;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
