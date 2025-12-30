package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.hosp.bean.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Henry Guan
 * @description Actor持久层接口
 * @since 2023-03-31
 */

public interface ActorRepository extends MongoRepository<Actor,String> {
    public List<Actor> findByName(String name);
}
