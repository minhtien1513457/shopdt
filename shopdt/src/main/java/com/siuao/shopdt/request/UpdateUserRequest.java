package com.siuao.shopdt.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private Long id;
    private String username;
    private String email;
    private String role;
    private Integer status;
}
