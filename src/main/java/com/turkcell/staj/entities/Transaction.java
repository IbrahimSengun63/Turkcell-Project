package com.turkcell.staj.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "price")
    private double price;
    @Enumerated(EnumType.STRING)
    /*
    @Column(name = "status")
    private Status status;
     */
    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDate createdDate;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne()
    @JoinColumn(name = "package_id")
    private Packages packages;
}
