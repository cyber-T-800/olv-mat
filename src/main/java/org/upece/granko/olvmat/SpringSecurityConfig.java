package org.upece.granko.olvmat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.upece.granko.olvmat.entity.enums.AdminRoleEnum;
import org.upece.granko.olvmat.service.AdminDetailService;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final AdminDetailService adminDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/admin", "/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/vstup", "/vstup/**").hasAnyAuthority("ADMIN", "VSTUP")
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin((form) -> form
                        .loginPage("/admin/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .exceptionHandling(ex->
                        ex.accessDeniedHandler(accessDeniedHandler())
                )
                .userDetailsService(adminDetailService)
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, accessDeniedException) -> {
            if(adminDetailService.hasAuthority(AdminRoleEnum.VSTUP)){
                response.sendRedirect("/vstup");
            }
        };
    }

    public AuthenticationSuccessHandler successHandler(){
        return (request, response, authentication) -> {
            String redirectURL = request.getContextPath();

            // Check roles and redirect
            for (GrantedAuthority auth : authentication.getAuthorities()) {

                String role = auth.getAuthority();

                if (role.equals("ADMIN")) {
                    redirectURL = "/admin";
                    break;
                }
                else if (role.equals("VSTUP")) {
                    redirectURL = "/vstup";
                    break;
                }
            }

            response.sendRedirect(redirectURL);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(14);
    }

}
