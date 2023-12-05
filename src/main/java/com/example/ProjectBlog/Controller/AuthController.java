package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.AuthenticationRequestDto;
import com.example.ProjectBlog.Entity.Dto.AuthenticationResponseDto;
import com.example.ProjectBlog.Entity.Dto.RegistirationRequestDto;
import com.example.ProjectBlog.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        AuthenticationResponseDto response = authService.register(request);
        if (response.isUserExists()) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "this email linked to another account."
            );
        }

        if (response.getToken() == null) {
            return new APIResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Token generate failed"
            );
        }

        return new APIResponse(
                HttpStatus.CREATED,
                "success",
                response
        );
    }

    @PostMapping("/login")
    public APIResponse login(@RequestBody AuthenticationRequestDto request) {
        AuthenticationResponseDto response = authService.login(request);
        if (response.getToken() == null) {
            return new APIResponse(
                    HttpStatus.UNAUTHORIZED,
                    "login failed"
            );
        }

        response.setUserExists(true);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                response
        );
    }
}
