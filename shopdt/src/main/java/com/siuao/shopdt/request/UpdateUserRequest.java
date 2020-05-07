package com.siuao.shopdt.request;

import com.siuao.shopdt.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Integer status;
}
