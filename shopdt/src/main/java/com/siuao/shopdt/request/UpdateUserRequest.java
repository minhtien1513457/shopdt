package com.siuao.shopdt.request;

import com.siuao.shopdt.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Integer status;
}
