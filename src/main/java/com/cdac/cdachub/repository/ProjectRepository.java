package com.cdac.cdachub.repository;

import com.cdac.cdachub.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(Project.Status status);
    List<Project> findByUserId(Long userId);
    List<Project> findByCategory(String category);
    
    List<Project> findByStatusAndYear(String status, Integer year);
    
}