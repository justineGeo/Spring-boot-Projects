package com.jso.IamService_payRoll.config;

import com.jso.IamService_payRoll.JDBC.UserRepository;
import com.jso.IamService_payRoll.Tables.UserTbl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserTbl user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user: " + email));
        return new CustomUserDetails(user);
    }

    // Dynamic lookup for Multi-tenancy
    public UserDetails loadUserByEmailAndOrganization(String email, Long orgId) {
        UserTbl user = userRepository.findByEmailAndOrganization_Id(email, orgId)
                .orElseThrow(() -> new UsernameNotFoundException("User not in this company."));
        return new CustomUserDetails(user);
    }
}