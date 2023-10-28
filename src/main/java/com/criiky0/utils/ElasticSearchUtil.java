package com.criiky0.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ElasticSearchUtil {


    private ElasticsearchClient myClient;

    @Autowired
    public ElasticSearchUtil(ElasticsearchClient myClient) {
        this.myClient = myClient;
    }

    public static ElasticsearchClient client;

    @PostConstruct
    public void init() {
        client = this.myClient;
    }
}
