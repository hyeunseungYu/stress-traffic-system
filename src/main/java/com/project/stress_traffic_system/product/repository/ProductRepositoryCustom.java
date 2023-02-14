package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Category;
import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductResponseDto> searchProducts(ProductSearchCondition condition, int page);

    void bulkInsert();
    void bulkInsertMembers();

    Page<ProductResponseDto> searchByCategory(Long categoryId, int page);
    Page<ProductResponseDto> findAllOrderByClickCountDesc(int page);
}
