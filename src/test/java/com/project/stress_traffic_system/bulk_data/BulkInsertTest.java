/*
package com.project.stress_traffic_system.bulk_data;

import com.github.javafaker.Faker;
import com.project.stress_traffic_system.product.model.Category;
import com.project.stress_traffic_system.product.repository.CategoryRepository;
import com.project.stress_traffic_system.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class BulkInsertTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 더미데이터 저장")
    void bulkInsert() {
//        productRepository.bulkInsert(); //todo
        productRepository.bulkInsertMembers();
    }

    @Test
    @DisplayName("회원 더미데이터 200만개 생성")
    void usersFakeData() {

        List<String> dataList = new ArrayList<>();

        //17개 지역
        String[] address = {"서울", "경기", "인천", "충남", "충북", "대전", "강원", "전남", "전북", "대구", "부산", "울산", "광주", "세종", "경북", "경남", "제주"};

        Random random = new Random();

        for (int i = 0; i < 2000000; i++) {

            StringBuilder builder = new StringBuilder();
            builder.append("user")
                    .append(i)
                    .append(",")
                    .append("$2a$10$8aYgCESquxcDpVSMTGHGEOawngL3UncAMpBwcux.Zr4WbpClUYerG")  //Test1234
                    .append(",")
                    .append("MEMBER")
                    .append(",")
                    .append(address[random.nextInt(17)]);

            dataList.add(builder.toString());
        }
        System.out.println(dataList.size());

            try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {
                for (String data : dataList) {
                    writer.write(data);
                    writer.println();
                }
                writer.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
    }
    @Test
    @DisplayName("상품 더미데이터 100만개 생성")
    void productFakerData() {

        List<String> dataList = new ArrayList<>();

        Random random = new Random();
        Faker faker = new Faker();

        for (int i = 0; i < 1000000; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append(random.nextInt(15) + 1) //카테고리id
                    .append(",")
                    .append(faker.name().fullName())
                    .append(",")
                    .append(random.nextInt(50000) + 10000)
                    .append(",")
                    .append(faker.lorem().sentence())
                    .append(",")
                    .append(2500)
                    .append(",")
                    .append(random.nextInt(20))
                    .append(",")
                    .append(random.nextInt(50000))
                    .append(",")
                    .append(random.nextInt(1000))
                    .append(",")
                    .append(faker.lorem().sentence())
                    .append(",")
                    .append(random.nextInt(1000) + 100);

            dataList.add(builder.toString());
        }
        System.out.println(dataList.size());

        try (PrintWriter writer = new PrintWriter(new File("product.csv"))) {
            for (String data : dataList) {
                writer.write(data);
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}*/
