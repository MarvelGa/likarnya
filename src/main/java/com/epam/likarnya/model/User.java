package com.epam.likarnya.model;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    public enum Role{
        ADMIN, DOCTOR, NURSE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String password;

    @ToString.Exclude
    @JoinColumn(name="category_id")
    @ManyToOne
    private Category category;

//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private Set<Statement> statements = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MedicalCard> medicalCards = new ArrayList<>();

//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private Set<Statement> statements = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private Role role;
}
