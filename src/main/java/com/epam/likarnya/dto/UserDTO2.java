package com.epam.likarnya.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO2 {
    private Long id;

    private String firstName;

    private String lastName;

    private String category;
}
