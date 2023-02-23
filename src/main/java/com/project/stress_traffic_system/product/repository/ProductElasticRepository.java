/*
package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.ProductDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductElasticRepository extends ElasticsearchRepository<ProductDoc, Long> {

    Page<ProductDoc> findTopByNameAndPriceBetween(String name, int min, int max, Pageable pageable);

//    List<ProductDoc> findByName(String name);
}

*/