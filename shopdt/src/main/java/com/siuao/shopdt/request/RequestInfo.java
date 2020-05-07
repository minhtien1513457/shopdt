package com.siuao.shopdt.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RequestInfo implements Serializable {
    private String username;
    private String language;
    private String token;

    public RequestInfo(String username, String language, String token) {
        this.username = username;
        this.language = language;
        this.token = token;
    }
}
