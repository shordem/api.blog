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

    private String codeNotExistsForUserMessage = "User has not requested for a code yet";

    private String hashPassword(String password) {
        return encoder.encode(password);
    }

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

        emailService.sendMail(user.getEmail(), subject, templateName, context);
    }

    private Code getCodeEntity(String code) throws EntityNotFoundException {
        Code codeEntityOptional = codeRepository.findByCodeAndDeletedAtIsNull(code)
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
        return codeEntityOptional;
    }

    private void deleteCode(Code code) {
        code.delete();
        codeRepository.save(code);
    }

    private void resendEmail(String email, String type)
            throws MessagingException, RuntimeException, EntityNotFoundException {
        User user = userService.findByEmail(email);

        Code existingCode = codeRepository.findByUserIdAndDeletedAtIsNull(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(codeNotExistsForUserMessage));

        deleteCode(existingCode);

        String code = generateCode(user);

        switch (type) {
            case "confirm-email":
                sendMail(code, user, "confirm-email", "Confirm Your Email");
                break;
            case "reset-password":
                sendMail(code, user, "reset-password", "Reset Your Password");
                break;
            default:
                throw new RuntimeException("Invalid Email type");
        }

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
        user.setPassword(hashPassword(password));
        user.setIsEmailVerified(false);
        user.setRoles(roles);

        userService.save(user);

        User userEntity = userService.findByUsername(username).get();

        String code = generateCode(userEntity);
        sendMail(code, user, "confirm-email", "Confirm Your Email");

    }

    public void resendVerificationEmail(String email)
            throws MessagingException, RuntimeException, EntityNotFoundException {

        resendEmail(email, "confirm-email");
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

        try {
            resendEmail(email, "reset-password");
        } catch (EntityNotFoundException e) {
            if (codeNotExistsForUserMessage.equals(e.getMessage())) {
                User user = userService.findByEmail(email);
                String code = generateCode(user);

                sendMail(code, user, "reset-password", "Reset Your Password");
            } else {
                throw e;
            }
        }
    }

    public void resetPassword(String email, String code, String password) {
        Code codeEntity = getCodeEntity(code);
        User user = codeEntity.getUser();

        if (user.getEmail().equals(email) == false) {
            throw new RuntimeException("Code is not valid!");
        }

        user.setPassword(hashPassword(password));
        userService.save(user);

        codeEntity.delete();
        codeRepository.save(codeEntity);
    }
}
