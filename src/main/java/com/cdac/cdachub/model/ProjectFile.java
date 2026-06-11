
package com.cdac.cdachub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileUrl;
    private String fileType;

    // ✅ FIX: When serializing project inside a file,
    // don't re-serialize the "files" list inside that project
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"files", "user"})
    private Project project;
}