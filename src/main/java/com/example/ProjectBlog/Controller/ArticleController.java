package com.example.ProjectBlog.Controller;

import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Article;
import com.example.ProjectBlog.Entity.Dto.ArticleDto;
import com.example.ProjectBlog.Entity.Dto.CreateArticleDto;
import com.example.ProjectBlog.Entity.Dto.UpdateArticleDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Service.ArticleService;
import com.example.ProjectBlog.Service.UserService;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomMapper customMapper;


    @Procedure("this is to get all the articles")
    @GetMapping("/getArticles")
    public APIResponse getArticles() {
        List<Article> articles = articleService.getArticles();
        if (articles.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "getArticles success but there is no result"
            );
        }

        List<ArticleDto> articleDtos = customMapper.convertList(articles, ArticleDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                articleDtos
        );
    }

    @Procedure("this is to get the article with given articleId")
    @GetMapping("/getArticleById/{articleId}")
    public APIResponse getArticleById(@PathVariable UUID articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "there is no article found"
            );
        }

        ArticleDto articleDto = customMapper.map(article, ArticleDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                articleDto
        );
    }

    @Procedure("this is to get the user's articles with given userId")
    @GetMapping("/getArticlesByUserId/{userId}")
    public APIResponse getArticlesByUserId(@PathVariable UUID userId) {
        if (userId == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given userId is not legit"
            );
        }
        List<Article> articles = articleService.getArticlesByUserId(userId);
        if (articles.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "there is no article found"
            );
        }

        List<ArticleDto> articleDtos = customMapper.convertList(articles, ArticleDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                articleDtos
        );
    }

    @Procedure("this is to create an article")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    @PostMapping("/createArticle")
    public APIResponse createArticle(@RequestBody CreateArticleDto createArticleDto) {
        if (createArticleDto.getAuthorId() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given article is not legit"
            );
        }

        User user = userService.getUserById(createArticleDto.getAuthorId());
        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no user found"
            );
        }

        Article article = customMapper.map(createArticleDto, Article.class);
        article.setAuthor(user);
        article.setCreatedAt(new Date());
        articleService.addArticle(article);
        ArticleDto articleDto = customMapper.map(article, ArticleDto.class);
        return new APIResponse(
                HttpStatus.CREATED,
                "success",
                articleDto
        );
    }

    @Procedure("this is to update an article")
    @PutMapping("/updateArticle")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    public APIResponse updateArticle(@RequestBody UpdateArticleDto updateArticleDto) {
        if (updateArticleDto.getId() == null || updateArticleDto.getContent() == null && updateArticleDto.getTitle() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given article is not legit"
            );
        }

        Article articleToBeUpdated = articleService.getArticleById(updateArticleDto.getId());
        if (articleToBeUpdated == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "given article not found"
            );
        }

        Article updatedArticle = articleService.updateArticleFields(articleToBeUpdated, updateArticleDto);
        articleService.updateArticle(updatedArticle);
        return new APIResponse(
                HttpStatus.OK,
                "success"
        );
    }

    @Procedure("this is to delete an article with given articleId")
    @PreAuthorize("hasAnyRole('ADMIN','EDITOR')")
    @DeleteMapping("/deleteArticleById/{articleId}")
    public APIResponse deleteArticle(@PathVariable UUID articleId) {
        if (articleId == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given articleId is not legit"
            );
        }

        Article articleToBeDeleted = articleService.getArticleById(articleId);
        if (articleToBeDeleted == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no article found"
            );
        }

        articleService.deleteArticle(articleToBeDeleted);
        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }
}
