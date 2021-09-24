package com.epam.likarnya.service;

import com.epam.likarnya.dto.NurseDto;
import com.epam.likarnya.dto.UserDto;
import com.epam.likarnya.model.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);

    User createOrUpdate(User user);

    List<NurseDto> getNurses();

    User findById(Long id);

    List<UserDto> findDoctorsWithCountOfPatients();

    List<UserDto> findDoctorsWithCountOfPatientsByCategoryId(Long id);

    List<UserDto> getDoctorsByCategory(String category);
}
