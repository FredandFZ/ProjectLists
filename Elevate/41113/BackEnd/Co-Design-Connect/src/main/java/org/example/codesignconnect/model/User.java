package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Short type;
    private Integer inviteCodeId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
