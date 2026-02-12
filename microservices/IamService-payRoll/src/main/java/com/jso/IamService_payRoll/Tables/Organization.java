package com.jso.IamService_payRoll.Tables;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Entity
@Table(name = "organizations", uniqueConstraints = {
        @UniqueConstraint(name = "uk_org_code", columnNames = {"code"}),
        @UniqueConstraint(name = "uk_org_tin", columnNames = {"tin_number"})
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // e.g., "JSO Tech Solutions Ltd"

    @Column(nullable = false, length = 20)
    private String code; // Unique slug e.g., "JSOTECH"

    // --- Tanzanian Statutory Details ---
    @Column(name = "tin_number", nullable = false, length = 15)
    private String tinNumber; // 9-digit TRA Taxpayer ID

    @Column(name = "vrn_number", length = 20)
    private String vrnNumber; // Value Added Tax Registration

    @Column(name = "nssf_number", length = 30)
    private String nssfNumber; // Employer Social Security Code

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    // One organization has many users
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<UserTbl> users;

    @Transient
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
}