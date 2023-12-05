package com.example.ProjectBlog.Service;

import com.example.ProjectBlog.Entity.Article;
import com.example.ProjectBlog.Entity.Dto.UpdateArticleDto;
import com.example.ProjectBlog.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(UUID id) {
        return articleRepository.findById(id).orElse(null);
    }

    public List<Article> getArticlesByUserId(UUID userId) {
        return articleRepository.findArticlesByAuthorId(userId);
    }

    public void addArticle(Article article) {
        articleRepository.save(article);
    }

    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

    public Article updateArticleFields(Article article, UpdateArticleDto toBeUpdatedFields) {
        article.setTitle(toBeUpdatedFields.getTitle() == null ? article.getTitle() : toBeUpdatedFields.getTitle());
        article.setContent(toBeUpdatedFields.getContent() == null ? article.getContent() : toBeUpdatedFields.getContent());
        article.setUpdatedAt(new Date());
        return article;
    }
}
