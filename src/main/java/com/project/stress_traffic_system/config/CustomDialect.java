package com.project.stress_traffic_system.config;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

//하이버네이트를 통해 full text 검색 사용하는 함수 등록할 건데,
//MySQL8Dialect 상속받아서 MySql 기능에다가 내가 커스텀한 메서드 추가해서 사용할 것이다.

//product 엔티티에서 이름에 choco라는 게 들어가는 상품을 검색할 때,
//SELECT p FROM Product p WHERE match(p.name) against("choco" in boolean mode) 이런 식으로 사용하지 싶음
public class CustomDialect extends MySQL8Dialect {
    public CustomDialect() {
        super();

        registerFunction("match", new SQLFunctionTemplate(StandardBasicTypes.DOUBLE,
                "match(?1) against (?2 in boolean mode)"));
    }
}
