package com.example.ProjectBlog.Entity.Dto;


import com.example.ProjectBlog.Entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistirationRequestDto {

    private String fullName;

    private String email;

    private String password;

    private Role role;

}
