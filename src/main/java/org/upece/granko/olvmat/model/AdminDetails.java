package org.upece.granko.olvmat.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.upece.granko.olvmat.entity.AdminEntity;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class AdminDetails implements UserDetails {
    private final AdminEntity adminEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return adminEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return adminEntity.getEmail();
    }
}
