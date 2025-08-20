package org.example.codesignconnect.service.impl;

import jakarta.servlet.http.HttpSession;
import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.model.InviteCode;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.EmailService;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.utils.CodeGenerator;
import org.example.codesignconnect.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public Result login(String email, String password, HttpSession session) {
        User user = userMapper.findByEmail(email);
        if(user == null || !user.getPassword().equals(password)) return Result.error("Invalid email or password");
        else{
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            String jwt = JWTUtils.generateJwt(claims);
            session.setAttribute("user", user.getUsername());
            return Result.success(Map.of("id", user.getId(), "accessToken", jwt));
        }
    }

    @Override
    public Result signup(SignupRequest request) {
        InviteCode inviteCode = userMapper.checkCode(request.getInviteCode());
        if(inviteCode == null || inviteCode.getIsUsed() || !inviteCode.getEmail().equals(request.getEmail())) return Result.error("Invalid Invite Code");
        else {
            try {
                User user = new User();
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPassword(request.getPassword());
                user.setType(inviteCode.getType());
                user.setInviteCodeId(inviteCode.getId());
                userMapper.addUser(user);
                userMapper.inactivateCode(inviteCode);
                return Result.success(user);
            } catch (Exception e) {
                return Result.error("Email Already Used");
            }
        }
    }

    @Override
    public Result generateCode(String email, Short type) {
        String code;
        do {
            code = CodeGenerator.generateInviteCode(8);
        } while (userMapper.checkCode(code) != null);
        try {
            InviteCode inviteCode = new InviteCode();
            inviteCode.setEmail(email);
            inviteCode.setCode(code);
            inviteCode.setType(type);
            userMapper.addCode(inviteCode);
            emailService.sendInviteCode(inviteCode);
        } catch (Exception e) {
            return Result.error("Email Already Used");
        }
        return Result.success();
    }

    @Override
    public Result resetPassword(String email, String verificationCode, String newPassword) {
        String code = userMapper.findVerificationCode(email);
        if(code == null || !code.equals(verificationCode)) return Result.error("Invalid Verification Code");
        else{
            userMapper.updatePassword(email, newPassword);
            userMapper.deleteVerificationCode(email);
            return Result.success();
        }
    }

    @Override
    public Result getUserInfo(Integer userId) {
        User user = userMapper.getUserById(userId);
        if(user == null) return Result.error("No such user");
        else return Result.success(userMapper.getUserById(userId));
    }

    @Override
    public String getUsernameById(Integer userId) {
        return userMapper.getUsernameById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }
}
