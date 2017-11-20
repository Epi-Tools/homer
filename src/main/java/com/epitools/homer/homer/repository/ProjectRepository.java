package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByUserId(Integer id);
}
