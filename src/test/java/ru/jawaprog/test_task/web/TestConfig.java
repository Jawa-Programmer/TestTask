package ru.jawaprog.test_task.web;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.ws.test.server.MockWebServiceClient;

import javax.sql.DataSource;

@TestConfiguration
@MapperScan("ru.jawaprog.test_task.repositories")
@ComponentScan({"ru.jawaprog.test_task.web.soap.endpoints",
        "ru.jawaprog.test_task.web.rest.controllers",
        "ru.jawaprog.test_task.services",
        "ru.jawaprog.test_task.repositories",
        "ru.jawaprog.test_task.web.utils"})
public class TestConfig {

    @Bean()
    MockWebServiceClient mockClient(ApplicationContext context) {
        return MockWebServiceClient.createClient(context);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://localhost:5432/test_task_mts");
        driver.setUsername("siblion");
        driver.setPassword("42univers");

        Resource initSchema = new ClassPathResource("schema-test.sql", getClass().getClassLoader());
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, driver);

        return driver;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }
}
