package com.ja.ioniprog.config.security;

import com.ja.ioniprog.service.PatientService;
import com.ja.ioniprog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private CustomUserDetailsService userDetailsService;
    private PasswordEncoder          passwordEncoder;
    private UserService              userService;

    @Autowired
    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                        UserService userService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder    = passwordEncoder;
        this.userService        = userService;
        System.out.println("customAuthProvider created");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        UserDetails userConnected = userDetailsService.loadUserByUsername(username);

        if (userConnected != null) {
            if (!userConnected.isAccountNonExpired()) {
                logger.info("user expired!");
                throw new BadCredentialsException("Invalid login details");
            }

            if (!userConnected.isAccountNonLocked()) {
                logger.info("user blocked");
                throw new BadCredentialsException("Invalid login details");
            }

            if (passwordEncoder.matches(pwd, userConnected.getPassword())) {
                userService.resetLoginAttempts(username);
                return new UsernamePasswordAuthenticationToken(userConnected, null, userConnected.getAuthorities());
            }  else {
                userService.decreaseLoginAttempts(username);
                throw new BadCredentialsException("Invalid login details!");
            }
        } else {
            logger.info("user not found");
            throw new BadCredentialsException("Invalid login details");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
