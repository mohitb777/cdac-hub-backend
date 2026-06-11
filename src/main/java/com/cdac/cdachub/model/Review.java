package com.cdac.cdachub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ FIX: Don't re-serialize files inside project
    // when project is inside a review
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"files", "user"})
    private Project project;

    // ✅ FIX: Don't expose sensitive reviewer data
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    @JsonIgnoreProperties({"password", "googleId"})
    private User reviewer;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private LocalDateTime reviewedAt;

    @PrePersist
    public void prePersist() {
        reviewedAt = LocalDateTime.now();
    }

    public enum Verdict {
        APPROVED, REJECTED
    }
}