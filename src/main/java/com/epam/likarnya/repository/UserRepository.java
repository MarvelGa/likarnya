package com.epam.likarnya.repository;

import com.epam.likarnya.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
//    Optional<User> findUserByEmail(String email);
    User findUserByEmail (String email);
}
