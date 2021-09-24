package com.epam.likarnya.service.impl;


import com.epam.likarnya.dto.NurseDto;
import com.epam.likarnya.dto.UserDto;
import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public User findByEmail(String email) {
        return repository.findUserByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User createOrUpdate(User user) {
        return repository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<NurseDto> getNurses() {
        return repository.getNurses();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(()->new EntityNotFoundException(String.format("User by id = %s was not found", id)));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<UserDto> findDoctorsWithCountOfPatients() {
        return repository.getDoctorsWithCountOfPatients();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<UserDto> findDoctorsWithCountOfPatientsByCategoryId(Long id) {
        return repository.getDoctorsWithCountOfPatientsByCategoryId(id);
    }

    @Override
    public List<UserDto> getDoctorsByCategory(String category) {
        return repository.getDoctorsByCategory(category);
    }


}
