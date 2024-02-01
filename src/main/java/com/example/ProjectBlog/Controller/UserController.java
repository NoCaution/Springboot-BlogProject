package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.UpdateUserDto;
import com.example.ProjectBlog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController()
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Procedure("this is to get all the users")
    @GetMapping("/getUsers")
    public APIResponse getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/getUserById/{userId}")
    public APIResponse getUserById(@PathVariable UUID userId){
        if(userId == null){
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given id is not legit"
            );
        }

        return userService.getUserById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{userId}")
    public APIResponse deleteUser(@PathVariable UUID userId){
        if(userId == null){
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given userId is not legit"
            );
        }

        return userService.deleteUser(userId);
    }

    @PutMapping("/updateUser")
    public APIResponse updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
