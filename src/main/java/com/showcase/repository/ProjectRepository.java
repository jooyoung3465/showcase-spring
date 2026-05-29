package com.showcase.repository;

import com.showcase.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByYearOrderByCreatedAtDesc(Integer year);

    List<Project> findByYearAndFieldOrderByCreatedAtDesc(Integer year, String field);

    Page<Project> findByYearOrderByCreatedAtDesc(Integer year, Pageable pageable);

    Page<Project> findByYearAndFieldOrderByCreatedAtDesc(Integer year, String field, Pageable pageable);

    long countByYear(Integer year);

    long countByYearAndField(Integer year, String field);

    @Query("SELECT DISTINCT p.year FROM Project p ORDER BY p.year DESC")
    List<Integer> findDistinctYears();
}
