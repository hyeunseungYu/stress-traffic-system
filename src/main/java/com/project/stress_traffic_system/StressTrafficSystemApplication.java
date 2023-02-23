package com.project.stress_traffic_system;

import com.project.stress_traffic_system.product.repository.ProductElasticRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@EnableJpaAuditing
@EnableJpaRepositories(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = ProductElasticRepository.class))
@EnableElasticsearchRepositories(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = ProductElasticRepository.class))
@SpringBootApplication(exclude = {ElasticsearchRepositoriesAutoConfiguration.class})
public class StressTrafficSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StressTrafficSystemApplication.class, args);
    }
}
