package org.upece.granko.olvmat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.upece.granko.olvmat.entity.AdminEntity;
import org.upece.granko.olvmat.model.AdminDetails;
import org.upece.granko.olvmat.repository.AdminRepository;

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
}
