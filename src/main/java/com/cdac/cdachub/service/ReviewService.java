package com.cdac.cdachub.service;

import com.cdac.cdachub.model.*;
import com.cdac.cdachub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // Reviewer submits their verdict on a project
    public Review submitReview(Long projectId, String reviewerEmail,
                                String feedback, String verdict) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        // Save the review
        Review review = Review.builder()
                .project(project)
                .reviewer(reviewer)
                .feedback(feedback)
                .verdict(Review.Verdict.valueOf(verdict.toUpperCase()))
                .build();

        reviewRepository.save(review);

        // Update project status based on verdict
        if (verdict.equalsIgnoreCase("APPROVED")) {
            project.setStatus(Project.Status.APPROVED);
        } else {
            project.setStatus(Project.Status.REJECTED);
        }
        projectRepository.save(project);

        return review;
    }

    // Get all pending projects (for reviewer to see what needs review)
    public List<Project> getPendingProjects() {
        return projectRepository.findByStatus(Project.Status.PENDING);
    }

    // Get all reviews for a project
    public List<Review> getReviewsForProject(Long projectId) {
        return reviewRepository.findByProjectId(projectId);
    }
}