package com.example.groupproject.service;

import com.example.groupproject.model.Address;
import com.example.groupproject.model.User;

public interface UserService {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);

    void addPassword(String password, Long userId);

    User selectUserByName(String username);

    Boolean comparePassword(String username, String password);

    User selectUserById(Long id);
}
