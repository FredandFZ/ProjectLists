package com.example.groupproject.mapper;

import com.example.groupproject.model.Password;
import com.example.groupproject.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);

    void addPassword(String password, Long userId);

    User selectUserByName(String username);

    Password selectPasswordByUserId(Long userId);

    User selectUserById(Long userId);
}