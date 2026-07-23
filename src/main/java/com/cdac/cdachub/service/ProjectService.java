package com.cdac.cdachub.service;

import com.cdac.cdachub.model.*;
import com.cdac.cdachub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final UserRepository userRepository;

    private final String UPLOAD_DIR = "uploads/";

    public Project submitProject(String title, String description,
            String techStack, String category,
            Double price, String userEmail,
            Integer year, String month,
            List<MultipartFile> files) throws IOException {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save project
        Project project = Project.builder()
                .title(title)
                .description(description)
                .techStack(techStack)
                .category(category)
                .price(price)
                .status(Project.Status.PENDING)
                .user(user)
                .year(year)    // Make sure this is here!
                .month(month)
                .build();
        	

        Project saved = projectRepository.save(project);

        // Save files
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path,
                StandardCopyOption.REPLACE_EXISTING);

            ProjectFile pf = ProjectFile.builder()
                    .fileName(file.getOriginalFilename())
                    .fileUrl("/uploads/" + fileName)
                    .fileType("DOCS")
                    .project(saved)
                    .build();
            projectFileRepository.save(pf);
        }

        return saved;
    }

    public List<Project> getApprovedProjects() {
        return projectRepository.findByStatus(Project.Status.APPROVED);
    }

    public List<Project> getMyProjects(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectRepository.findByUserId(user.getId());
    }
    
    public List<Project> getApprovedByYear(Integer year) {
        return projectRepository.findByStatusAndYear("APPROVED", year);
    }

    public Project updateStatus(Long projectId, Project.Status status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setStatus(status);
        return projectRepository.save(project);
    }
}