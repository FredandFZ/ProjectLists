package org.example.codesignconnect.service;

import jakarta.servlet.http.HttpSession;
import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.User;

import java.util.List;

public interface UserService {
    Result login(String email, String password, HttpSession session);

    Result signup(SignupRequest request);

    Result generateCode(String email, Short type);

    Result resetPassword(String email, String verificationCode, String newPassword);

    Result getUserInfo(Integer userId);

    String getUsernameById(Integer userId);

    List<User> getAllUsers();

    void deleteUser(Integer id);
}
