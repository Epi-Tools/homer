package com.epitools.homer.homer.repository;

import com.epitools.homer.homer.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> { }