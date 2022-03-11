package com.ja.ioniprog.config.security;

import com.ja.ioniprog.utils.enums.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfiguration())
                .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/*/css/*", "/*/js/*", "/*/webfonts/*").permitAll()
                .antMatchers("/patient/**").hasAuthority(RoleEnum.DOCTOR.getName())
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login-page.html").permitAll()
                .loginProcessingUrl("/login-process")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login-page.html?error=true")
                .and()
            .logout()
                .logoutUrl("/logout-process")
                .logoutSuccessUrl("/login-page.html?logout=true")
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("passwordEncoder created");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        System.out.println("corsConf created");
        return new CustomCorsConfiguration();
    }

    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        System.out.println("auth success handler created");
        return new CustomAuthenticationSuccessHandler();
    }
}
