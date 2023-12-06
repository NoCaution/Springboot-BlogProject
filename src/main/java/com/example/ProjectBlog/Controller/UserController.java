package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.UpdateUserDto;
import com.example.ProjectBlog.Entity.Dto.UserDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Service.UserService;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomMapper customMapper;


    @Procedure("this is to get all the users")
    @GetMapping("/getUsers")
    public APIResponse getUsers(){
        List<User> users = userService.getUsers();
        if(users.isEmpty()){
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no user found"
            );
        }

        List<UserDto> userDtos = customMapper.convertList(users, UserDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                userDtos
        );
    }

    @Procedure("this is to get user with given id")
    @GetMapping("/getUserById/{userId}")
    public APIResponse getUserById(@PathVariable UUID userId){
        if(userId == null){
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given id is not legit"
            );
        }

        User user = userService.getUserById(userId);
        if(user == null){
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

    @Procedure("this is to delete an existing user")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public APIResponse deleteUser(@PathVariable UUID userId){
        if(userId == null){
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given userId is not legit"
            );
        }

        User userToBeDeleted = userService.getUserById(userId);
        if(userToBeDeleted == null){
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        userService.deleteUser(userToBeDeleted);
        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }

    @Procedure("this is to update a user")
    @PutMapping("/updateUser")
    public APIResponse updateUser(@RequestBody UpdateUserDto updateUserDto){
        if(updateUserDto.getId() == null){
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given user is not legit"
            );
        }

        User user = userService.getUserById(updateUserDto.getId());
        if(user == null){
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }
        //updates the fields of the given user with null safety and returns this user.
        User updatedUser = userService.updateUserFields(user,updateUserDto);
        userService.createOrUpdateUser(updatedUser);
        UserDto userDto = customMapper.map(updatedUser, UserDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                userDto
        );
    }
}
