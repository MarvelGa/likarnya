package com.epam.likarnya.service;

import com.epam.likarnya.model.*;
import com.epam.likarnya.repository.MedicalCardRepository;
import com.epam.likarnya.service.impl.MedicalCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MedicalCardServiceImplTest {
    @MockBean
    private MedicalCardRepository medicalCardRepository;
    private MedicalCardService service;
    private User doctor;
    private Patient patient;
    private MedicalCard medicalCard;
    private MedicalCard newMedicalCard;
    private Statement statement;

    @BeforeEach
    void setUp() {
        service = new MedicalCardServiceImpl(medicalCardRepository);

        doctor = new User();
        doctor.setId(20L);
        doctor.setFirstName("Valeriy");
        doctor.setLastName("Pionerov");
        doctor.setRole(User.Role.valueOf("DOCTOR"));

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Andrey");
        patient.setLastName("Andreev");
        patient.setGender(Patient.Gender.MALE);
        patient.setDateOfBirth(LocalDate.of(1990, 4, 14));

        statement = new Statement();
        statement.setId(1L);
        statement.setPatientStatus(Statement.PatientStatus.NEW);
        statement.setPatient(patient);
        statement.setCreatedAt(LocalDateTime.now());

        medicalCard = new MedicalCard();
        medicalCard.setId(1L);
        medicalCard.setUser(doctor);
        medicalCard.setComplaints("leg pain");
        medicalCard.setStatement(statement);

        newMedicalCard = new MedicalCard();
        newMedicalCard.setUser(doctor);
        newMedicalCard.setComplaints("leg pain");
        newMedicalCard.setStatement(statement);
    }

    @Rollback
    @Test
    void shouldUpdateMedicalCard() {
        medicalCard.getStatement().setPatientStatus(Statement.PatientStatus.DISCHARGED);
        medicalCard.setDiagnosis("broken leg");

        when(medicalCardRepository.findById(medicalCard.getId())).thenReturn(Optional.of(medicalCard));
        when(medicalCardRepository.save(medicalCard)).thenReturn(medicalCard);
        var actual = service.createOrUpdate(medicalCard);

        assertEquals(medicalCard.getDiagnosis(), actual.getDiagnosis());
        assertEquals(medicalCard.getStatement().getPatientStatus(), actual.getStatement().getPatientStatus());
        assertEquals(medicalCard, actual);
    }

    @Rollback
    @Test
    void shouldCreateMedicalCard() {
        when(medicalCardRepository.findById(newMedicalCard.getId())).thenReturn(null);
        when(medicalCardRepository.save(newMedicalCard)).thenReturn(medicalCard);
        var actual = service.createOrUpdate(newMedicalCard);
        assertEquals(medicalCard, actual);
    }

    @Test
    void shouldGetMedicalCardById() {
        MedicalCard expected = medicalCard;
        Long id = patient.getId();

        when(medicalCardRepository.getMedicalCardById(id)).thenReturn(medicalCard);
        MedicalCard actual = service.getMedicalCardForDiagnosis(patient.getId());

        assertEquals(expected, actual);
    }
}
