package com.shordem.blog.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;
import com.shordem.blog.entity.User;
import com.shordem.blog.exception.RoleException;
import com.shordem.blog.payload.request.LoginRequest;
import com.shordem.blog.payload.request.RegisterRequest;
import com.shordem.blog.payload.response.JwtResponse;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.RoleService;
import com.shordem.blog.service.UserService;
import com.shordem.blog.utils.JwtUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register/")
    public ResponseEntity<?> doRegister(@Valid @RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already taken!"));
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));

        if (strRoles != null) {
            strRoles.forEach((role) -> {
                switch (role) {
                    case "admin":
                        Role adminRole = null;

                        if (roleService.findByName(ERole.ADMIN).isEmpty()) {
                            adminRole = new Role(ERole.ADMIN);
                        } else {
                            adminRole = roleService.findByName(ERole.ADMIN)
                                    .orElseThrow(() -> new RoleException("Error: Admin Role is not found."));
                        }

                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = null;

                        if (roleService.findByName(ERole.USER).isEmpty()) {
                            userRole = new Role(ERole.USER);
                        } else {
                            userRole = roleService.findByName(ERole.USER)
                                    .orElseThrow(() -> new RoleException("Error: User Role is not found."));
                        }

                        roles.add(userRole);
                }
            });
        } else {
            roleService.findByName(ERole.USER).ifPresentOrElse(roles::add, () -> roles.add(new Role(ERole.USER)));
        }

        user.setRoles(roles);
        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login/")
    public ResponseEntity<?> doLogin(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // UserDetailsImpl userDetails = (UserDetailsImpl)
        // authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        // List<String> roles = userDetails.getAuthorities().stream().map(item ->
        // item.getAuthority())
        // .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setMessage("User logged in successfully!");

        return ResponseEntity.ok(jwtResponse);
    }
}
