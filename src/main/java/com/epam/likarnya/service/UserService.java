package com.epam.likarnya.service;

import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.model.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User createOrUpdate(User user);

    List<NurseDTO> getNurses();

    User findById(Long id);

    List<UserDTO> findDoctorsWithCountOfPatients();

    List<UserDTO> findDoctorsWithCountOfPatientsByCategoryId(Long id);
    List<UserDTO> getDoctorsByCategory(String category);
}
