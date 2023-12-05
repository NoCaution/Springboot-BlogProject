package com.example.ProjectBlog.Repository;

import com.example.ProjectBlog.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    List<Article> findArticlesByAuthorId(UUID authorId);

}
