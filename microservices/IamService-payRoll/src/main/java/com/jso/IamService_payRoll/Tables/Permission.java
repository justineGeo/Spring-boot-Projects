package com.jso.IamService_payRoll.Tables;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "permissions")
@Getter @Setter @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class Permission extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;  // e.g., "PAYROLL_RUN", "VIEW_SLIP"

    private String name;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Transient
    public boolean isActive() { return status == AccountStatus.ACTIVE; }
}