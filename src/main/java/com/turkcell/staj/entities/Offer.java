package com.turkcell.staj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "offer_name")
    private String offerName;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @Column(name = "status")
    private Boolean status;
    @OneToMany(mappedBy = "offer")
    private List<Transaction> transactions;
    @OneToMany(mappedBy = "offer")
    private List<Review> reviews;
    @OneToOne(mappedBy = "offer")
    private Discount discount;
}
