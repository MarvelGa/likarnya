package com.epam.likarnya.dto;

import com.epam.likarnya.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {
    @NotBlank(message = "The field is required")
    private String firstName;

    @NotBlank(message = "The field is required")
    private String lastName;

    @NotBlank(message = "The field is required")
    private String email;

    @NotBlank (message = "The field is required")
    private String password;

    private User.Role role;

    private Long category;
}
