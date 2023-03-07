package com.project.stress_traffic_system.product.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductImgDto {

    private int img_id;
    private String img_s3;

    public ProductImgDto(int id, String img_s3) {
        this.img_id = id;
        this.img_s3 = img_s3;
    }

}
