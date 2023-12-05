package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Article;
import com.example.ProjectBlog.Entity.Comment;
import com.example.ProjectBlog.Entity.Dto.CommentDto;
import com.example.ProjectBlog.Entity.Dto.CreateCommentDto;
import com.example.ProjectBlog.Entity.Dto.UpdateCommentDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Service.ArticleService;
import com.example.ProjectBlog.Service.CommentService;
import com.example.ProjectBlog.Service.UserService;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CustomMapper customMapper;

    @Procedure("this is to get the comment with given commentId that belongs to the article with given articleId ")
    @GetMapping("/getCommentByCommentId/{commentId}")
    public APIResponse getCommentByCommentId(@PathVariable UUID commentId) {
        Comment comment = commentService.getCommentByCommentId(commentId);
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

    @Procedure("this is to get the comment that belongs to the article with given articleId")
    @GetMapping("/getCommentsByArticleId/{articleId}")
    public APIResponse getCommentsByArticleId(@PathVariable UUID articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        if (comments.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "no article found"
            );
        }

        List<CommentDto> commentDtos = customMapper.convertList(comments, CommentDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                commentDtos
        );
    }

    @Procedure("this is to get the comments of user with given userId")
    @GetMapping("/getCommentsByUserId/{userId}")
    public APIResponse getCommentsByUserId(@PathVariable UUID userId) {
        List<Comment> comments = commentService.getCommentsByUserId(userId);
        if (comments.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "no comment found"
            );
        }

        List<CommentDto> commentDtos = customMapper.convertList(comments, CommentDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                commentDtos
        );
    }

    @Procedure("this is to create a comment")
    @PostMapping("/createComment")
    public APIResponse createComment(@RequestBody CreateCommentDto createCommentDto) {
        if (createCommentDto.getUserId() == null || createCommentDto.getArticleId() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given comment is not legit"
            );
        }

        User user = userService.getUserById(createCommentDto.getUserId());
        Article article = articleService.getArticleById(createCommentDto.getArticleId());
        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "user not found"
            );
        }

        Comment comment = new Comment(null,user,article,createCommentDto.getComment(),new Date(),null);
        commentService.addOrUpdateComment(comment);
        CommentDto commentDto = customMapper.map(comment, CommentDto.class);
        return new APIResponse(
                HttpStatus.CREATED,
                "success",
                commentDto
        );
    }

    @Procedure("this is to update a comment")
    @PutMapping("/updateComment")
    public APIResponse updateComment(@RequestBody UpdateCommentDto updateCommentDto) {
        if (updateCommentDto.getId() == null || updateCommentDto.getComment() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given comment is not legit"
            );
        }

        Comment commentToBeUpdated = commentService.getCommentByCommentId(updateCommentDto.getId());
        if (commentToBeUpdated == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no comment found."
            );
        }

        commentToBeUpdated.setComment(updateCommentDto.getComment());
        commentToBeUpdated.setUpdatedAt(new Date());
        commentService.addOrUpdateComment(commentToBeUpdated);
        return new APIResponse(
                HttpStatus.OK,
                "success"
        );
    }

    @Procedure("this is to delete a comment with given commentId")
    @DeleteMapping("/deleteCommentByCommentId/{commentId}")
    public APIResponse deleteComment(@PathVariable UUID commentId) {
        Comment commentToBeDeleted = commentService.getCommentByCommentId(commentId);
        if (commentToBeDeleted == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no comment found"
            );
        }

        commentService.deleteComment(commentToBeDeleted);
        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }
}
