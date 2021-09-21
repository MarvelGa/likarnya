package com.epam.likarnya.controller;

import com.epam.likarnya.dto.NurseDTO;
import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.dto.UserDTO;
import com.epam.likarnya.model.Category;
import com.epam.likarnya.model.Patient;
import com.epam.likarnya.model.Treatment;
import com.epam.likarnya.model.User;
import com.epam.likarnya.service.CategoryService;
import com.epam.likarnya.service.PatientService;
import com.epam.likarnya.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PatientService patientService;

    @Autowired
    private WebApplicationContext webappContext;

    private User admin;
    private Category category;
    private Patient patient;
    private TreatmentPatientDto treatmentPatientDto;

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

        category = new Category();
        category.setId(1L);
        category.setTitle("SURGEON");

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Andrey");
        patient.setLastName("Andreev");
        patient.setGender(Patient.Gender.MALE);
        patient.setDateOfBirth(LocalDate.of(1990, 4, 14));
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenUserWithRoleAdminTryToGetListOfNurses() throws Exception {
        when(userService.getNurses()).thenReturn(List.of(getNurse()));

        this.mockMvc
                .perform(get("/admin/nurses").sessionAttr("user", admin))
                .andExpect(view().name("/admin/listNurses"))
                .andExpect(model().attributeExists("nurses"))
                .andExpect(model().attribute("nurses", hasSize(1)))
                .andExpect(model().attribute("nurses", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("firstName", is("Milli")),
                                hasProperty("lastName", is("Ridik"))
                        )
                )))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminTryToGetListOfDoctors() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));
        when(userService.findDoctorsWithCountOfPatients()).thenReturn(List.of(getDoctors()));
        this.mockMvc
                .perform(get("/admin/doctors")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/listDoctors"))
                .andExpect(model().attributeExists("doctorsList"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("cat"))
                .andExpect(model().attribute("doctorsList", hasSize(1)))
                .andExpect(model().attribute("doctorsList", hasItem(
                        allOf(
                                hasProperty("id", is(5L)),
                                hasProperty("firstName", is("Sergey")),
                                hasProperty("lastName", is("Sergeev"))
                        )
                )))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminTryToGetListOfDoctorsByCategory() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));
        when(userService.findDoctorsWithCountOfPatients()).thenReturn(List.of(getDoctors()));
        when(userService.findDoctorsWithCountOfPatientsByCategoryId(category.getId())).thenReturn(List.of(getDoctors()));
        this.mockMvc
                .perform(get("/admin/doctors")
                        .sessionAttr("user", admin)
                        .param("category", String.valueOf(1)))
                .andExpect(view().name("/admin/listDoctors"))
                .andExpect(model().attributeExists("doctorsList"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("cat"))
                .andExpect(model().attribute("doctorsList", hasSize(1)))
                .andExpect(model().attribute("doctorsList", hasItem(
                        allOf(
                                hasProperty("id", is(5L)),
                                hasProperty("firstName", is("Sergey")),
                                hasProperty("lastName", is("Sergeev"))
                        )
                )))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenAdminTryToGetListOfDoctorsByCategoryAndSorting() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(category));
        when(userService.findDoctorsWithCountOfPatients()).thenReturn(List.of(getDoctors()));
        when(userService.findDoctorsWithCountOfPatientsByCategoryId(category.getId())).thenReturn(List.of(getDoctors()));
        this.mockMvc
                .perform(get("/admin/doctors")
                        .sessionAttr("user", admin)
                        .param("category", String.valueOf(1))
                        .param("sorting", "ASC-NAME"))
                .andExpect(view().name("/admin/listDoctors"))
                .andExpect(model().attributeExists("doctorsList"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("cat"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attribute("doctorsList", hasSize(1)))
                .andExpect(model().attribute("doctorsList", hasItem(
                        allOf(
                                hasProperty("id", is(5L)),
                                hasProperty("firstName", is("Sergey")),
                                hasProperty("lastName", is("Sergeev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(userService, times(1)).findDoctorsWithCountOfPatientsByCategoryId(1L);
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenInvokeTheListOfPatientsWithSorting() throws Exception {
        when(patientService.patientsWithOutMedicCard()).thenReturn(List.of(patient));
        this.mockMvc
                .perform(get("/admin")
                        .sessionAttr("user", admin)
                        .param("sorting", "ASC-NAME"))
                .andExpect(view().name("/admin/listPatient"))
                .andExpect(model().attributeExists("listPatients"))
                .andExpect(model().attributeExists("sort"))
                .andExpect(model().attribute("listPatients", hasSize(1)))
                .andExpect(model().attribute("listPatients", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("firstName", is("Andrey")),
                                hasProperty("lastName", is("Andreev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).patientsWithOutMedicCard();
    }


    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenInvokeTheListOfPatientsWithOutSorting() throws Exception {
        when(patientService.patientsWithOutMedicCard()).thenReturn(List.of(patient));
        this.mockMvc
                .perform(get("/admin")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/listPatient"))
                .andExpect(model().attributeExists("listPatients"))
                .andExpect(model().attribute("listPatients", hasSize(1)))
                .andExpect(model().attribute("listPatients", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("firstName", is("Andrey")),
                                hasProperty("lastName", is("Andreev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).patientsWithOutMedicCard();
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldGet200WhenInvokeTheHistoryPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 8);
        Page<TreatmentPatientDto> pagedResponse = new PageImpl(List.of(getTreatmentPatientDto()), pageable, 5);
        when(patientService.getHistory(pageable)).thenReturn(pagedResponse);
        this.mockMvc
                .perform(get("/admin/patients/history")
                        .sessionAttr("user", admin))
                .andExpect(view().name("/admin/patientsHistory"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("complaints", is("leg pain")),
                                hasProperty("diagnosis", is("broken leg"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).getHistory(pageable);
    }

    private NurseDTO getNurse() {
        NurseDTO nurseDTO = new NurseDTO() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getFirstName() {
                return "Milli";
            }

            @Override
            public String getLastName() {
                return "Ridik";
            }

            @Override
            public String getRole() {
                return "NURSE";
            }
        };
        return nurseDTO;
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

    private TreatmentPatientDto getTreatmentPatientDto() {
        TreatmentPatientDto treatmentPatientDto = new TreatmentPatientDto() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getFirstName() {
                return "Andrey";
            }

            @Override
            public String getLastName() {
                return "Andreev";
            }

            @Override
            public String getDateOfBirth() {
                return String.valueOf(LocalDate.of(1990, 4, 14));
            }

            @Override
            public String getGender() {
                return String.valueOf(Patient.Gender.MALE);
            }

            @Override
            public String getComplaints() {
                return "leg pain";
            }

            @Override
            public String getDiagnosis() {
                return "broken leg";
            }

            @Override
            public String getAppointment() {
                return String.valueOf(Treatment.Appointment.OPERATION);
            }

            @Override
            public String getAppointmentStatus() {
                return String.valueOf(Treatment.AppointmentStatus.EXECUTED);
            }

            @Override
            public String getDoctorFirstName() {
                return "Sergey";
            }

            @Override
            public String getDoctorLastName() {
                return "Sergeev";
            }

            @Override
            public String getDoctorCategory() {
                return "SURGEON";
            }

            @Override
            public String getTreatmentId() {
                return "1";
            }

            @Override
            public String getStatementId() {
                return "2";
            }

            @Override
            public String getNameOfExecutor() {
                return "Sergey";
            }

            @Override
            public String getLastNameOfExecutor() {
                return "Sergeev";
            }

            @Override
            public String getRoleOfExecutor() {
                return "DOCTOR";
            }

            @Override
            public LocalDateTime getDateOfAdmission() {
                return LocalDateTime.now().minusDays(20);
            }

            @Override
            public LocalDateTime getDateOfDischarge() {
                return LocalDateTime.now().minusDays(1);
            }
        };
        return treatmentPatientDto;
    }
}
