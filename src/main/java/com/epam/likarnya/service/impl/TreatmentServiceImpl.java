package com.epam.likarnya.service.impl;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.Treatment;
import com.epam.likarnya.repository.TreatmentRepository;
import com.epam.likarnya.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TreatmentServiceImpl implements TreatmentService {
    private final TreatmentRepository repository;

    @Override
    public Treatment createOrUpdate(Treatment treatment) {
        if (treatment.getId()!=null){
            Optional<Treatment> treatmentOptional = repository.findById(treatment.getId());
            if (treatmentOptional.isPresent()){
                Treatment newTreatment = treatmentOptional.get();
                newTreatment.setExecutorId(treatment.getExecutorId());
                newTreatment.setAppointmentStatus(treatment.getAppointmentStatus());
                newTreatment.setChanged(LocalDateTime.now());
                repository.save(newTreatment);
            }
        }
        return repository.save(treatment);
    }

    @Override
    public Treatment getById(Long id) {
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Treatment with id %s was not found", id)));
    }
}
