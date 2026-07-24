package com.cdac.cdachub.controller;

import com.cdac.cdachub.model.Project;
import com.cdac.cdachub.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminProjectController {

    private final ProjectRepository projectRepository;

    // Admin — see every project, any status
    @GetMapping("/api/admin/projects")
    public ResponseEntity<List<Project>> getAllForAdmin() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    // Admin — permanently delete a project (cascades to its files + team members)
    @DeleteMapping("/api/admin/projects/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            projectRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Project deleted"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not delete project: " + e.getMessage()));
        }
    }
}