//package com.project.stress_traffic_system.config;
//
//
//import com.project.stress_traffic_system.product.repository.ProductElasticRepository;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
////@EnableElasticsearchRepositories(basePackageClasses = ProductElasticRepository.class)
//@Configuration
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
//
//    @Value("${SPRING.ELASTICSEARCH.HOST}")
//    private String host;
//
//    @Override
//    public ElasticsearchOperations elasticsearchOperations(ElasticsearchConverter elasticsearchConverter, RestHighLevelClient elasticsearchClient) {
//        return new ElasticsearchRestTemplate(elasticsearchClient());
//    }
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(host)
//                .build();
//        return RestClients.create(clientConfiguration).rest();
//    }
//}