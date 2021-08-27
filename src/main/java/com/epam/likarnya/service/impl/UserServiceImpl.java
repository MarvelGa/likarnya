package com.epam.likarnya.service.impl;


import com.epam.likarnya.model.User;
import com.epam.likarnya.repository.UserRepository;
import com.epam.likarnya.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public User createOrUpdate(User user) {
        if (user.getId()!=null){
            Optional<User> userOptional = repository.findById(user.getId());
            if (userOptional.isPresent()){
                User newUser = userOptional.get();
//                newUser.setEmail();
            }

        }
//        User createUser = User.builder()
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .build();

        return repository.save(user);
    }


}
