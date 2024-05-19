package com.example.python.controller;

import com.example.python.model.Article;
import com.example.python.repository.ArticleRepository;
import com.example.python.service.ArticleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/add")
    ResponseEntity<Article> addArticle(@RequestBody Article article) {
        articleService.createArticle(article);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }


    @RequestMapping
    ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        articleService.sendArticles(articles);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }
}
