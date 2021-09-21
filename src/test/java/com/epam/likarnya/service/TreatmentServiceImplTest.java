package com.epam.likarnya.service;

import com.epam.likarnya.exception.EntityNotFoundException;
import com.epam.likarnya.model.*;
import com.epam.likarnya.repository.TreatmentRepository;
import com.epam.likarnya.service.impl.TreatmentServiceImpl;
import org.junit.jupiter.api.Assertions;
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
public class TreatmentServiceImplTest {
    @MockBean
    private TreatmentRepository repository;
    private TreatmentService service;

    private User doctor;
    private Patient patient;
    private MedicalCard medicalCard;
    private Statement statement;
    private Treatment treatment;
    private Treatment newTreatment;

    @BeforeEach
    void setUp() {
        service = new TreatmentServiceImpl(repository);

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
        medicalCard.setDiagnosis("broken leg");
        medicalCard.setStatement(statement);

        treatment = new Treatment();
        treatment.setId(1L);
        treatment.setAppointment(treatment.getAppointment());
        treatment.setAppointmentStatus(Treatment.AppointmentStatus.NOT_EXECUTED);
        treatment.setCreatedAt(LocalDateTime.now());
        treatment.setMedicalCard(medicalCard);

        newTreatment = new Treatment();
        newTreatment.setAppointment(treatment.getAppointment());
        newTreatment.setAppointmentStatus(Treatment.AppointmentStatus.NOT_EXECUTED);
        newTreatment.setCreatedAt(LocalDateTime.now());
        newTreatment.setMedicalCard(medicalCard);
    }


    @Rollback
    @Test
    void shouldUpdateTreatment() {
        treatment.setExecutorId(doctor.getId());
        treatment.setAppointmentStatus(Treatment.AppointmentStatus.EXECUTED);

        when(repository.findById(treatment.getId())).thenReturn(Optional.of(treatment));
        when(repository.save(treatment)).thenReturn(treatment);
        var actual = service.createOrUpdate(treatment);

        assertEquals(treatment.getAppointmentStatus(), actual.getAppointmentStatus());
        assertEquals(treatment.getExecutorId(), actual.getExecutorId());
        assertEquals(treatment, actual);
    }

    @Rollback
    @Test
    void shouldCreateTreatment() {
        when(repository.findById(newTreatment.getId())).thenReturn(null);
        when(repository.save(newTreatment)).thenReturn(newTreatment);
        var actual = service.createOrUpdate(newTreatment);
        assertEquals(newTreatment, actual);
    }

    @Test
    void shouldGetTreatmentById() {
        var expected = treatment;
        Long id = treatment.getId();

        when(repository.findById(id)).thenReturn(Optional.of(treatment));
        var actual = service.getById(id);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfEntityNotFound() {
        Long notExistId = 122L;

        Throwable exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> service.getById(notExistId));

        assertEquals(String.format("Treatment with id %s was not found", notExistId), exception.getMessage());
        assertEquals(EntityNotFoundException.class, exception.getClass());
    }
}
