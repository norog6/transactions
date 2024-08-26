package com.softclub.trans.service;

import com.softclub.trans.entity.Product;
import com.softclub.trans.entity.Review;
import com.softclub.trans.repository.ProductRepository;
import com.softclub.trans.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private static final int MAX_RETRIES = 3;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void addReviewAndUpdateProduct(Long productId, Review review) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        List<Review> rev = reviewRepository.findAllByProductId(productId);
        rev.add(review);
        product.calculateRating(rev);
        review.setProduct(product);
        productRepository.save(product);
        addReviewInNestedTransaction(review);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addReviewInNestedTransaction(Review review) {
        reviewRepository.save(review);
        try {
        } catch (Exception e) {
            throw new RuntimeException("review transaction rolled back:" + e.getMessage());
        }
    }

    @Transactional
    public void updateProduct(Product product) {
        int retryCount = 0;

        while (retryCount < MAX_RETRIES) {
            try {
                Product product1 = productRepository.findById(product.getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                product1.setName(product.getName());

                productRepository.save(product1);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= MAX_RETRIES) {
                    throw new RuntimeException("Failed to update product after " + MAX_RETRIES + " attempts");
                }
            }
        }
    }
}
