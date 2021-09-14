package com.epam.likarnya.repository;

import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail (String email);

    @Query(value = "SELECT u.id AS id, u.first_name  AS firstName, u.last_name  AS lastName, cat.title AS category FROM users u INNER JOIN categories cat ON u.category_id=cat.id WHERE u.role='DOCTOR' AND cat.title =:category", nativeQuery = true)
     List<UserDTO> getDoctorsByCategory(String category);

    @Query(value = "SELECT u.id as id, u.first_name as firstName, u.last_name as lastName, u.role as Role FROM users u WHERE u.role='NURSE'", nativeQuery = true)
    List<NurseDTO> getNurses();

    @Query(value = "select u.id, u.first_name AS firstName, u.last_name AS lastName, u.role, c.title as category, \n" +
            "IFNULL((select count(1) from users us inner join medical_cards mcd on mcd.doctor_id=us.id where us.id=u.id group by mcd.doctor_id),0) as countOfPatients\n" +
            "from  users u JOIN categories c ON u.category_id=c.id group by u.id;", nativeQuery = true)
    List<UserDTO> getDoctorsWithCountOfPatients();

    @Query(value="select u.id, u.first_name AS firstName, u.last_name AS lastName, u.role, c.title as category, \n" +
            "IFNULL((select count(1) from users us inner join medical_cards mcd on mcd.doctor_id=us.id where us.id=u.id group by mcd.doctor_id),0) as countOfPatients\n" +
            "from  users u JOIN categories c ON u.category_id=c.id where c.id=:id group by u.id;", nativeQuery = true)
    List<UserDTO> getDoctorsWithCountOfPatientsByCategoryId(Long id);


}
