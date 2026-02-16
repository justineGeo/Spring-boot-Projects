package com.jso.IamService_payRoll.JDBC;

import com.jso.IamService_payRoll.Tables.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByCode(String code);
    Optional<Organization> findByTinNumber(String tinNumber);
    boolean existsByCode(String code);
    boolean existsByTinNumber(String tinNumber);
}