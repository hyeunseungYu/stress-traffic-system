package com.project.stress_traffic_system.bulk_data;

import com.project.stress_traffic_system.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BulkInsertTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void bulkInsert() {
        productRepository.bulkInsert();
    }
}