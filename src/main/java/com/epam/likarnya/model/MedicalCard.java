package com.epam.likarnya.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "medical_cards")
public class MedicalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "statement_id")
    private Statement statement;

    private String complaints;

    private String diagnosis;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "medicalCard", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Treatment> treatments = new ArrayList<>();
}
