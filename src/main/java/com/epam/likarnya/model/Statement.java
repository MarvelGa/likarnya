package com.epam.likarnya.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name ="statements")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Statement {
    public enum PatientStatus {
        NEW, DIAGNOSED ,DISCHARGED
    }

    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ToString.Exclude
//    @JoinColumn(name="user_id")
//    @ManyToOne
//    private User user;

    @ToString.Exclude
    @JoinColumn(name="patient_id")
    @ManyToOne
    private Patient patient;

    @Column(name="patient_status")
    @Enumerated(EnumType.STRING)
    private PatientStatus patientStatus = PatientStatus.NEW;

//    @Column(name = "diagnosis", length = 50, nullable = false)
//    private String  provisionalDiagnosis;

    @EqualsAndHashCode.Exclude
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @EqualsAndHashCode.Exclude
    @LastModifiedDate
    @Column(name = "changed")
    private LocalDateTime changed;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "statement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY )
    private List<MedicalCard> medicalCards = new ArrayList<>();
}
