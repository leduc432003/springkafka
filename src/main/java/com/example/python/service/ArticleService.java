package com.example.python.service;

import com.example.python.model.Article;
import com.example.python.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ArticleRepository articleRepository;


    public ArticleService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendArticles(List<Article> articles) {
        this.kafkaTemplate.send("python", articles);
    }
    public void createArticle(Article article) {
        this.kafkaTemplate.send("my_topic", article);
    }

}
