package com.epam.likarnya.repository;

import com.epam.likarnya.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
//    Optional<User> findUserByEmail(String email);
    User findUserByEmail (String email);
//
//    @Query(value = "SELECT * from users u WHERE u.role='DOCTOR'", nativeQuery = true)
//    List<User> getUserByRole(@Param("role") User.Role role);

    @Query(value = "SELECT * from users u WHERE u.role=?1", nativeQuery = true)
    List<User> getUserByRole(User.Role role);

}
