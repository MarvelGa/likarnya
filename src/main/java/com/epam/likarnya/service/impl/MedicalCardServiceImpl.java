package com.epam.likarnya.service.impl;

import com.epam.likarnya.model.MedicalCard;
import com.epam.likarnya.repository.MedicalCardRepository;
import com.epam.likarnya.service.MedicalCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MedicalCardServiceImpl implements MedicalCardService {
    private final MedicalCardRepository  medicalCardRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public MedicalCard createOrUpdate(MedicalCard medicalCard) {
        if (medicalCard.getId()!=null){
            Optional<MedicalCard> medicalCardOptional = medicalCardRepository.findById(medicalCard.getId());
            if (medicalCardOptional.isPresent()){
                MedicalCard newMedicalCard = medicalCardOptional.get();
                newMedicalCard.setStatement(medicalCard.getStatement());
                newMedicalCard.setDiagnosis(medicalCard.getDiagnosis());
                return medicalCardRepository.save(newMedicalCard);
            }
        }
        return medicalCardRepository.save(medicalCard);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public MedicalCard getMedicalCardForDiagnosis(Long patientId) {
        return medicalCardRepository.getMedicalCardById(patientId);
    }
}
