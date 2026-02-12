package com.jso.IamService_payRoll.Services;

import com.jso.IamService_payRoll.Dtos.AuthRequest;
import com.jso.IamService_payRoll.Dtos.AuthResponse;
import com.jso.IamService_payRoll.Dtos.RegisterRequest;
import com.jso.IamService_payRoll.JDBC.OrganizationRepository;
import com.jso.IamService_payRoll.JDBC.RoleRepository;
import com.jso.IamService_payRoll.JDBC.UserRepository;
import com.jso.IamService_payRoll.Tables.AccountStatus;
import com.jso.IamService_payRoll.Tables.RoleDtl;
import com.jso.IamService_payRoll.Tables.UserTbl;
import com.jso.IamService_payRoll.config.CustomUserDetails;
import com.jso.IamService_payRoll.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public List<RoleDtl> getRegistrationRoles() {
        log.info("Fetching available roles for registration dropdown");
        return roleRepository.findAll().stream()
                .filter(role -> !role.getName().equalsIgnoreCase("SUPER_ADMIN"))
                .toList();
    }

    @Transactional
    public void registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered in the system.");
        }

        var organization = organizationRepository.findById(request.organizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found."));

        List<RoleDtl> roleList = roleRepository.findAllById(request.roleIds());
        Set<RoleDtl> selectedRoles = new HashSet<>(roleList);

        if (selectedRoles.isEmpty()) {
            throw new RuntimeException("At least one valid role must be selected.");
        }

        boolean attemptingSuperAdmin = selectedRoles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("SUPER_ADMIN"));

        if (attemptingSuperAdmin) {
            log.warn("Security Alert: Unauthorized SUPER_ADMIN registration attempt by {}", request.email());
            throw new RuntimeException("Unauthorized role selection. You cannot register with high-level privileges.");
        }

        UserTbl user = UserTbl.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .organization(organization)
                .status(AccountStatus.ACTIVE)
                .roles(selectedRoles)
                .build();

        userRepository.save(user);
        log.info("New user registered successfully: {} for organization: {}", request.email(), organization.getName());
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Attempting login for user: {}", request.email());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getStatus() != AccountStatus.ACTIVE) {
                throw new RuntimeException("Account is " + userDetails.getStatus());
            }

            String token = jwtService.generateAccessToken(userDetails);

            return AuthResponse.builder()
                    .token(token)
                    .email(userDetails.getEmail())
                    .organizationCode(userDetails.getOrganizationCode())
                    .roles(userDetails.getRoleNames())
                    .build();
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("The email or password you entered is incorrect.");
        }
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        UserTbl user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password does not match.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public String initiatePasswordReset(String email) {
        UserTbl user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("If account exists, reset instructions sent."));

        String resetToken = user.generateResetToken(passwordEncoder);
        userRepository.save(user);

        return resetToken;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        UserTbl user = userRepository.findByPasswordResetTokenHash(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        if (!user.matchesResetToken(token, passwordEncoder)) {
            throw new RuntimeException("Token expired.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.clearResetToken();
        userRepository.save(user);
    }
}