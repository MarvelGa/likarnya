package com.epam.likarnya.dto;

public interface UserDto {
    Long getId();

    String getFirstName();

    String getLastName();

    String getCategory();

    String getRole();

    Long getCountOfPatients();
}
