package com.siuao.shopdt.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private String role;
    private String expire;

    public JwtResponse(String accessToken, String username, String email, String role, String expire) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
        this.expire = expire;
    }
}
