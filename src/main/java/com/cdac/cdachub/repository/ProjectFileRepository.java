package com.cdac.cdachub.repository;

import com.cdac.cdachub.model.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    List<ProjectFile> findByProjectId(Long projectId);
}