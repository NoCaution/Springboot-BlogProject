package com.example.ProjectBlog.Entity.Dto;

import com.example.ProjectBlog.Entity.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;

    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    private Role role;

}
