package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContributorRepository extends JpaRepository<Contributor, Integer> {

    Contributor findFirstByUserIdAndProjectId(Integer userId, Integer projectId);

    List<Contributor> findByProjectId(Integer id);

    Integer countAllByProjectId(Integer id);

}
