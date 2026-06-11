package com.cdac.cdachub.repository;

import com.cdac.cdachub.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Get all reviews for a specific project
    List<Review> findByProjectId(Long projectId);

    // Get all reviews done by a specific reviewer
    List<Review> findByReviewerId(Long reviewerId);

    // Check if reviewer already reviewed this project
    Optional<Review> findByProjectIdAndReviewerId(Long projectId, Long reviewerId);
}