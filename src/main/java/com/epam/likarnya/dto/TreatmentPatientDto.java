package com.epam.likarnya.dto;

import java.time.LocalDateTime;

public interface TreatmentPatientDto {
    Long getId();

    String getFirstName();

    String getLastName();

    String getDateOfBirth();

    String getGender();

    String getComplaints();

    String getDiagnosis();

    String getAppointment();

    String getAppointmentStatus();

    String getDoctorFirstName();

    String getDoctorLastName();

    String getDoctorCategory();

    String getTreatmentId();

    String getStatementId();

    String getNameOfExecutor();

    String getLastNameOfExecutor();

    String getRoleOfExecutor();

    LocalDateTime getDateOfAdmission();

    LocalDateTime getDateOfDischarge();
}
