package com.example.ProjectBlog.Repository;


import com.example.ProjectBlog.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByEmail(String email);

    User findUserByFullName(String fullName);
}
