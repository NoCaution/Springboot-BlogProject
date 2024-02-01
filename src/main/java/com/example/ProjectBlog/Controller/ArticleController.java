package com.example.ProjectBlog.Controller;

import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Dto.CreateArticleDto;
import com.example.ProjectBlog.Entity.Dto.UpdateArticleDto;
import com.example.ProjectBlog.Service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @GetMapping("/getArticles")
    public APIResponse getArticles() {
        return articleService.getArticles();
    }

    @GetMapping("/getArticleById/{articleId}")
    public APIResponse getArticleById(@PathVariable UUID articleId) {
        return articleService.getArticleById(articleId);
    }

    @GetMapping("/getArticlesByUserId/{userId}")
    public APIResponse getArticlesByUserId(@PathVariable UUID userId) {
        return articleService.getArticlesByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    @PostMapping("/createArticle")
    public APIResponse createArticle(@RequestBody CreateArticleDto createArticleDto) {
        return articleService.createArticle(createArticleDto);
    }

    @PutMapping("/updateArticle")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public APIResponse updateArticle(@RequestBody UpdateArticleDto updateArticleDto) {
        return articleService.updateArticle(updateArticleDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    @DeleteMapping("/deleteArticleById/{articleId}")
    public APIResponse deleteArticle(@PathVariable UUID articleId) {
       return articleService.deleteArticle(articleId);
    }
}
