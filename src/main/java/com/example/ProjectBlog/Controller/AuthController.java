package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.AuthenticationRequestDto;
import com.example.ProjectBlog.Entity.Dto.RegistirationRequestDto;
import com.example.ProjectBlog.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public APIResponse register(@RequestBody RegistirationRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public APIResponse login(@RequestBody AuthenticationRequestDto request) {
        return authService.login(request);
    }
}
