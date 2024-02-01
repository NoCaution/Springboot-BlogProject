package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Article;
import com.example.ProjectBlog.Entity.Comment;
import com.example.ProjectBlog.Entity.Dto.CommentDto;
import com.example.ProjectBlog.Entity.Dto.CreateCommentDto;
import com.example.ProjectBlog.Entity.Dto.UpdateCommentDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Repository.CommentRepository;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    public APIResponse getCommentsByArticleId(UUID articleId) {
        List<Comment> commentList = commentRepository.findCommentsByArticleId(articleId);

        if (commentList.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "no article found"
            );
        }

        List<CommentDto> commentDtos = customMapper.convertList(commentList, CommentDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                commentDtos
        );

    }

    public APIResponse getCommentByCommentId(UUID commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no comment or article found"
            );
        }

        CommentDto commentDto = customMapper.map(comment, CommentDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                commentDto
        );
    }

    public APIResponse getCommentsByUserId(UUID userId) {
        List<Comment> commentList = commentRepository.findCommentsByUserId(userId);

        if (commentList.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "no comment found"
            );
        }

        List<CommentDto> commentDtos = customMapper.convertList(commentList, CommentDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                commentDtos
        );
    }

    public APIResponse createComment(CreateCommentDto createCommentDto) {

        APIResponse userServiceResponse = userService.getUserById(createCommentDto.getUserId());
        User user = (User) userServiceResponse.getResult();

        APIResponse articleServiceResponse = articleService.getArticleById(createCommentDto.getArticleId());
        Article article = (Article) articleServiceResponse.getResult();
        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        if(article == null){
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "article not found"
            );
        }

        Comment comment = new Comment(null, user, article, createCommentDto.getComment(), new Date(), null);

        commentRepository.save(comment);
        CommentDto commentDto = customMapper.map(comment, CommentDto.class);
        return new APIResponse(
                HttpStatus.CREATED,
                "success",
                commentDto
        );
    }

    public APIResponse updateComment(UpdateCommentDto updateCommentDto) {
        Comment commentToBeUpdated = commentRepository.findById(updateCommentDto.getId()).orElse(null);

        if (commentToBeUpdated == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no comment found."
            );
        }

        commentToBeUpdated.setComment(updateCommentDto.getComment());
        commentToBeUpdated.setUpdatedAt(new Date());

        commentRepository.save(commentToBeUpdated);
        return new APIResponse(
                HttpStatus.OK,
                "success"
        );
    }


    public APIResponse deleteComment(UUID id) {
        Comment commentToBeDeleted = commentRepository.findById(id).orElse(null);
        if (commentToBeDeleted == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no comment found"
            );
        }

        commentRepository.delete(commentToBeDeleted);

        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }

}
