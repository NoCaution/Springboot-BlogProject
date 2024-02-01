package com.example.ProjectBlog.Controller;


import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.CreateCommentDto;
import com.example.ProjectBlog.Entity.Dto.UpdateCommentDto;
import com.example.ProjectBlog.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/getCommentByCommentId/{commentId}")
    public APIResponse getCommentByCommentId(@PathVariable UUID commentId) {
        return commentService.getCommentByCommentId(commentId);
    }

    @GetMapping("/getCommentsByArticleId/{articleId}")
    public APIResponse getCommentsByArticleId(@PathVariable UUID articleId) {
        return commentService.getCommentsByArticleId(articleId);
    }

    @GetMapping("/getCommentsByUserId/{userId}")
    public APIResponse getCommentsByUserId(@PathVariable UUID userId) {
        return commentService.getCommentsByUserId(userId);
    }

    @PostMapping("/createComment")
    public APIResponse createComment(@RequestBody CreateCommentDto createCommentDto) {
        return commentService.createComment(createCommentDto);
    }

    @PutMapping("/updateComment")
    public APIResponse updateComment(@RequestBody UpdateCommentDto updateCommentDto) {
        if (updateCommentDto.getId() == null || updateCommentDto.getComment() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given comment is not legit"
            );
        }

        return commentService.updateComment(updateCommentDto);
    }

    @Procedure("this is to delete a comment with given commentId")
    @DeleteMapping("/deleteCommentByCommentId/{commentId}")
    public APIResponse deleteComment(@PathVariable UUID commentId) {
        return commentService.deleteComment(commentId);
    }
}
