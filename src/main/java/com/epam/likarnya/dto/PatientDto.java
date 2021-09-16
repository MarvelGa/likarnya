package com.epam.likarnya.dto;

import com.epam.likarnya.model.Patient;
import lombok.*;

import javax.validation.constraints.NotBlank;

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
