package com.project.stress_traffic_system.product.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCondition {

    private String name;
    private Integer priceFrom;
    private Integer priceTo;
    private Integer page;

}
