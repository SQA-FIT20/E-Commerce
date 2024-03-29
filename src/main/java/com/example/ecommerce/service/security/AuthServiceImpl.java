package com.example.ecommerce.service.security;

import com.example.ecommerce.domain.Cart;
import com.example.ecommerce.domain.Customer;
import com.example.ecommerce.domain.Store;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.request.auth.ChangePasswordRequest;
import com.example.ecommerce.dto.response.LoginResponse;
import com.example.ecommerce.dto.request.auth.LoginRequest;
import com.example.ecommerce.dto.request.auth.RegistrationRequest;
import com.example.ecommerce.dto.response.Response;
import com.example.ecommerce.exception.LoginFailedException;
import com.example.ecommerce.exception.NotFoundException;
import com.example.ecommerce.exception.RegistrationException;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.impl.CustomerService;
import com.example.ecommerce.service.impl.StoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.ecommerce.utils.Utils.generateAvatarLink;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final StoreService storeService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<Response> register(RegistrationRequest registrationRequest) {

        boolean emailAlreadyRegistered = userRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());

        if (emailAlreadyRegistered) {
            throw new RegistrationException("Email already registered!");
        }

        String role = registrationRequest.getRole();

        if (role.equals("CUSTOMER")) {
            Customer customer = new Customer();
            customer.setEmail(registrationRequest.getEmail());
            customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            customer.setName(registrationRequest.getName());
            customer.setRole(registrationRequest.getRole());
            customer.setAvatar(generateAvatarLink(customer.getName()));
            customer.setCart(new Cart());
            customer.setCreatedAt(LocalDateTime.now());
            customerService.save(customer);

        } else if (role.equals("STORE")) {
            Store store = new Store();
            store.setEmail(registrationRequest.getEmail());
            store.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            store.setName(registrationRequest.getName());
            store.setRole(registrationRequest.getRole());
            store.setAvatar(generateAvatarLink(store.getName()));
            store.setCreatedAt(LocalDateTime.now());
            storeService.save(store);
        } else {
            throw new RegistrationException("Invalid role!");
        }

        Response response = Response.builder()
                .status(200)
                .message("Registration successfully!")
                .build();
        return ResponseEntity.ok(response);

    }




    @Override
    public ResponseEntity<Response> login(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        try {
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            // if username or password is wrong, the BadCredentialsException will be thrown

            User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail()).get();
            if (user.isLocked()) {
                throw new LoginFailedException("Your account is locked! Please contact admin to unlock your account!");
            }
            String jwtToken = jwtService.generateToken(user);

            LoginResponse token = LoginResponse.builder()
                    .token(jwtToken)
                    .role(user.getRole())
                    .build();

            Response response = Response.builder()
                    .status(200)
                    .message("Log in successfully!")
                    .data(token)
                    .build();

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Response response = Response.builder()
                    .status(400)
                    .message("Wrong username or password!")
                    .build();


            return ResponseEntity.badRequest().body(response);
        }



    }

    @Override
    public ResponseEntity<Response> changePassword(User currentUser, ChangePasswordRequest changePasswordRequest) {

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new BadCredentialsException("Wrong old password!");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(currentUser);

        Response response = Response.builder()
                .status(200)
                .message("Change password successfully!")
                .build();

        return ResponseEntity.ok(response);

    }
}
