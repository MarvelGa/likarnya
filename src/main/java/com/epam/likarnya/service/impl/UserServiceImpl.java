package com.epam.likarnya.service.impl;


import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        if (user.getId()!=null){
            Optional<User> userOptional = repository.findById(user.getId());
            if (userOptional.isPresent()){
                User newUser = userOptional.get();
//                newUser.setEmail();
            }

        }
        return repository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<NurseDTO> getNurses() {
        return repository.getNurses();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(()->new EntityNotFoundException(String.format("User by id = %s was not found", id)));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<UserDTO> findDoctorsWithCountOfPatients() {
        return repository.getDoctorsWithCountOfPatients();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<UserDTO> findDoctorsWithCountOfPatientsByCategoryId(Long id) {
        return repository.getDoctorsWithCountOfPatientsByCategoryId(id);
    }

    @Override
    public List<UserDTO> getDoctorsByCategory(String category) {
        return repository.getDoctorsByCategory(category);
    }


}
