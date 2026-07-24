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

    // ✅ Replaces "price"
    @Column(nullable = false)
    private String gitLink;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String month;

    // ✅ NEW — captured fresh at submission time
    private String submitterName;
    private String submitterEmail;
    private String submitterRollNo;

    // ✅ NEW — guide details
    private String guideName;
    private String guideEmail;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "googleId"})
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("project")
    private List<ProjectFile> files;

    // ✅ NEW — 0 to 12 team members
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("project")
    private List<TeamMember> teamMembers;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, UNDER_REVIEW, APPROVED, REJECTED
    }
}