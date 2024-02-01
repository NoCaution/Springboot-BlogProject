package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.UpdateUserDto;
import com.example.ProjectBlog.Entity.Dto.UserDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Repository.UserRepository;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public APIResponse getUsers() {
        List<User> userList = userRepository.findAll();

        if (userList.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no user found"
            );
        }

        List<UserDto> userDtos = customMapper.convertList(userList, UserDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                userDtos
        );
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public APIResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        UserDto userDto = customMapper.map(user, UserDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                userDto
        );
    }


    public APIResponse deleteUser(UUID id) {
        User userToBeDeleted = userRepository.findById(id).orElse(null);

        if (userToBeDeleted == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        userRepository.deleteById(id);

        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }

    public APIResponse updateUser(UpdateUserDto updateUserDto) {
        if (updateUserDto.getId() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given user is not legit"
            );
        }

        User user = userRepository.findById(updateUserDto.getId()).orElse(null);

        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        //updates the fields of the given user with null safety and returns this user.
        User updatedUser = updateUserFields(user, updateUserDto);

        userRepository.save(updatedUser);

        UserDto userDto = customMapper.map(updatedUser, UserDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                userDto
        );
    }

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String id) {
        UUID uuid = UUID.fromString(id);
        User user = userRepository.findById(uuid).orElse(null);

        return new org.springframework.security.core.userdetails.User(
                id, //userName is the id of our entity User
                user.getPassword(),
                user.getAuthorities()
        );
    }

    private User updateUserFields(User user, UpdateUserDto toBeUpdatedFields) {
        user.setEmail(toBeUpdatedFields.getEmail() == null ? user.getEmail() : toBeUpdatedFields.getEmail());
        user.setPassword(toBeUpdatedFields.getPassword() == null ? user.getPassword() : passwordEncoder.encode(toBeUpdatedFields.getPassword()));
        user.setFullName(toBeUpdatedFields.getFullName() == null ? user.getFullName() : toBeUpdatedFields.getFullName());
        user.setPhoneNumber(toBeUpdatedFields.getPhoneNumber() == null ? user.getPhoneNumber() : toBeUpdatedFields.getPhoneNumber());
        user.setRole(toBeUpdatedFields.getRole() == null ? user.getRole() : toBeUpdatedFields.getRole());
        user.setUpdatedAt(new Date());
        return user;
    }
}
