package com.epam.likarnya.repository;

import com.epam.likarnya.dto.UserDTO;
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


    @Query(value = "SELECT u.id AS id, u.first_name  AS firstName, u.last_name  AS lastName, cat.title AS category FROM users u INNER JOIN categories cat ON u.category_id=cat.id WHERE u.role='DOCTOR' AND cat.title =:category", nativeQuery = true)
     List<UserDTO> getDoctorsByCategory(String category);

//    @Query(value = "SELECT u.id AS id, u.first_name  AS firstName, u.last_name  AS lastName, cat.title AS category FROM users u INNER JOIN categories cat ON u.category_id=cat.id WHERE u.role='DOCTOR' AND cat.id =:id", nativeQuery = true)
//    List<UserDTO> getDoctorsByCategory(Long id);




}
