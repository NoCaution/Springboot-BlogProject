package com.example.ProjectBlog.Entity.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private UUID id;

    private UUID userId;

    private UUID articleId;

    private String comment;

    private Date createdAt;

}
