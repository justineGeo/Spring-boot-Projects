package com.jso.IamService_payRoll.Tables;

import com.jso.IamService_payRoll.config.IpAuditingListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, IpAuditingListener.class})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Auditable {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private Instant modifiedDate;

    @CreatedBy
    @Column(name = "created_user", updatable = false, length = 100)
    private String createdUser;

    @LastModifiedBy
    @Column(name = "modified_user", length = 100)
    private String modifiedUser;

    @Column(name = "created_ip", length = 45)
    private String createdIp;

    @Column(name = "modified_ip", length = 45)
    private String modifiedIp;
}