package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.User;
import com.epitools.homer.homer.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {

    Integer countByProjectAndStatus(Project project,Integer status);

    List<Validation> findByProjectIdOrderByIdDesc(Integer id);

    List<Validation> findByUserAndValidNot(User user, Boolean valid);

    List<Validation> findByProjectAndStatus(Project project, Integer status);

    Validation findByUserAndProjectAndStatusAndValidNot(User user, Project project, Integer status, Boolean valid);

    @Transactional
    List <Validation> removeByProjectId(Integer id);

}
