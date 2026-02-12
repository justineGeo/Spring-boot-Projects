package com.jso.IamService_payRoll.Tables;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_email_org", columnNames = {"email", "organization_id"})
})
@Getter @Setter @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class UserTbl extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "password_reset_token_hash")
    private String passwordResetTokenHash;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenHashExpiry;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleDtl> roles = new HashSet<>();

    @Transient
    public boolean isActive() { return this.status == AccountStatus.ACTIVE; }


    public String generateResetToken(PasswordEncoder encoder) {
        String rawToken = UUID.randomUUID().toString();
        this.passwordResetTokenHash = encoder.encode(rawToken);
        this.passwordResetTokenHashExpiry = LocalDateTime.now().plusMinutes(30); // 30-minute window
        return rawToken;
    }


    public boolean matchesResetToken(String rawToken, PasswordEncoder encoder) {
        if (this.passwordResetTokenHash == null || this.passwordResetTokenHashExpiry == null) {
            return false;
        }
        return encoder.matches(rawToken, this.passwordResetTokenHash)
                && LocalDateTime.now().isBefore(this.passwordResetTokenHashExpiry);
    }

    public void clearResetToken() {
        this.passwordResetTokenHash = null;
        this.passwordResetTokenHashExpiry = null;
    }
}