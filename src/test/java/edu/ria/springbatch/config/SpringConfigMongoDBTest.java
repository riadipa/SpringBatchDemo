package edu.ria.springbatch.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

class SpringConfigMongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void job() {
    }

    @Test
    void itemReader() {
        MongoItemReader reader = new MongoItemReader();


    }
}