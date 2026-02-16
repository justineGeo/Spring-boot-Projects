package com.jso.EmployeeService.Tables;

import com.jso.EmployeeService.config.RequestContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class EntityAuditListener {
    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable a) {
            a.setCreatedIp(RequestContext.getClientIp());
            a.setModifiedIp(RequestContext.getClientIp());
            a.setCreatedUser(RequestContext.getUserId()); // Backup for CreatedBy
        }
    }

    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable a) {
            a.setModifiedIp(RequestContext.getClientIp());
            a.setModifiedUser(RequestContext.getUserId());
        }
    }
}