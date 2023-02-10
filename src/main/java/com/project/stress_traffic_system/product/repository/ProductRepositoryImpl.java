package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import com.project.stress_traffic_system.product.model.dto.QProductResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.project.stress_traffic_system.product.model.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final Integer PAGE_LIMIT = 10;

    private ProductRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductResponseDto> searchProducts(ProductSearchCondition condition, int page) {
        List<ProductResponseDto> content = queryFactory
                .select(new QProductResponseDto(
                        product.id,
                        product.name,
                        product.location,
                        product.around,
                        product.notice,
                        product.base,
                        product.price,
                        product.imgurl,
                        product.date
                ))
                .from(product)
                .where(nameEq(condition.getName()),
                        priceFrom(condition.getPriceFrom()),
                        priceTo(condition.getPriceTo()))
                .offset(page)
                .limit(PAGE_LIMIT)
                .fetch();

        return new PageImpl<>(content);
    }

    private BooleanExpression nameEq(String name) {
        return StringUtils.isEmpty(name) ? null : product.name.like(name);
    }

    private BooleanExpression priceFrom(Integer priceFrom) {
        return priceFrom == null ? null : product.price.goe(priceFrom);
    }

    private BooleanExpression priceTo(Integer priceTo) {
        return priceTo == null ? null : product.price.loe(priceTo);
    }
}
