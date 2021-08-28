package com.epam.likarnya.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private PatientStatus patientStatus = PatientStatus.NEW;

    @Column(name = "diagnosis", length = 50, nullable = false)
    private String  provisionalDiagnosis;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "statement", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY )
    private List<MedicalCard> medicalCards = new ArrayList<>();
}
