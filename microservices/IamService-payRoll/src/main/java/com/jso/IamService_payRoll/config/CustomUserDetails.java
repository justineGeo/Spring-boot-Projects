package com.jso.IamService_payRoll.config;

import com.jso.IamService_payRoll.Tables.AccountStatus;
import com.jso.IamService_payRoll.Tables.Permission;
import com.jso.IamService_payRoll.Tables.RoleDtl;
import com.jso.IamService_payRoll.Tables.UserTbl;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final AccountStatus status; // The field must be here
    private final String email;
    private final String password;
    private final Long organizationId;
    private final String organizationCode;
    private final List<String> roleNames;
    private final List<String> permissionCodes;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserTbl user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.organizationId = user.getOrganization().getId();
        this.organizationCode = user.getOrganization().getCode();
        this.status = user.getStatus();

        // Prevent LazyInitializationException by mapping immediately
        this.roleNames = user.getRoles().stream()
                .filter(RoleDtl::isActive)
                .map(RoleDtl::getName)
                .toList();

        this.permissionCodes = user.getRoles().stream()
                .filter(RoleDtl::isActive)
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::isActive)
                .map(Permission::getCode)
                .distinct()
                .toList();

        // Merge roles and permissions into authorities
        Set<SimpleGrantedAuthority> auths = new HashSet<>();
        this.roleNames.forEach(name -> auths.add(new SimpleGrantedAuthority(name)));
        this.permissionCodes.forEach(code -> auths.add(new SimpleGrantedAuthority(code)));
        this.authorities = Collections.unmodifiableSet(auths);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return email; }
    @Override
    public boolean isEnabled() { return true; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}