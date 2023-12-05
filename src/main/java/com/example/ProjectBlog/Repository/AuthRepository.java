package com.example.ProjectBlog.Repository;


import com.example.ProjectBlog.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthRepository extends JpaRepository<User, UUID> {
}
