package com.epam.likarnya.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "treatments")
public class Treatment {

    public enum Appointment {
        PROCEDURE, DRUG, OPERATION
    }

    public enum AppointmentStatus {
        EXECUTED, NOT_EXECUTED
    }

    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus = AppointmentStatus.NOT_EXECUTED;

    @EqualsAndHashCode.Exclude
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    @Column(name = "changed")
    private LocalDateTime changed;

    @Column(name = "executor_id")
    private Long executorId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="m_card_id")
    private MedicalCard medicalCard;
}
