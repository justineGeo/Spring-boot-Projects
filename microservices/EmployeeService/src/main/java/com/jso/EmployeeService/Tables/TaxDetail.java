package com.jso.EmployeeService.Tables;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tax_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // This enables the TaxDetail.builder() method used in your Service
public class TaxDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taxNumber;
    private String socialSecurityNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}