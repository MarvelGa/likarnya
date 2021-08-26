package com.epam.likarnya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "The field 'email' can't be empty")
    private String email;
    @NotBlank (message = "The field 'password' can't be empty")
    private String password;
}
