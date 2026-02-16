//package com.jso.EmployeeService.config;
//
//import com.jso.EmployeeService.Tables.Auditable;
//import jakarta.persistence.PrePersist;
//import jakarta.persistence.PreUpdate;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class IpAuditingListener {
//
//    @PrePersist
//    public void setCreatedIp(Object entity) {
//        if (entity instanceof Auditable auditable) {
//            String ip = RequestContext.getClientIP();
//            auditable.setCreatedIp(ip);
//            log.debug("Auditing: Setting created_ip {} for entity", ip);
//        }
//    }
//
//    @PreUpdate
//    public void setModifiedIp(Object entity) {
//        if (entity instanceof Auditable auditable) {
//            String ip = RequestContext.getClientIP();
//            auditable.setModifiedIp(ip);
//            log.debug("Auditing: Updating modified_ip {} for entity", ip);
//        }
//    }
//}