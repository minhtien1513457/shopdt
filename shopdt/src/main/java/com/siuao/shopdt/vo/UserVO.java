package com.siuao.shopdt.vo;

import com.siuao.shopdt.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Integer status;
    private String createdUser;
    private String updatedUser;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
