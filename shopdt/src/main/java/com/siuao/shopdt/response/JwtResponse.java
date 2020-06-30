package com.siuao.shopdt.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private String expire;
    private boolean success;

    public JwtResponse(String accessToken, String username, String email, String role, String expire, boolean success) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expire = expire;
        this.success = success;
    }
}
