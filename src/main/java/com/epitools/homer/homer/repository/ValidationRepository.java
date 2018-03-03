package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Project;
import com.epitools.homer.homer.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {

    Integer countByProjectAndStatus(Project project,Integer status);

    List<Validation> findByProjectIdOrderByIdDesc(Integer id);

    @Transactional
    List <Validation> removeByProjectId(Integer id);

}
