package com.softclub.trans.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {


    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, message = "review should have more then 5 symbols")
    private String content;

    private double rating;



    @ManyToOne
    @JoinColumn(name = "product_id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Product product;
}