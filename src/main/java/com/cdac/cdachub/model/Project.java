package com.cdac.cdachub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String techStack;
    private String category;
    private Double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    // ✅ FIX: When serializing user inside project,
    // don't include password and googleId
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "googleId"})
    private User user;

    // ✅ FIX: When serializing files inside project,
    // don't re-serialize the "project" field inside each file
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("project")
    private List<ProjectFile> files;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, UNDER_REVIEW, APPROVED, REJECTED
    }
}