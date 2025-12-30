package com.atguigu.yygh.hosp;


import com.atguigu.yygh.hosp.bean.Actor;
import com.mongodb.client.result.DeleteResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Henry Guan
 * @description mongo测试类
 * @since 2023-03-31
 */
@SpringBootTest
public class MongoTemplateTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    //新增
    // insert只能做添加，不能做修改
    @Test
    public void testInsert() {
        Actor actor = new Actor();
        actor.setId("111");
        actor.setName("张三");
        actor.setGender(true);
        actor.setBrith(new Date());
        mongoTemplate.insert(actor);
    }

    //修改
    // save 既可以添加，也可以修改
    @Test
    public void testModify() {
        Actor actor = mongoTemplate.findById("111",Actor.class);
        actor.setName("张三丰");
        mongoTemplate.save(actor);
    }

    //批量新增
    @Test
    public void testBatchInsert() {
        List<Actor> list = new ArrayList<>();
        list.add(new Actor("222","李四",true,new Date()));
        list.add(new Actor("333","王五",true,new Date()));
        list.add(new Actor("444","赵六",true,new Date()));
        mongoTemplate.insert(list,Actor.class);
    }

    //删除
    @Test
    public void testDel() {
        Query query = new Query(Criteria.where("name").is("赵六").and("gender").is(true));
        DeleteResult remove = mongoTemplate.remove(query, Actor.class);
        System.out.println(remove.getDeletedCount());
    }

    //修改
    @Test
    public void testUpdate() {
        Query query = new Query(Criteria.where("name").is("赵六"));
        Update update = new Update();
        update.set("gender",false);
        mongoTemplate.upsert(query,update,Actor.class);
    }

    //查询
    @Test
    public void testQuery() {
        Actor actor = mongoTemplate.findById("64268cb2a172243feabe6f0d", Actor.class);
        System.out.println(actor);
    }

    //模糊查询
    @Test
    public void testFuzzyQuery() {
        Pattern pattern = Pattern.compile(".*三.*");
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<Actor> list = mongoTemplate.find(query, Actor.class);
        for (Actor actor : list) {
            System.out.println(actor);
        }
    }

    //分页查询
    @Test
    public void testQueryPage() {
        int pageNum = 1;
        int pageSize = 2;
        Query query = new Query(Criteria.where("gender").is(true));
        long total = mongoTemplate.count(query, Actor.class);
        List<Actor> list = mongoTemplate.find(query.skip((pageNum - 1) * pageSize).limit(pageSize), Actor.class);
        for (Actor actor : list) {
            System.out.println(actor);
        }
    }
}
