package com.epam.likarnya.service;

import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.model.User;

import java.util.List;

public interface UserService {
    User findByEmail(String email);
    User createOrUpdate(User user);
    List<NurseDTO> getNurses();
    User findById(Long id);
}
