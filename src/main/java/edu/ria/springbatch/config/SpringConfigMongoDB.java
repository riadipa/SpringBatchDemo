package edu.ria.springbatch.config;

import edu.ria.springbatch.model.Users;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class SpringConfigMongoDB {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<Users> itemReader,
                   ItemProcessor<Users, Users> itemProcessor,
                   ItemWriter<Users> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<Users, Users>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public MongoItemReader<Users> itemReader(MongoTemplate mongoTemplate){
        MongoItemReader<Users> itemReader= new MongoItemReader<>();
        itemReader.setTemplate(mongoTemplate);
        itemReader.setSort(new HashMap<String, Sort.Direction>(){{
            put("_id", Sort.Direction.DESC);
        }});
        itemReader.setTargetType(Users.class);
        itemReader.setCollection("users");
        itemReader.setQuery("{}");

        return itemReader;
    }
}
