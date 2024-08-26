package com.softclub.trans.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String name;

    private double rating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Version
    private int version;

    public void calculateRating(List<Review> reviews) {
        this.rating = reviews.stream().mapToDouble(r -> r.getRating()).average().orElse(0);
    }
}
