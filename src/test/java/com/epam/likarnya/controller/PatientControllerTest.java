package com.epam.likarnya.controller;

import com.epam.likarnya.dto.PatientDto;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private PatientService patientService;

    @Autowired
    private WebApplicationContext webappContext;

    private User admin;
    private Patient patient;
    private PatientDto patientDto;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webappContext)
                .apply(springSecurity())
                .build();

        admin = new User();
        admin.setId(2L);
        admin.setFirstName("Petro");
        admin.setLastName("Leontev");
        admin.setRole(User.Role.valueOf("ADMIN"));

        patient = new Patient();
        patient.setFirstName("Andrey");
        patient.setLastName("Andreev");
        patient.setGender(Patient.Gender.MALE);
        patient.setDateOfBirth(LocalDate.of(1990, 4, 14));

        patientDto = new PatientDto();
        patientDto.setFirstName("Andrey");
        patientDto.setLastName("Andreev");
        patientDto.setGender(Patient.Gender.MALE);
        patientDto.setDateOfBirth(String.valueOf(LocalDate.of(1990, 4, 14)));
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenOpenPageForPatientRegistration() throws Exception {
        this.mockMvc
                .perform(get("/likarnya/admin/patient-registration").sessionAttr("user", admin))
                .andExpect(view().name("/admin/patientRegistration"))
                .andExpect(model().attributeExists("registrationPatient"))
                .andExpect(model().attributeExists("dateOfToday"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet302RedirectionWhenRegistrationWillBeSuccess() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientService.createOrUpdate(patient)).thenReturn(patient);
        this.mockMvc
                .perform(post("/likarnya/admin/patient-registration").sessionAttr("user", admin)
                        .param("firstName", patientDto.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", patientDto.getLastName()).param("confirmedLastName", "")
                        .param("dateOfBirth", patientDto.getDateOfBirth()).param("confirmedDateOfBirth", "")
                        .param("gender", String.valueOf(patientDto.getGender())).param("confirmedGender", ""))
                .andExpect(view().name("redirect:/likarnya/admin"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200AndReturnToPreviouslyPageWhenOneOfFourFieldNotBeenFilled() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientService.createOrUpdate(patient)).thenReturn(patient);
        this.mockMvc
                .perform(post("/likarnya/admin/patient-registration").sessionAttr("user", admin)
                        .param("lastName", patientDto.getLastName()).param("confirmedLastName", "")
                        .param("dateOfBirth", patientDto.getDateOfBirth()).param("confirmedDateOfBirth", "")
                        .param("gender", String.valueOf(patientDto.getGender())).param("confirmedGender", ""))
                .andExpect(view().name("/admin/patientRegistration"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200AndReturnToPreviouslyPageWhenFilledNameWasNotValid() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientService.createOrUpdate(patient)).thenReturn(patient);
        this.mockMvc
                .perform(post("/likarnya/admin/patient-registration").sessionAttr("user", admin)
                        .param("lastName", "jdjdj").param("confirmedLastName", "")
                        .param("lastName", patientDto.getLastName()).param("confirmedLastName", "")
                        .param("dateOfBirth", patientDto.getDateOfBirth()).param("confirmedDateOfBirth", "")
                        .param("gender", String.valueOf(patientDto.getGender())).param("confirmedGender", ""))
                .andExpect(view().name("/admin/patientRegistration"))
                .andExpect(status().isOk());
    }

}
