package com.cdac.cdachub.controller;

import com.cdac.cdachub.model.Project;
import com.cdac.cdachub.service.ProjectService;
import com.cdac.cdachub.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map; // ✅ FIX 1: Added Map import

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
    
    @GetMapping("/public/projects/year/{year}")
    public ResponseEntity<List<Project>> getByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(projectService.getApprovedByYear(year));
    }
    
    // STUDENT — submit project (needs JWT)
    @PostMapping("/student/projects")
    public ResponseEntity<?> submit(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String techStack,
            @RequestParam String category,
            @RequestParam Double price,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String month,
            @RequestParam List<MultipartFile> files) throws Exception {

        // ✅ Validate before hitting the DB — clean error instead of a raw SQL crash
        if (month == null || month.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Month is required"));
        }
        if (year == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Year is required"));
        }

        String email = AuthUtil.getCurrentUserEmail();

        // ✅ FIX 2: Corrected the parameter order to match the service (email comes before year/month)
        Project p = projectService.submitProject(
        	    title, description, techStack, category, price, email, year, month, files
        	);
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