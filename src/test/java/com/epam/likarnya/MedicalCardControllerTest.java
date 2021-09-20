package com.epam.likarnya;

import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.model.*;
import com.epam.likarnya.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalCardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private StatementService statementService;

    @MockBean
    private MedicalCardService medicalCardService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private WebApplicationContext webappContext;

    private User doctor;
    private User admin;
    private Category category;
    private Patient patient;
    private MedicalCard medicalCard;
    private Statement statement;
    private Treatment treatment;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webappContext)
                .apply(springSecurity())
                .build();

        doctor = new User();
        doctor.setId(20L);
        doctor.setFirstName("Valeriy");
        doctor.setLastName("Pionerov");
        doctor.setRole(User.Role.valueOf("DOCTOR"));

        admin = new User();
        admin.setId(2L);
        admin.setFirstName("Petro");
        admin.setLastName("Leontev");
        admin.setRole(User.Role.valueOf("ADMIN"));

        category = new Category();
        category.setId(1L);
        category.setTitle("SURGEON");

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
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminChosenCategoryOfDoctorForPatient() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));
        when(patientService.findById(patient.getId())).thenReturn(patient);
        this.mockMvc
                .perform(get("/admin/create-medical-card/1")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/medicalCard"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", hasProperty("id", is(1L))))
                .andExpect(model().attribute("patient", hasProperty("firstName", is("Andrey"))))
                .andExpect(model().attribute("patient", hasProperty("lastName", is("Andreev"))))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", hasSize(1)))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("SURGEON"))
                        )
                )))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getAll();
        verify(patientService, times(1)).findById(patient.getId());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminChosenDoctorByCategoryForPatient() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));
        when(patientService.findById(patient.getId())).thenReturn(patient);
        when(userService.getDoctorsByCategory("SURGEON")).thenReturn(List.of(getDoctors()));
        this.mockMvc
                .perform(get("/admin/create-medical-card/1/SURGEON")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/medicalCard"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", hasProperty("id", is(1L))))
                .andExpect(model().attribute("patient", hasProperty("firstName", is("Andrey"))))
                .andExpect(model().attribute("patient", hasProperty("lastName", is("Andreev"))))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", hasSize(1)))
                .andExpect(model().attribute("categories", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("title", is("SURGEON"))
                        )
                )))
                .andExpect(model().attribute("doctors", hasSize(1)))
                .andExpect(model().attribute("doctors", hasItem(
                        allOf(
                                hasProperty("id", is(5L)),
                                hasProperty("lastName", is("Sergeev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getAll();
        verify(patientService, times(1)).findById(patient.getId());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet500errorWhenAdminTriedCreatingMedicalCardToPatient() throws Exception {
        when(patientService.findById(patient.getId())).thenReturn(patient);
        when(userService.findById(doctor.getId())).thenReturn(doctor);
        when(statementService.createOrUpdate(statement)).thenReturn(statement);
        when(medicalCardService.createOrUpdate(medicalCard)).thenReturn(medicalCard);
        this.mockMvc
                .perform(post("/admin/create-medical-card/add/1")
                        .sessionAttr("user", admin)
                        .param("patient_id", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminTriedCreatingMedicalCardToPatient() throws Exception {
        when(patientService.findById(patient.getId())).thenReturn(patient);
        when(userService.findById(doctor.getId())).thenReturn(doctor);
        when(statementService.createOrUpdate(statement)).thenReturn(statement);
        when(medicalCardService.createOrUpdate(medicalCard)).thenReturn(medicalCard);
        this.mockMvc
                .perform(post("/admin/create-medical-card/add/{patient_id}", 1L, medicalCard.getUser())
                        .sessionAttr("user", admin)
                        .param("user.id", String.valueOf(medicalCard.getUser().getId())).param("confirmedUser", "")
                        .param("complaints", String.valueOf(medicalCard.getComplaints())).param("confirmedComplaints", ""))
                .andExpect(view().name("redirect:/admin"))
                .andExpect(status().isFound());
    }

    private UserDTO getDoctors() {
        UserDTO doctor = new UserDTO() {
            @Override
            public Long getId() {
                return 5L;
            }

            @Override
            public String getFirstName() {
                return "Sergey";
            }

            @Override
            public String getLastName() {
                return "Sergeev";
            }

            @Override
            public String getCategory() {
                return "SURGEON";
            }

            @Override
            public String getRole() {
                return "DOCTOR";
            }

            @Override
            public Long getCountOfPatients() {
                return 20L;
            }
        };
        return doctor;
    }
}
