package com.epam.likarnya.service;

import com.epam.likarnya.model.Category;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);
    List<Category> getAll();
}
