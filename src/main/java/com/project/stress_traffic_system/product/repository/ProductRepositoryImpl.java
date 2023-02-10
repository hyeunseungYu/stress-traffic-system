package com.project.stress_traffic_system.product.repository;

import com.project.stress_traffic_system.product.model.Product;
import com.project.stress_traffic_system.product.model.dto.ProductResponseDto;
import com.project.stress_traffic_system.product.model.dto.ProductSearchCondition;
import com.project.stress_traffic_system.product.model.dto.QProductResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                        product.setLocation(productInfo[2].isEmpty() ? "" : productInfo[2]);
                        product.setAround(productInfo[3].isEmpty() ? "" : productInfo[3]);
                        product.setNotice(productInfo[4].isEmpty() ? "" : productInfo[4]);
                        product.setBase(productInfo[5].isEmpty() ? "" : productInfo[5]);
                        product.setPrice(productInfo[6].isEmpty() ? 0 : Integer.parseInt(productInfo[6]));
                        product.setImgurl(productInfo[7].isEmpty() ? 0 : Integer.parseInt(productInfo[7]));
                        product.setDate(productInfo[8].isEmpty() ? null : LocalDateTime.parse(productInfo[8], formatter));
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
        String sql = String.format("INSERT INTO Product (name, location, around, notice, base, price, imgurl, DATE) " +
                "VALUES (:name, :location, :around, :notice, :base, :price, :imgurl, :date)", "Product");

        SqlParameterSource[] params = products.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        StopWatch queryStopwatch = new StopWatch();
        queryStopwatch.start();
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
        queryStopwatch.stop();

        System.out.println("products 사이즈는 = " + products.size());
        System.out.println("객체 생성 시간 = " + stopWatch.getTotalTimeSeconds());
        System.out.println("쿼리 실행 시간 = " + queryStopwatch.getTotalTimeSeconds());
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
