package com.epam.likarnya.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="statements")
@Entity
public class Statement {
    public enum PatientStatus {
        NEW, DISCHARGED
    }

    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @JoinColumn(name="doctor_id")
    @ManyToOne
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private PatientStatus patientStatus = PatientStatus.NEW;

    @Column(name = "diagnosis", length = 50, nullable = false)
    private String  provisionalDiagnosis;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
