package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteCode {
    private Integer id;
    private String email;
    private String code;
    private Short type;
    private Boolean isUsed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
