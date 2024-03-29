package com.example.ecommerce.service.service;

import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.request.UpdateAdminRequest;
import com.example.ecommerce.dto.request.auth.ChangeAccessRequest;
import com.example.ecommerce.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {


    ResponseEntity<Response> save(User user);
    ResponseEntity<Response> deleteUserById(Long userId);
    ResponseEntity<Response> updateUser(Long userId, User user);
    ResponseEntity<Response> getUserById(Long userId);

    ResponseEntity<Response> getAllUsers(Integer page, Integer elementsPerPage, String filter, String sortBy, String status, String role);
    User findByEmail(String email);
    ResponseEntity<Response> changeAccess(ChangeAccessRequest request);

    ResponseEntity<Response> getUserInformationById(Long id);

    User findUserById(Long userId);

    ResponseEntity<Response> searchUserByName(String name, Integer page, Integer elementsPerPage, String status, String filter, String sortBy, String role);

    ResponseEntity<Response> searchUserByEmail(String email);

    ResponseEntity<Response> updateAccountInfoById(Long id, UpdateAdminRequest updateAccountRequest);

    void saveAll(List<User> store);

}
