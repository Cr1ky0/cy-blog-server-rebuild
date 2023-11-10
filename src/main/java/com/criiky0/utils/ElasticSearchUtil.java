package com.criiky0.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


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

    public static boolean hasEsIndex(String index) throws RuntimeException{
        // 启动时检测是否存在blogs的es索引，没有则创建
        try {
            BooleanResponse res = client.indices().exists(d -> d.index(index));
            return res.value();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void createIndexIfNotExists(String index) throws RuntimeException{
        if(!hasEsIndex(index)){
            try {
                client.indices().create(c->c.index(index));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
