package com.epam.likarnya.service;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.repository.CategoryRepository;
import com.epam.likarnya.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CategoryServiceImplTest {

    @MockBean
    private CategoryRepository categoryRepository;
    private CategoryService service;
    private Category category;

    @BeforeEach
    void setUp() {
        service = new CategoryServiceImpl(categoryRepository);
        category = new Category();
        category.setId(1L);
        category.setTitle("SURGEON");
    }

    @Test
    void shouldGetCategoryById() {
        Category expected = category;
        Long id = category.getId();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        Category actual = service.findById(id);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetAllCategories() {
        List<Category> expected = List.of(category);
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        var actual = service.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        Long notExistId = 122L;

        Throwable exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.findById(notExistId));

        assertEquals(String.format("Category by id = %s was not found", notExistId), exception.getMessage());
        assertEquals(EntityNotFoundException.class, exception.getClass());
    }
}
