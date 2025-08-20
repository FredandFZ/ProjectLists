package com.example.groupproject.service.impl;

import com.example.groupproject.mapper.UserMapper;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Password;
import com.example.groupproject.model.User;
import com.example.groupproject.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void addPassword(String password, Long userId) {
        String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        userMapper.addPassword(encodedPassword, userId);
    }

    @Override
    public User selectUserByName(String username) {
        return userMapper.selectUserByName(username);
    }

    @Override
    public Boolean comparePassword(String username, String password) {
        User user = userMapper.selectUserByName(username);
        if(user == null) return false;
        Long userId = user.getId();
        Password encodedPassword = userMapper.selectPasswordByUserId(userId);
        return BCrypt.checkpw(password, encodedPassword.getPassword());
    }

    @Override
    public User selectUserById(Long id) {
        return userMapper.selectUserById(id);
    }
}
