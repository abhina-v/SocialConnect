package com.interestconnect.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private String type;

    private Long id;

    private String name;

    private String email;
}