package com.project.stress_traffic_system.members.repository;

import com.project.stress_traffic_system.product.model.ImgS3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImgRepository extends JpaRepository<ImgS3, Integer> {
}
