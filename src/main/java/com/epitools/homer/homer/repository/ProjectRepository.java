package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByUserId(Integer id);

    List<Project> findByUserIdAndStatusNotOrderByIdDesc(Integer id, Integer status);

    List<Project> findByUserIdAndStatusOrderByIdDesc(Integer id, Integer status);

    List<Project> findByStatusNotOrderByIdDesc(Integer status);

    List<Project> findByStatusOrderByIdDesc(Integer status);

    List<Project> findAllByOrderByIdDesc();
}
