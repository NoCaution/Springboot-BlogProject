package com.example.ProjectBlog.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Articles")
public class Article {

    @Column(name = "article_id",nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private UUID id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    @ManyToOne
    private User author;
    @OneToMany
    private Set<Comment> comments;

}
