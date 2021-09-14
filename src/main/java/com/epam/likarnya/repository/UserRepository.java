package com.epam.likarnya.repository;

import com.epam.likarnya.dto.NurseDTO;
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

    User findUserByEmail (String email);

    @Query(value = "SELECT u.id AS id, u.first_name  AS firstName, u.last_name  AS lastName, cat.title AS category FROM users u INNER JOIN categories cat ON u.category_id=cat.id WHERE u.role='DOCTOR' AND cat.title =:category", nativeQuery = true)
     List<UserDTO> getDoctorsByCategory(String category);


//    List<DoctorDTO> getDoctors();
    @Query(value = "SELECT u.id as id, u.first_name as firstName, u.last_name as lastName, u.role as Role FROM users u WHERE u.role='NURSE'", nativeQuery = true)
    List<NurseDTO> getNurses();




}
