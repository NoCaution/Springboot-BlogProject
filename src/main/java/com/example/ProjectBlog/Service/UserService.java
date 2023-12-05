package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.Dto.UpdateUserDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public void createOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String id) {
        UUID uuid = UUID.fromString(id);
        User user = getUserById(uuid);
        return new org.springframework.security.core.userdetails.User(
                id, //userName is the id of our entity User
                user.getPassword(),
                user.getAuthorities()
        );
    }

    public User updateUserFields(User user, UpdateUserDto toBeUpdatedFields) {
        user.setEmail(toBeUpdatedFields.getEmail() == null ? user.getEmail() : toBeUpdatedFields.getEmail());
        user.setPassword(toBeUpdatedFields.getPassword() == null ? user.getPassword() : toBeUpdatedFields.getPassword());
        user.setFullName(toBeUpdatedFields.getFullName() == null ? user.getFullName() : toBeUpdatedFields.getFullName());
        user.setPhoneNumber(toBeUpdatedFields.getPhoneNumber() == null ? user.getPhoneNumber() : toBeUpdatedFields.getPhoneNumber());
        user.setRole(toBeUpdatedFields.getRole() == null ? user.getRole() : toBeUpdatedFields.getRole());
        user.setUpdatedAt(new Date());
        return user;
    }
}
