package com.example.ProjectBlog.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {

    private String token;

    private boolean userExists;

    public AuthenticationResponseDto(String token){
        this.token = token;
    }

    public AuthenticationResponseDto(boolean userExists){
        this.userExists = userExists;
    }
}
