package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import com.project.stress_traffic_system.product.model.dto.QProductResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.project.stress_traffic_system.product.model.QProduct.product;

@Slf4j
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JPAQueryFactory queryFactory;
    private final Integer PAGE_LIMIT = 10;

    private ProductRepositoryImpl(EntityManager em, NamedParameterJdbcTemplate jdbcTemplate) {
        queryFactory = new JPAQueryFactory(em);
        namedParameterJdbcTemplate = jdbcTemplate;
    }

    //Product bulk 데이터 입력하기
    @Override
    public void bulkInsert() {
        String path = "C:\\Users\\forbe\\Downloads\\products100.csv"; //csv 경로
        FileReader in = null;
        BufferedReader bufIn = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<Product> products = new ArrayList<>();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {

            in = new FileReader(path);
            bufIn = new BufferedReader(in);
            bufIn.readLine(); // 컬럼명은 저장되지 않도록 한 줄 읽기

            String data;
            do {//파일에서 데이터를 읽어 파싱하고 Product 객체로 만들어 ArrayList에 넣는다.
                data = bufIn.readLine();  //한 라인 읽기
                if (data != null) {
                    String[] productInfo = data.split(",");  //콤마로 분리하기
                    Product product = new Product();   //Product 객체 생성하기

                    if (data != null) {
                        product.setName(productInfo[1].isEmpty() ? "" : productInfo[1]);      //객체에 값 저장하기
                        product.setPrice(productInfo[2].isEmpty() ? 0 : Integer.parseInt(productInfo[2]));
                        product.setDescription(productInfo[3].isEmpty() ? "" : productInfo[3]);
                        product.setShippingFee(productInfo[4].isEmpty() ? 0 : Integer.parseInt(productInfo[4]));
                        product.setImgurl(productInfo[5].isEmpty() ? 0 : Integer.parseInt(productInfo[5]));
                        product.setCount(productInfo[6].isEmpty() ? 0 : Long.parseLong(productInfo[6]));
                        product.setStock(productInfo[7].isEmpty() ? 0 : Integer.parseInt(productInfo[7]));
                        product.setIntroduction(productInfo[8].isEmpty() ? "" : productInfo[8]);
                        product.setPages(productInfo[9].isEmpty() ? 0 : Integer.parseInt(productInfo[9]));
                        product.setDate(productInfo[10].isEmpty() ? null : LocalDateTime.parse(productInfo[10], formatter));
                    }
                    products.add(product);
                }
            } while (data != null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }  finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                bufIn.close();
            } catch (Exception e) {
            }
        }
        stopWatch.stop();

        //jdbc batchUpdate 실행
        String sql = String.format("INSERT INTO Product (name, price, description, shipping_fee, imgurl, count, stock, introduction, pages, date) " +
                "VALUES (:name, :price, :description, :shipping_fee, :imgurl, :count, :stock, :introduction, :pages, :date)", "Product");

        SqlParameterSource[] params = products.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        StopWatch queryStopwatch = new StopWatch();
        queryStopwatch.start();
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        queryStopwatch.stop();

        log.info("products 사이즈는 = {}", products.size());
        log.info("객체 생성 시간 = {}", stopWatch.getTotalTimeSeconds());
        log.info("쿼리 실행 시간 = {}", queryStopwatch.getTotalTimeSeconds());
    }

    // 상품 조건에 따라 검색(이름, 가격)
    @Override
    public Page<ProductResponseDto> searchProducts(ProductSearchCondition condition, int page) {
        List<ProductResponseDto> content = queryFactory
                .select(new QProductResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.description,
                        product.shippingFee,
                        product.imgurl,
                        product.count,
                        product.stock,
                        product.introduction,
                        product.pages,
                        product.date
                ))
                .from(product)
                .where(nameLike(condition.getName()),
                        priceFrom(condition.getPriceFrom()),
                        priceTo(condition.getPriceTo()))
                .offset(page)
                .limit(PAGE_LIMIT)
                .fetch();

        return new PageImpl<>(content);
    }

    @Override
    public Page<ProductResponseDto> searchByCategory(Long categoryId, int page) {
        List<ProductResponseDto> content = queryFactory
                .select(new QProductResponseDto(
                        product.id,
                        product.name,
                        product.price,
                        product.description,
                        product.shippingFee,
                        product.imgurl,
                        product.count,
                        product.stock,
                        product.introduction,
                        product.pages,
                        product.date
                ))
                .from(product)
                .where(product.category.id.eq(categoryId))
                .offset(page)
                .limit(PAGE_LIMIT)
                .fetch();

        return new PageImpl<>(content);

    }

    private BooleanExpression nameLike(String name) {
        return StringUtils.isEmpty(name) ? null : product.name.like(name);
    }

    private BooleanExpression priceFrom(Integer priceFrom) {
        return priceFrom == null ? null : product.price.goe(priceFrom);
    }

    private BooleanExpression priceTo(Integer priceTo) {
        return priceTo == null ? null : product.price.loe(priceTo);
    }
}
