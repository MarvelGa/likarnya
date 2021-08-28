//package com.epam.likarnya.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Builder
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name="staff")
//public class Staff {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name ="user_id")
//    private User user;
//
//    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
//    private Doctor doctor;
//}
