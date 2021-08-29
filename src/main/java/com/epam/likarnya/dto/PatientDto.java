package com.epam.likarnya.dto;

import com.epam.likarnya.model.Patient;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    @NotBlank(message = "The field is required")
    private String firstName;
    @NotBlank(message = "The field is required")
    private String lastName;

    private String dateOfBirth;

    private Patient.Gender gender;
}
