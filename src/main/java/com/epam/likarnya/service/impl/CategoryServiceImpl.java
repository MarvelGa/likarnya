package com.epam.likarnya.service.impl;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.repository.CategoryRepository;
import com.epam.likarnya.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Category by id = %s was not found", id)));
    }
}
