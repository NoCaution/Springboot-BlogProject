package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.Dto.AuthenticationRequestDto;
import com.example.ProjectBlog.Entity.Dto.AuthenticationResponseDto;
import com.example.ProjectBlog.Entity.Dto.RegistirationRequestDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authManager;



    private boolean isUniqeUser(String email){
        User user = userService.getUserByEmail(email);
        return user == null;
    }

    public AuthenticationResponseDto register(RegistirationRequestDto requestDto){
        if(!isUniqeUser(requestDto.getEmail())){
            return new AuthenticationResponseDto(
                    true
            );
        }

        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = new User(
                requestDto.getFullName(),
                requestDto.getEmail(),
                password,
                requestDto.getRole(),
                new Date()
        );
        authRepository.save(user);

        user = userService.getUserByEmail(requestDto.getEmail());
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                user.getAuthorities()
        );

        String token = tokenService.generateJwtToken(userDetails);
        return new AuthenticationResponseDto(
            token
        );
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto requestDto){
        if(isUniqeUser(requestDto.getEmail())){
            return new AuthenticationResponseDto(
                    false
            );
        }

        User user = userService.getUserByEmail(requestDto.getEmail());
        if(!passwordEncoder.matches(requestDto.getPassword(),user.getPassword())){
            return new AuthenticationResponseDto(
                    true
            );
        }

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getId(),
                        requestDto.getPassword()
                )
        );

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                user.getAuthorities()
        );
        String token = tokenService.generateJwtToken(userDetails);

        return new AuthenticationResponseDto(
                token
        );
    }
}
