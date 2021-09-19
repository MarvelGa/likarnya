package com.epam.likarnya.service.impl;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.repository.CategoryRepository;
import com.epam.likarnya.service.CategoryService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Category by id = %s was not found", id)));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<Category> getAll() {
        return Lists.newArrayList (categoryRepository.findAll());
    }
}
