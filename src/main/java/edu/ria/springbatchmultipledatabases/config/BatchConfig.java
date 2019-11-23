package edu.ria.springbatchmultipledatabases.config;

import edu.ria.springbatchmultipledatabases.mongo.model.MongoUsers;
import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import edu.ria.springbatchmultipledatabases.destination.model.TargetUsers;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    PlatformTransactionManager batchTransactionManager;

    @Autowired
    MongoTemplate mongoTemplate;

    @Bean
    public Job sourceDbJob(JobBuilderFactory jobBuilderFactory,
                           StepBuilderFactory stepBuilderFactory,
                           @Qualifier("sourceItemReader") ItemReader<SourceUsers> itemReader,
                           @Qualifier("dbProcessor") ItemProcessor<SourceUsers, TargetUsers> itemProcessor,
                           @Qualifier("targetDbItemWriter") ItemWriter<TargetUsers> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-database-load")
                .<SourceUsers, TargetUsers>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-databaseLoad")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Job csvJob(JobBuilderFactory jobBuilderFactory,
                      StepBuilderFactory stepBuilderFactory,
                      @Qualifier("csvItemReader") ItemReader<SourceUsers> itemReader,
                      @Qualifier("csvProcessor") ItemProcessor<SourceUsers, SourceUsers> itemProcessor,
                      @Qualifier("dbItemWriter") ItemWriter<SourceUsers> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-file-load")
                .<SourceUsers, SourceUsers>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-fileLoad")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public FlatFileItemReader csvItemReader(@Value("${input}") Resource resource) {
        FlatFileItemReader flatFileItemReader = new FlatFileItemReader();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());

        return flatFileItemReader;
    }

    @Bean
    public LineMapper lineMapper() {
        DefaultLineMapper<SourceUsers> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "dept", "salary");

        BeanWrapperFieldSetMapper<SourceUsers> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(SourceUsers.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public Job mongoDbJob(JobBuilderFactory jobBuilderFactory,
                          StepBuilderFactory stepBuilderFactory,
                          @Qualifier("mongoDbItemReader") ItemReader<MongoUsers> itemReader,
                          @Qualifier("mongoProcessor") ItemProcessor<MongoUsers, MongoUsers> itemProcessor,
                          @Qualifier("dbItemWriter") ItemWriter<MongoUsers> itemWriter
    ) {

        Step step = stepBuilderFactory.get("ETL-Mongo Database-load")
                .<MongoUsers, MongoUsers>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

        return jobBuilderFactory.get("ETL-mongodataload")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public MongoItemReader<MongoUsers> mongoDbItemReader(MongoTemplate mongoTemplate) {
        MongoItemReader<MongoUsers> itemReader = new MongoItemReader<>();
        itemReader.setTemplate(mongoTemplate);
        itemReader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        itemReader.setTargetType(MongoUsers.class);
        itemReader.setCollection("mongoUsers");
        itemReader.setQuery("{}");

        return itemReader;
    }

    @Bean
    public BatchConfigurer batchConfigurer() {
        return new DefaultBatchConfigurer() {
            @Override
            public PlatformTransactionManager getTransactionManager() {
                return batchTransactionManager;
            }
        };
    }
}
