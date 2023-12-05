package com.example.ProjectBlog.Entity.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequestDto {

    private String email;

    private String password;

    public AuthenticationRequestDto(String email, String password){
        this.email = email;
        this.password = password;
    }
}
