package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {

    Page<ProductResponseDto> searchProducts(ProductSearchCondition condition, int page);
}
