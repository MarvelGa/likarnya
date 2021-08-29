package com.epam.likarnya.service;

import com.epam.likarnya.model.Patient;

public interface PatientService {
    Patient createOrUpdate(Patient patient);
}
