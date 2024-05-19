package com.example.python.service;

import com.example.python.model.Article;
import com.example.python.repository.ArticleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KafkaConsumerService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "create-article", groupId = "group_id")
    public void consume(String articleJson){
        ObjectMapper objectMapper = new ObjectMapper();
        Article article = null;
        try {
            article = objectMapper.readValue(articleJson, Article.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        articleRepository.save(article);
        System.out.println("Consumed Message: " + article.toString());
    }

    @KafkaListener(topics = "get-all", groupId = "group_id")
    public void consume1(String articleJson){
        if(articleJson != null){
            List<Article> articles = articleRepository.findAll();
            this.kafkaTemplate.send("get-all-article", articles);
        }
    }

    @KafkaListener(topics = "get_article_by_id", groupId = "group_id")
    public void consume2(String articleJson) throws Exception {
        if(articleJson != null){
            Long id = Long.parseLong(articleJson);
            Article article = articleRepository.findById(id).orElseThrow(() -> new Exception("Article not found with id " + id));
            this.kafkaTemplate.send("get-by-id", article);
        }
    }

    @KafkaListener(topics = "update-article", groupId = "group_id")
    public void consume3(String articleJson) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Article articleUpdate = null;
        try {
            articleUpdate = objectMapper.readValue(articleJson, Article.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Consumed Message: " + articleUpdate.toString());
        Article article = articleRepository.findById(articleUpdate.getId()).orElseThrow(() -> new Exception("Article not found"));
        if(article.getTitle() != null){
            article.setTitle(articleUpdate.getTitle());
        }
        if(article.getContent() != null) {
            article.setContent(articleUpdate.getContent());
        }
        if(article.getImage() != null) {
            article.setImage(articleUpdate.getImage());
        }
        if(article.getSummary() != null) {
            article.setSummary(articleUpdate.getSummary());
        }
        articleRepository.save(article);
    }

    @KafkaListener(topics = "delete_article", groupId = "group_id")
    public void consume4(String articleJson) throws Exception {
        if(articleJson != null){
            Long id = Long.parseLong(articleJson);
            articleRepository.deleteById(id);
        }
    }
}
