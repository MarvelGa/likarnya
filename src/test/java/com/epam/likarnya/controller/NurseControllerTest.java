package com.epam.likarnya.controller;

import com.epam.likarnya.dto.TreatmentPatientDto;
import com.epam.likarnya.model.*;
import com.epam.likarnya.service.*;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NurseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementService statementService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private TreatmentService treatmentService;

    @Autowired
    private WebApplicationContext webappContext;

    private User nurse;
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

        nurse = new User();
        nurse.setId(25L);
        nurse.setFirstName("Miley");
        nurse.setLastName("Cyrus");
        nurse.setRole(User.Role.valueOf("NURSE"));

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
        medicalCard.setUser(nurse);
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
    @WithMockUser(username = "nurse-miley@gmail.com", roles = {"NURSE"})
    public void shouldGet200WhenInvokeTheListOfPatientsForDrugOrProcedureTreatmentByNurse2() throws Exception {
        when(patientService.getPatientsForTreatmentByNurse()).thenReturn(List.of(getTreatmentPatientDto()));
        this.mockMvc
                .perform(get("/likarnya/nurse-cabinet")
                        .sessionAttr("nurse", nurse))
                .andExpect(view().name("/nurse/nursePage"))
                .andExpect(model().attributeExists("patientsForTreatingByProcedureOrDrug"))
                .andExpect(model().attribute("patientsForTreatingByProcedureOrDrug", hasSize(1)))
                .andExpect(model().attribute("patientsForTreatingByProcedureOrDrug", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("complaints", is("leg pain")),
                                hasProperty("lastName", is("Andreev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).getPatientsForTreatmentByNurse();
    }

    @Test
    @WithMockUser(username = "nurse-miley@gmail.com", roles = {"NURSE"})
    public void shouldGet200WhenNurseInvokeThePatientExecuteTreatmentPage() throws Exception {
        when(patientService.getPatientByIdForTreatmentByNurse(patient.getId())).thenReturn(getTreatmentPatientDto());
        this.mockMvc
                .perform(get("/likarnya/nurse-cabinet/execute-treatment/1")
                        .sessionAttr("nurse", nurse)
                        .param("patient_id", "1L"))
                .andExpect(view().name("/nurse/nurseTreatment"))
                .andExpect(model().attributeExists("patientForTreatmentByNurse"))
                .andExpect(model().attribute("patientForTreatmentByNurse", hasProperty("id", is(1L))))
                .andExpect(model().attribute("patientForTreatmentByNurse", hasProperty("complaints", is("leg pain"))))
                .andExpect(status().isOk());
        verify(patientService, times(1)).getPatientByIdForTreatmentByNurse(patient.getId());
    }

    @Test
    @WithMockUser(username = "nurse-miley@gmail.com", roles = {"NURSE"})
    public void shouldGetRedirectionWhenDoctorExecutedTreatment() throws Exception {
        var updatedTreatment = treatment;
        updatedTreatment.setAppointmentStatus(Treatment.AppointmentStatus.EXECUTED);
        updatedTreatment.setExecutorId(nurse.getId());
        when(treatmentService.getById(treatment.getId())).thenReturn(treatment);
        when(treatmentService.createOrUpdate(updatedTreatment)).thenReturn(updatedTreatment);
        var updatedStatement = statement;
        updatedStatement.setPatientStatus(Statement.PatientStatus.DISCHARGED);
        when(statementService.getById(statement.getId())).thenReturn(statement);
        when(statementService.createOrUpdate(statement)).thenReturn(updatedStatement);
        this.mockMvc
                .perform(post("/likarnya/nurse-cabinet/execute-treatment")
                        .sessionAttr("nurse", nurse)
                        .param("treatmentId", "1")
                        .param("statementId", "1"))
                .andExpect(view().name("redirect:/likarnya/nurse-cabinet"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "nurse-miley@gmail.com", roles = {"NURSE"})
    public void shouldGet200WhenInvokeTheHistoryPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);
        Page<TreatmentPatientDto> pagedResponse = new PageImpl(List.of(getTreatmentPatientDto()), pageable, 5);
        when(patientService.getNurseTreatmentHistoryById(nurse.getId(), pageable)).thenReturn(pagedResponse);
        this.mockMvc
                .perform(get("/likarnya/nurse-cabinet/history")
                        .sessionAttr("nurse", nurse))
                .andExpect(view().name("/nurse/nurseTreatmentHistory"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("page", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("complaints", is("leg pain")),
                                hasProperty("diagnosis", is("broken leg"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).getNurseTreatmentHistoryById(nurse.getId(), pageable);
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
