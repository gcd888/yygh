package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-06
 */

public interface DepartmentRepositoty extends MongoRepository<Department,String> {
    Department findByHoscodeAndDepcode(String hoscode, String depcode);
}
