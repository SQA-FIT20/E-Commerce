package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.Customer;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.domain.Review;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.exception.NotFoundException;
import com.example.ecommerce.repository.ReviewRepository;
import com.example.ecommerce.service.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public void save(Review currentReview) {
        reviewRepository.save(currentReview);
    }

    @Override
    public void deleteByReviewId(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found for id: " + reviewId));
    }

    @Override
    public List<Review> getAllReview() {
        return reviewRepository.findAll();
    }


    @Override
    public List<Review> getReviewByCustomer(User customer) {
        return reviewRepository.findAllByCustomer(customer);
    }

    @Override
    public Review findReviewById(Long reviewId) {

        return reviewRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found for id: " + reviewId));
    }

    @Override
    public Page<Review> findAll(Example<Review> example, Pageable pageable) {
        return reviewRepository.findAll(example, pageable);
    }

    @Override
    public Page<Review> findAllByProduct(Product product, Pageable pageable) {
        return reviewRepository.findAllByProduct(product, pageable);
    }

    @Override
    public boolean existsByCustomerAndProduct(Customer customer, Product product) {
        return reviewRepository.existsByCustomerAndProduct(customer, product);
    }

}
