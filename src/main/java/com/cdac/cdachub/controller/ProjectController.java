package com.cdac.cdachub.controller;

import com.cdac.cdachub.model.Project;
import com.cdac.cdachub.service.ProjectService;
import com.cdac.cdachub.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    private final ProjectService projectService;

    // PUBLIC — anyone can browse
    @GetMapping("/public/projects")
    public ResponseEntity<List<Project>> getApproved() {
        return ResponseEntity.ok(projectService.getApprovedProjects());
    }

    // STUDENT — submit project (needs JWT)
    @PostMapping("/student/projects")
    public ResponseEntity<Project> submit(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String techStack,
            @RequestParam String category,
            @RequestParam Double price,
            @RequestParam List<MultipartFile> files) throws Exception {

        // Get email from JWT token (via AuthUtil)
        String email = AuthUtil.getCurrentUserEmail();

        Project p = projectService.submitProject(
            title, description, techStack,
            category, price, email, files);

        return ResponseEntity.ok(p);
    }

    // STUDENT — see my projects
    @GetMapping("/student/projects/mine")
    public ResponseEntity<List<Project>> myProjects() {
        String email = AuthUtil.getCurrentUserEmail();
        return ResponseEntity.ok(projectService.getMyProjects(email));
    }

    // REVIEWER — update project status
    @PutMapping("/reviewer/projects/{id}/status")
    public ResponseEntity<Project> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Project.Status s = Project.Status.valueOf(status.toUpperCase());
        return ResponseEntity.ok(projectService.updateStatus(id, s));
    }
}