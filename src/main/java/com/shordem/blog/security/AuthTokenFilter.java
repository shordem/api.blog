package com.shordem.blog.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shordem.blog.service.UserDetailsServiceImpl;
import com.shordem.blog.utils.JwtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String sub = jwtUtils.getSubjectFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(sub);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Cannot set the Security Context");
            }
        } catch (Exception e) {
            logger.error("Cannot set the Security Context | Catch Exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        System.out.println("headerAuth: " + headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            logger.info("AuthTokenFilter | parseJwt | parseJwt: {}", headerAuth.substring(7, headerAuth.length()));

            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
}
