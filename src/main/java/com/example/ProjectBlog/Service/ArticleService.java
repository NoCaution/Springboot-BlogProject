package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.APIResponse;
import com.example.ProjectBlog.Entity.Article;
import com.example.ProjectBlog.Entity.Dto.ArticleDto;
import com.example.ProjectBlog.Entity.Dto.CreateArticleDto;
import com.example.ProjectBlog.Entity.Dto.UpdateArticleDto;
import com.example.ProjectBlog.Entity.User;
import com.example.ProjectBlog.Repository.ArticleRepository;
import com.example.ProjectBlog.Util.CustomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private UserService userService;

    public APIResponse getArticles() {
        List<Article> articleList = articleRepository.findAll();

        if (articleList.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NO_CONTENT,
                    "getArticles success but there is no result"
            );
        }

        List<ArticleDto> articleDtos = customMapper.convertList(articleList, ArticleDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                articleDtos
        );
    }

    public APIResponse getArticleById(UUID id) {
        Article article = articleRepository.findById(id).orElse(null);

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

    public APIResponse getArticlesByUserId(UUID userId) {
        if (userId == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given userId is not legit"
            );
        }

        List<Article> articleList = articleRepository.findArticlesByAuthorId(userId);


        if (articleList.isEmpty()) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "there is no article found"
            );
        }

        List<ArticleDto> articleDtos = customMapper.convertList(articleList, ArticleDto.class);
        return new APIResponse(
                HttpStatus.OK,
                "success",
                articleDtos
        );
    }

    public APIResponse createArticle(CreateArticleDto createArticleDto) {
        if (createArticleDto.getAuthorId() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given article is not legit"
            );
        }

        APIResponse response = userService.getUserById(createArticleDto.getAuthorId());
        User user = (User) response.getResult();

        if (user == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no user found"
            );
        }

        Article article = customMapper.map(createArticleDto, Article.class);
        article.setAuthor(user);
        article.setCreatedAt(new Date());

        articleRepository.save(article);

        ArticleDto articleDto = customMapper.map(article, ArticleDto.class);
        return new APIResponse(
                HttpStatus.CREATED,
                "success",
                articleDto
        );
    }

    public APIResponse updateArticle(UpdateArticleDto updateArticleDto) {
        if (updateArticleDto.getId() == null || updateArticleDto.getContent() == null && updateArticleDto.getTitle() == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given article is not legit"
            );
        }

        Article articleToBeUpdated = articleRepository.findById(updateArticleDto.getId()).orElse(null);
        if (articleToBeUpdated == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "given article not found"
            );
        }

        Article updatedArticle = updateArticleFields(articleToBeUpdated, updateArticleDto);

        articleRepository.save(updatedArticle);
        return new APIResponse(
                HttpStatus.OK,
                "success"
        );
    }

    public APIResponse deleteArticle(UUID id) {
        if (id == null) {
            return new APIResponse(
                    HttpStatus.BAD_REQUEST,
                    "given articleId is not legit"
            );
        }

        Article articleToBeDeleted = articleRepository.findById(id).orElse(null);

        if (articleToBeDeleted == null) {
            return new APIResponse(
                    HttpStatus.NOT_FOUND,
                    "no article found"
            );
        }

        articleRepository.deleteById(id);
        return new APIResponse(
                HttpStatus.NO_CONTENT,
                "success"
        );
    }

    public Article updateArticleFields(Article article, UpdateArticleDto toBeUpdatedFields) {
        article.setTitle(toBeUpdatedFields.getTitle() == null ? article.getTitle() : toBeUpdatedFields.getTitle());
        article.setContent(toBeUpdatedFields.getContent() == null ? article.getContent() : toBeUpdatedFields.getContent());
        article.setUpdatedAt(new Date());
        return article;
    }
}
