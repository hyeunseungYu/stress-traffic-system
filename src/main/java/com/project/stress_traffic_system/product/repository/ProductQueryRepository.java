/*
package com.project.stress_traffic_system.product.repository;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import com.project.stress_traffic_system.product.model.ProductDoc;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final ElasticsearchOperations operations;
    private final RestHighLevelClient client;
//    private final ProductElasticRepository productElasticRepository;

    public List<ProductDoc> findByCondition(ProductSearchCondition searchCondition, Pageable pageable) {
        log.info("Repository - findByCondition 메서드 접근");

        CriteriaQuery query = createConditionCriteriaQuery(searchCondition).setPageable(pageable);

        SearchHits<ProductDoc> search = operations.search((Query) query, ProductDoc.class);
        return search.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    private CriteriaQuery createConditionCriteriaQuery(ProductSearchCondition searchCondition) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        if (searchCondition == null)
            return query;

        //todo 책이름 검색하는 방법..개선할 것(match 되는 경우 검색이 안 됨)
        if (searchCondition.getName() != null){
            query.addCriteria(Criteria.where("name").contains(searchCondition.getName()));
        }

        if(searchCondition.getPriceTo() != null && searchCondition.getPriceTo() > 0)
            query.addCriteria(Criteria.where("price").lessThanEqual(searchCondition.getPriceTo()));

        if(searchCondition.getPriceFrom() != null && searchCondition.getPriceFrom() > 0)
            query.addCriteria(Criteria.where("price").greaterThanEqual(searchCondition.getPriceFrom()));

        return query;
    }
}

*/