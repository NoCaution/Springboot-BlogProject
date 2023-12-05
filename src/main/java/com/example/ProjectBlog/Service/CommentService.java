package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.Comment;
import com.example.ProjectBlog.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByArticleId(UUID articleId) {
        return commentRepository.findCommentsByArticleId(articleId);
    }

    public Comment getCommentByCommentId(UUID commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getCommentsByUserId(UUID userId) {
        return commentRepository.findCommentsByUserId(userId);
    }

    public void addOrUpdateComment(Comment comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

}
