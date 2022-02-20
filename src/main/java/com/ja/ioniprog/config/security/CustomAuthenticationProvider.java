package com.ja.ioniprog.config.security;

import com.ja.ioniprog.service.UserService;
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
        System.out.println("user try to login: " + username);

        String pwd = authentication.getCredentials().toString();
        System.out.println("user pwd try to login: " + pwd);

        UserDetails userConnected = userDetailsService.loadUserByUsername(username);
        System.out.println(userConnected);

        if (userConnected != null) {
            if (!userConnected.isAccountNonExpired()) {
                System.out.println("userul e expirat");
                throw new BadCredentialsException("Invalid login details");
            }

            if (!userConnected.isAccountNonLocked()) {
                System.out.println("userul e blocat");
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
            System.out.println("userul nu a putut fi gasit");
            throw new BadCredentialsException("Invalid login details");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
