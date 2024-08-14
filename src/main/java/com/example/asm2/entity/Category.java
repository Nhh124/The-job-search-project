package com.example.asm2.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "number_choose")
    private int numberChoose;

    private int ranking;

    private int categoryCount;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Recruitment> recruitments;
    public Category() {
    }

    public Category(int id, String name, int numberChoose, int ranking) {
        this.id = id;
        this.name = name;
        this.numberChoose = numberChoose;
        this.ranking = ranking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberChoose() {
        return numberChoose;
    }

    public void setNumberChoose(int numberChoose) {
        this.numberChoose = numberChoose;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }
}
