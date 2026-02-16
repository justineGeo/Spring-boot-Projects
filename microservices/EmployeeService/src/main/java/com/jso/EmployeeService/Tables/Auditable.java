package com.jso.EmployeeService.Tables;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;



@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, EntityAuditListener.class})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Auditable {
    @CreatedDate
    @Column(updatable = false) private Instant createdDate;
    @LastModifiedDate
    private Instant modifiedDate;

    @Column(updatable = false, length = 100) private String createdUser;
    @Column(length = 100) private String modifiedUser;

    @Column(updatable = false, length = 45) private String createdIp;
    @Column(length = 45) private String modifiedIp;
}