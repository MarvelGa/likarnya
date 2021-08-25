package com.epam.likarnya.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String title;

    @ToString.Exclude
    @OneToMany(mappedBy = "category")
    @EqualsAndHashCode.Exclude
    private List<Doctor> doctors = new ArrayList<>();
}
