package com.project.stress_traffic_system.bulk_data;

import com.project.stress_traffic_system.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BulkInsertTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("github CI/CD 확인용 어노테이션(추후 수정 부탁)")
    void bulkInsert() {
        productRepository.bulkInsert();
    }
}