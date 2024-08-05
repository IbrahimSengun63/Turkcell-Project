package com.turkcell.staj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "packages")
public class Packages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "package_name")
    private String packageName;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name = "status")
    private boolean status;
    @OneToMany(mappedBy = "package")
    private List<Transaction> transactions;
    @OneToMany(mappedBy = "packages")
    private List<Review> reviews;
}
