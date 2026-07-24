package com.cdac.cdachub.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.cdachub.dto.TeamMemberDTO;
import com.cdac.cdachub.model.Project;
import com.cdac.cdachub.model.ProjectFile;
import com.cdac.cdachub.model.TeamMember;
import com.cdac.cdachub.model.User;
import com.cdac.cdachub.repository.ProjectFileRepository;
import com.cdac.cdachub.repository.ProjectRepository;
import com.cdac.cdachub.repository.TeamMemberRepository;
import com.cdac.cdachub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

    private final String UPLOAD_DIR = "uploads/";

    public Project submitProject(String title, String description,
            String techStack, String category, String gitLink,
            Integer year, String month,
            String submitterName, String submitterEmail, String submitterRollNo,
            String guideName, String guideEmail,
            List<TeamMemberDTO> teamMemberDtos,
            String userEmail, List<MultipartFile> files) throws IOException {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .title(title)
                .description(description)
                .techStack(techStack)
                .category(category)
                .gitLink(gitLink)
                .year(year)
                .month(month)
                .submitterName(submitterName)
                .submitterEmail(submitterEmail)
                .submitterRollNo(submitterRollNo)
                .guideName(guideName)
                .guideEmail(guideEmail)
                .status(Project.Status.PENDING)
                .user(user)
                .build();

        Project saved = projectRepository.save(project);

        // Save team members, if any
        if (teamMemberDtos != null) {
            for (TeamMemberDTO dto : teamMemberDtos) {
                TeamMember tm = TeamMember.builder()
                        .name(dto.getName())
                        .rollNo(dto.getRollNo())
                        .email(dto.getEmail())
                        .project(saved)
                        .build();
                teamMemberRepository.save(tm);
            }
        }

        // Save files (unchanged from before)
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

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