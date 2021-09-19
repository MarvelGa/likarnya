package com.epam.likarnya;

import com.epam.likarnya.dto.PatientDataDTO;
import com.epam.likarnya.dto.TreatmentPatientDto;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {
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
    private TreatmentService treatmentService;

    @Autowired
    private WebApplicationContext webappContext;

    private User doctor;
    private Category category;
    private Patient patient;
    private TreatmentPatientDto treatmentPatientDto;
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
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGet200WhenInvokeTheListOfPatientsByDoctor() throws Exception {
        when(patientService.getPatientsForDiagnosis(doctor)).thenReturn(List.of(getPatientDataDTO()));
        this.mockMvc
                .perform(get("/doctor-cabinet")
                        .sessionAttr("doctor", doctor))
                .andExpect(view().name("/doctor/doctorPage"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasSize(1)))
                .andExpect(model().attribute("patients", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("complaints", is("leg pain")),
                                hasProperty("lastName", is("Sergeev"))
                        )
                )))
                .andExpect(status().isOk());

        verify(patientService, times(1)).getPatientsForDiagnosis(doctor);
    }

    @Test
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGet200WhenInvokeAddTreatmentToPatientsPageByDoctor() throws Exception {
        when(patientService.findById(patient.getId())).thenReturn(patient);
        when(medicalCardService.getMedicalCardForDiagnosis(patient.getId())).thenReturn(medicalCard);
        this.mockMvc
                .perform(get("/doctor-cabinet/add-treatment/1")
                        .sessionAttr("doctor", doctor)
                .param("patient_id","1"))
                .andExpect(view().name("/doctor/addTreatment"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("medicalCard"))
                .andExpect(model().attributeExists("treatment"))
                .andExpect(model().attribute("medicalCard", hasProperty("id", is(1L))))
                .andExpect(model().attribute("medicalCard", hasProperty("complaints", is("leg pain"))))
                .andExpect(status().isOk());

        verify(medicalCardService, times(1)).getMedicalCardForDiagnosis(patient.getId());
    }

    @Test
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGetRedirectWhenDoctorAddedTreatmentToPatient() throws Exception {
        var updatedMedicCard = medicalCard;
        updatedMedicCard.setDiagnosis(medicalCard.getDiagnosis());
        updatedMedicCard.getStatement().setPatientStatus(Statement.PatientStatus.DIAGNOSED);
        updatedMedicCard.getStatement().setChanged(LocalDateTime.now());
        when(medicalCardService.getMedicalCardForDiagnosis(patient.getId())).thenReturn(medicalCard);
        when(medicalCardService.createOrUpdate(updatedMedicCard)).thenReturn(updatedMedicCard);
        var updatedTreatment =treatment;
        updatedTreatment.setMedicalCard(updatedMedicCard);
        when(treatmentService.createOrUpdate(treatment)).thenReturn(updatedTreatment);
        this.mockMvc
                .perform(post("/doctor-cabinet/add-treatment/1")
                        .sessionAttr("doctor", doctor)
                        .param("patient_id","1"))
                .andExpect(view().name("redirect:/doctor-cabinet"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGet200WhenDoctorInvokeThePatientTreatmentPage() throws Exception {
        when(patientService.getPatientsForTreatment(doctor.getId())).thenReturn(List.of(getTreatmentPatientDto()));
        this.mockMvc
                .perform(get("/doctor-cabinet/treatment-patients")
                        .sessionAttr("doctor", doctor))
                .andExpect(view().name("/doctor/treatmentPatients"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", hasSize(1)))
                .andExpect(model().attribute("patients", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("complaints", is("leg pain")),
                                hasProperty("lastName", is("Andreev"))
                        )
                )))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGet200WhenDoctorInvokeThePatientExecuteTreatmentPage() throws Exception {
        when(patientService.getPatientForTreatment(doctor.getId(), patient.getId())).thenReturn(getTreatmentPatientDto());
        this.mockMvc
                .perform(get("/doctor-cabinet/execute-treatment/1")
                        .sessionAttr("doctor", doctor)
                        .param("patient_id","1L"))
                .andExpect(view().name("/doctor/executeTreatment"))
                .andExpect(model().attributeExists("patientForTreatment"))
                .andExpect(model().attribute("patientForTreatment", hasProperty("id", is(1L))))
                .andExpect(model().attribute("patientForTreatment", hasProperty("complaints", is("leg pain"))))
                .andExpect(status().isOk());
        verify(patientService, times(1)).getPatientForTreatment(doctor.getId(), patient.getId());
    }

    @Test
    @WithMockUser(username = "doctor-surgeon@gmail.com", roles = {"DOCTOR"})
    public void shouldGetRedirectionWhenDoctorExecutedTreatment() throws Exception {
        var updatedTreatment = treatment;
        updatedTreatment.setAppointmentStatus(Treatment.AppointmentStatus.EXECUTED);
        updatedTreatment.setExecutorId(doctor.getId());
        when(treatmentService.getById(treatment.getId())).thenReturn(treatment);
        when(treatmentService.createOrUpdate(updatedTreatment)).thenReturn(updatedTreatment);
        var updatedStatement =statement;
        updatedStatement.setPatientStatus(Statement.PatientStatus.DISCHARGED);
        when(statementService.getById(statement.getId())).thenReturn(statement);
        when(statementService.createOrUpdate(statement)).thenReturn(updatedStatement);
        this.mockMvc
                .perform(post("/doctor-cabinet/execute-treatment")
                        .sessionAttr("doctor", doctor)
                        .param("treatmentId", "1")
                        .param("statementId", "1"))
                .andExpect(view().name("redirect:/doctor-cabinet/treatment-patients"))
                .andExpect(status().is3xxRedirection());
    }


    private PatientDataDTO getPatientDataDTO(){
        PatientDataDTO patientDataDTO = new PatientDataDTO() {
            @Override
            public Long getId() {
                return 1L;
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
            public String getDateOfBirth() {
                return String.valueOf(LocalDate.of(1990,4,14));
            }

            @Override
            public String getGender() {
                return "MAIL";
            }

            @Override
            public String getComplaints() {
                return "leg pain";
            }
        };
        return patientDataDTO;
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
