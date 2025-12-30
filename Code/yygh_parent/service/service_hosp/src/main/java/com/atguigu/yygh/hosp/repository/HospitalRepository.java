package com.atguigu.yygh.hosp.repository;


import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Henry Guan
 * @description Hospital mongo接口
 * @since 2023-04-03
 */

public interface HospitalRepository extends MongoRepository<Hospital,String> {
    Hospital findByHoscode(String hoscode);

    List<Hospital> findByHosnameLike(String name);
}
