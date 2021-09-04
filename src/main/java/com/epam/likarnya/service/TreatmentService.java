package com.epam.likarnya.service;

import com.epam.likarnya.model.Treatment;

public interface TreatmentService {
    Treatment createOrUpdate(Treatment treatment);
    Treatment getById(Long id);
}
