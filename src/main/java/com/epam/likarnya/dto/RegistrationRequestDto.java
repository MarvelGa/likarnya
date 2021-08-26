package com.epam.likarnya.dto;

import com.epam.likarnya.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {
    @NotBlank(message = "The field 'email' can't be empty")
//    @Pattern(regexp = "[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@\"\n" +
//            " + \"[A-Za-z0-9-]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})", message = "Invalid email")
    private String email;
    @NotBlank (message = "The field 'password' can't be empty")
//    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*[\\W|_]).{8,20}", message = "Invalid password")
    private String password;
    private User.Role role;
}
