package org.upece.granko.olvmat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.repository.AdminRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDetailService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminEntity user = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return new AdminDetails(user);
    }

    public List<? extends GrantedAuthority> getAuthorities(){
        return ((AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().stream().toList();
    }

    public Boolean hasAuthority(AdminRoleEnum authority){
        return getAuthorities().contains(new SimpleGrantedAuthority(authority.toString()));
    }
}
