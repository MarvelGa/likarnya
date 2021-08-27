package com.epam.likarnya.service;

import com.epam.likarnya.model.User;

public interface UserService {
    User findByEmail(String email);
    User createOrUpdate(User user);
}
