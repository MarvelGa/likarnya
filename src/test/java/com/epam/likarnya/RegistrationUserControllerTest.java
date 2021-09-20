package com.epam.likarnya;

import com.epam.likarnya.model.*;
import com.epam.likarnya.service.CategoryService;
import com.epam.likarnya.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private WebApplicationContext webappContext;

    private User doctor;
    private User doctor2;
    private User doctor3;
    private User nurse;
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
        doctor.setEmail("doctor@gamil.com");
        doctor.setPassword("TTTttt555");
        doctor.setRole(User.Role.valueOf("DOCTOR"));
        doctor.setCategory(category);

        doctor2 = new User();
        doctor2.setFirstName("Valeriy");
        doctor2.setLastName("Pionerov");
        doctor2.setEmail("doctor@gamil.com");
        doctor2.setPassword("TTTttt555");
        doctor2.setRole(User.Role.valueOf("DOCTOR"));
        doctor2.setCategory(category);


        doctor3 = new User();
        doctor3.setId(25L);
        doctor3.setFirstName("Valeriy");
        doctor3.setLastName("Pionerov");
        doctor3.setEmail("doctor@gamil.com");
        doctor3.setPassword("$2a$10$lnLpd0HN33lBsnMq3j7q2.NdymIoEjF99imkRxHhAD1x4bmKbFgAG");
        doctor3.setRole(User.Role.valueOf("DOCTOR"));
        doctor3.setCategory(category);

        nurse = new User();
        nurse.setId(25L);
        nurse.setEmail("miley2@gmail.com");
        nurse.setPassword("TTTttt555");
        nurse.setFirstName("Miley");
        nurse.setLastName("Cyrus");
        nurse.setRole(User.Role.valueOf("NURSE"));

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
    public void shouldGet200WhenAdminOpenPageForRegistrationOfMedicalWorkers() throws Exception {
        this.mockMvc
                .perform(get("/admin/medical-registration")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(model().attributeExists("registrationUser"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet302WhenAdminSuccessfullyRegisteredTheDoctor() throws Exception {
        when(userService.findByEmail("some@email.com")).thenReturn(null);
        when(userService.createOrUpdate(doctor2)).thenReturn(doctor3);
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", doctor.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", doctor.getLastName()).param("confirmedLastName", "")
                        .param("email", doctor.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(doctor.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(doctor.getRole())).param("confirmedRole", "")
                        .param("category", "1").param("confirmedCategory", ""))
                .andExpect(view().name("redirect:/admin/doctors"))
                .andExpect(status().isFound());
        verify(userService, times(1)).findByEmail("doctor@gamil.com");
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet302WhenAdminSuccessfullyRegisteredTheNurse() throws Exception {
        when(userService.findByEmail(nurse.getEmail())).thenReturn(null);
        when(userService.createOrUpdate(nurse)).thenReturn(nurse);
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(view().name("redirect:/admin/nurses"))
                .andExpect(status().isFound());
        verify(userService, times(1)).findByEmail(nurse.getEmail());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenFirstNameNotValid() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", "kkkk").param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenLastNameNotValid() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", "kkkk").param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenEmailNotValid() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", "milidocm").param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenPasswordNotValid() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", "jjj").param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenNoRoleParam() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", "milidocm").param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenRegisteringNurseAlreadyExist() throws Exception {
        when(userService.findByEmail(nurse.getEmail())).thenReturn(nurse);
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "0").param("confirmedCategory", ""))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
        verify(userService, times(1)).findByEmail(nurse.getEmail());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenChosenTheCategoryForNurse() throws Exception {
        this.mockMvc
                .perform(post("/admin/medical-registration")
                        .sessionAttr("user", admin)
                        .param("firstName", nurse.getFirstName()).param("confirmedFirstName", "")
                        .param("lastName", nurse.getLastName()).param("confirmedLastName", "")
                        .param("email", nurse.getEmail()).param("confirmedEmail", "")
                        .param("password", String.valueOf(nurse.getPassword())).param("confirmedPassword", "")
                        .param("role", String.valueOf(nurse.getRole())).param("confirmedRole", "")
                        .param("category", "2").param("confirmedCategory", ""))
                .andExpect(model().attribute("errorMessages", hasSize(1)))
                .andExpect(view().name("/admin/medicalRegistrationPage"))
                .andExpect(status().isOk());
    }
}
