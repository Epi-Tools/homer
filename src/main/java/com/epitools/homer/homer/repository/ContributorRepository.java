package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.Contributor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributorRepository extends CrudRepository<Contributor, Long> {}
