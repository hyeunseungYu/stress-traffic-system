package com.project.stress_traffic_system.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id") //todo sub_category_id 로 컬럼명 변경할지 결정
    private Long id;

    private String categoryName;

    public SubCategory(String categoryName) {
        this.categoryName = categoryName;
    }
}


