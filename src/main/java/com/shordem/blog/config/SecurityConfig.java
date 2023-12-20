package com.shordem.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.security.AuthTokenFilter;
import com.shordem.blog.security.JwtAccessDeniedHandler;
import com.shordem.blog.security.JwtAuthenticationEntryPoint;
import com.shordem.blog.service.UserDetailsServiceImpl;
import com.shordem.blog.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private final JwtAccessDeniedHandler accessDeniedHandler;

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils,
            UserDetailsServiceImpl userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.headers(h -> h.frameOptions(f -> f.disable()))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(HttpMethod.GET, "/test/").hasRole(ERole.USER.name())
                                .requestMatchers(HttpMethod.POST, "/tag/").hasRole(ERole.ADMIN.name())
                                .requestMatchers("/profile/").hasRole(ERole.USER.name())
                                .requestMatchers("/post/").hasRole(ERole.USER.name())
                                .anyRequest().permitAll())
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(unauthorizedHandler);
                    exception.accessDeniedHandler(accessDeniedHandler);
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(authenticationJwtTokenFilter(jwtUtils, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
