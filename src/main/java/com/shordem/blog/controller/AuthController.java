package com.shordem.blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.payload.request.ForgotPasswordRequest;
import com.shordem.blog.payload.request.LoginRequest;
import com.shordem.blog.payload.request.RegisterRequest;
import com.shordem.blog.payload.request.ResetPasswordRequest;
import com.shordem.blog.payload.request.VerifyOtpRequest;
import com.shordem.blog.payload.response.JwtResponse;
import com.shordem.blog.payload.response.MessageResponse;
import com.shordem.blog.service.AuthService;
import com.shordem.blog.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register/")
    public ResponseEntity<?> doRegister(@Valid @RequestBody RegisterRequest registerRequest)
            throws EntityNotFoundException, MessagingException {
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if (userService.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already taken!"));
        }

        authService.register(username, email, password);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/login/")
    public ResponseEntity<?> doLogin(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (userService.userIsVerified(email) == false) {
            return ResponseEntity.badRequest().body(new MessageResponse("User is not verified!"));
        }

        String jwt = authService.login(email, password);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(jwt);
        jwtResponse.setMessage("User logged in successfully!");

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/resend-verification-email/")
    public ResponseEntity<?> doResendVerificationEmail(@Valid @RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();

        try {
            if (userService.userIsVerified(email)) {
                return ResponseEntity.badRequest().body(new MessageResponse("User is already verified!"));
            }

            authService.resendVerificationEmail(email);
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email could not be sent!"));
        }

        return ResponseEntity.ok(new MessageResponse("Verification email sent successfully!"));
    }

    @PostMapping("/verify-email/")
    public ResponseEntity<?> doVerifyEmail(@Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        String code = verifyOtpRequest.getCode();

        authService.verifyEmail(code);

        return ResponseEntity.ok(new MessageResponse("User verified successfully!"));
    }

    @PostMapping("/forgot-password/")
    public ResponseEntity<?> doForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();

        try {
            authService.forgotPassword(email);
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email could not be sent!"));
        }

        return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email!"));
    }

    @PutMapping("/reset-password/")
    public ResponseEntity<?> doResetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        String code = resetPasswordRequest.getCode();
        String password = resetPasswordRequest.getPassword();

        authService.resetPassword(email, code, password);

        return ResponseEntity.ok(new MessageResponse("Password reset successfully!"));
    }
}
