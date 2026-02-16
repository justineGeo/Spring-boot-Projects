package com.jso.EmployeeService.Tables;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employment_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Standard builder works here as it doesn't extend Auditable
public class EmploymentDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate joiningDate;

    @Column(precision = 19, scale = 2)
    private BigDecimal basicSalary;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false) // Foreign key is here
    private Employee employee;
}