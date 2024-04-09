package com.shordem.blog.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.shordem.blog.entity.Code;
import com.shordem.blog.entity.ERole;
import com.shordem.blog.entity.Role;
import com.shordem.blog.entity.User;
import com.shordem.blog.exception.EntityNotFoundException;
import com.shordem.blog.repository.CodeRepository;
import com.shordem.blog.repository.RoleRepository;
import com.shordem.blog.utils.JwtUtils;
import com.shordem.blog.utils.StringHelper;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final CodeRepository codeRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final String sender = "shordem@horlakz.com";

    private String generateCode(User user) {
        Code code = new Code();
        String otp = StringHelper.otpGenerator();
        if (codeRepository.existsByCodeAndDeletedAtIsNull(otp)) {
            otp = StringHelper.otpGenerator();
        }

        code.setCode(otp);
        code.setUser(user);

        codeRepository.save(code);

        return code.getCode();
    }

    private void sendMail(String code, User user, String templateName, String subject) throws MessagingException {

        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("name", user.getUsername());

        emailService.sendMail(this.sender, user.getEmail(), subject, templateName, context);
    }

    private Code getCodeEntity(String code) throws EntityNotFoundException {
        Code codeEntityOptional = codeRepository.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        return codeEntityOptional;
    }

    public String login(String email, String password) {

        User user = userService.findByEmail(email);
        String username = user.getUsername();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return jwt;
    }

    public void register(String username, String email, String password)
            throws MessagingException, EntityNotFoundException {
        Set<Role> roles = new HashSet<>();
        User user = new User();

        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));

        roles.add(userRole);

        user.setUsername(StringHelper.lowerCase(username));
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setIsEmailVerified(false);
        user.setRoles(roles);

        userService.save(user);

        User userEntity = userService.findByUsername(username).get();

        String code = generateCode(userEntity);
        sendMail(code, user, "confirm-email", "Confirm Your Email");

    }

    public void resendVerificationEmail(String email)
            throws MessagingException, RuntimeException, EntityNotFoundException {
        User user = userService.findByEmail(email);

        if (user.getIsEmailVerified()) {
            throw new RuntimeException("User is already verified!");
        }

        Code existingCode = codeRepository.findByUserIdAndDeletedAtIsNull(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));

        if (existingCode != null) {
            existingCode.delete();
            codeRepository.save(existingCode);
        }

        String code = generateCode(user);
        sendMail(code, user, "confirm-email", "Confirm Your Email");

    }

    public void verifyEmail(String code) {
        Code codeEntity = getCodeEntity(code);

        User user = codeEntity.getUser();
        user.setIsEmailVerified(true);
        userService.save(user);

        codeEntity.delete();
        codeRepository.save(codeEntity);

    }

    public void forgotPassword(String email) throws MessagingException {
        User user = userService.findByEmail(email);

        String code = generateCode(user);
        sendMail(code, user, "reset-password", "Reset Your Password");
    }

    public void resetPassword(String email, String code, String password) {
        Code codeEntity = getCodeEntity(code);
        User user = codeEntity.getUser();

        if (!user.getEmail().equals(email)) {
            throw new RuntimeException("Email is not valid!");
        }

        user.setPassword(encoder.encode(password));
        userService.save(user);

        codeEntity.delete();
        codeRepository.save(codeEntity);
    }
}
