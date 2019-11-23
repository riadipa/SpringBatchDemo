package edu.ria.springbatchmultipledatabases.config;

import com.zaxxer.hikari.HikariDataSource;
import edu.ria.springbatchmultipledatabases.source.model.SourceUsers;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "edu.ria.springbatchmultipledatabases.source",
        entityManagerFactoryRef = "sourceEntityManagerFactory",
        transactionManagerRef = "sourceTransactionManager"
)
public class SourceDbConfig {

    private static final String FETCH_SQL_QUERY = "SELECT id, name, dept, salary FROM SOURCEUSERS";

    @Bean
    @ConfigurationProperties("source.datasource")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("source.datasource.configuration")
    public DataSource sourceDataSource() {
        return sourceDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(sourceDataSource())
                .packages("edu.ria.springbatchmultipledatabases.source")
                .properties(properties)
                .build();
    }

    @Bean
    public PlatformTransactionManager sourceTransactionManager(
            final @Qualifier("sourceEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }

    @Bean
    public ItemReader sourceItemReader(@Qualifier("sourceDataSource") final DataSource sourceDataSource) {
        JdbcCursorItemReader<SourceUsers> reader = new JdbcCursorItemReader<>();

        reader.setDataSource(sourceDataSource);
        reader.setSql(FETCH_SQL_QUERY);
        reader.setRowMapper(new BeanPropertyRowMapper<>(SourceUsers.class));

        return reader;
    }

}
