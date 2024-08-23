package com.softclub.trans.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, message = "review should have more then 5 symbols")
    private String content;

    private double rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}