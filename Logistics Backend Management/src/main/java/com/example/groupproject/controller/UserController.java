package com.example.groupproject.controller;
import com.example.groupproject.model.Address;
import com.example.groupproject.model.Result;
import com.example.groupproject.model.User;
import com.example.groupproject.service.UserService;
import com.example.groupproject.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    //增加用户信息
    @PostMapping("/user")
    public Result addUser(@RequestBody User user) {
        log.info("添加用户:{}", user);
        userService.addUser(user);
        return Result.success();
    }

    //更改用户信息
    @PutMapping("/user")
    public Result updateUser(@RequestBody User user){
        log.info("更改用户信息:{}", user);
        userService.updateUser(user);
        return Result.success();
    }

    //删除用户信息
    @DeleteMapping("/user")
    public Result deleteUser(Long id){
        log.info("删除用户信息:{}", id);
        userService.deleteUser(id);
        return Result.success();
    }

    //储存用户密码
    @PostMapping("/password")
    public Result addPassword(String password, Long userId){
        log.info("储存密码:{}, {}", password, userId);
        if(password.length() >= 20) return Result.error("密码长度超出限制");
        userService.addPassword(password, userId);
        return Result.success();
    }

    //用户名查询用户
    @GetMapping("/user")
    public Result selectUserByName(String username){
        log.info("用户名查询用户:{}", username);
        User user = userService.selectUserByName(username);
        return user != null ? Result.success(user) : Result.error("无查询结果");
    }

    //用户登录
    @PostMapping("/login")
    public Result login(String username, String password){
        log.info("用户登录:{}, {}", username, password);
        Boolean flag = userService.comparePassword(username, password);
        if(flag == true){
            User user = userService.selectUserByName(username);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("name", user.getName());
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }
        else return Result.error("用户名或密码错误");
    }

    //展示用户资料
    @GetMapping("/user/{id}")
    public Result selectUserById(@PathVariable Long id){
        log.info("展示用户资料:{}", id);
        User user = userService.selectUserById(id);
        return user != null ? Result.success(user) : Result.error("无用户信息");
    }
}