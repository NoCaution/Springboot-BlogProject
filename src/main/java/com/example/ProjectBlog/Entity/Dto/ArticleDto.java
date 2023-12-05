package com.example.ProjectBlog.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private UUID id;

    private String title;

    private String content;

    private Date createdAt;

    private Date updatedAt;

    private UUID authorId;

}
