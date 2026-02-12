package com.jso.IamService_payRoll.config;

import com.jso.IamService_payRoll.Tables.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class IpAuditingListener {

    @PrePersist
    public void setCreatedIp(Auditable auditable) {
        auditable.setCreatedIp(RequestContext.getClientIP());
    }

    @PreUpdate
    public void setModifiedIp(Auditable auditable) {
        auditable.setModifiedIp(RequestContext.getClientIP());
    }
}