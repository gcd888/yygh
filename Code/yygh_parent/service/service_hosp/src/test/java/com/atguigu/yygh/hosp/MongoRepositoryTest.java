package com.atguigu.yygh.hosp;


import com.atguigu.yygh.hosp.bean.Actor;
import com.atguigu.yygh.hosp.repository.ActorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Henry Guan
 * @description mongo测试类
 * @since 2023-03-31
 */
@SpringBootTest
public class MongoRepositoryTest {

    @Autowired
    private ActorRepository actorRepository;

    //新增
    // insert只能做添加，不能做修改
    @Test
    public void testInsert() {
        Actor actor = new Actor();
        actor.setId("666");
        actor.setName("孙悟空");
        actor.setGender(false);
        actor.setBrith(new Date());
        actorRepository.insert(actor);
    }

    //修改
    // save 既可以添加，也可以修改
    @Test
    public void testModify() {
        Actor actor = new Actor();
        actor.setId("666");
        actor.setName("孙悟空");
        actor.setGender(true);
        actor.setBrith(new Date());
        actorRepository.save(actor);
    }

    //批量新增
    @Test
    public void testBatchInsert() {
        List<Actor> list = new ArrayList<>();
        list.add(new Actor("777","唐三藏",true,new Date()));
        list.add(new Actor("888","猪悟能",true,new Date()));
        list.add(new Actor("999","沙僧",true,new Date()));
        actorRepository.saveAll(list);
    }
    @Test
    public void testBatchInsert2() {
        List<Actor> list = new ArrayList<>();
        list.add(new Actor("1111","toby",true,new Date()));
        list.add(new Actor("1112","jack",true,new Date()));
        list.add(new Actor("1113","tom",true,new Date()));
        actorRepository.saveAll(list);
    }

    //删除
    @Test
    public void testDel() {
        actorRepository.deleteById("999");
    }

    //修改
    @Test
    public void testUpdate() {
        actorRepository.save(new Actor("999","沙僧",true,new Date()));
        actorRepository.save(new Actor("999","沙悟净",true,new Date()));
    }

    //查询
    @Test
    public void testQuery() {
        Optional<Actor> optional = actorRepository.findById("999");
        Actor actor = optional.get();
        System.out.println(actor);
    }

    //模糊查询
    @Test
    public void testFuzzyQuery() {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Actor actor = new Actor();
        actor.setName("o");
        Example<Actor> userExample = Example.of(actor, matcher);
        List<Actor> userList = actorRepository.findAll(userExample);
        System.out.println(userList);
    }

    //分页查询
    @Test
    public void testQueryPage() {
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        //0为第一页
        Pageable pageable = PageRequest.of(0, 10, sort);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Actor actor = new Actor();
        actor.setName("o");
        Example<Actor> userExample = Example.of(actor, matcher);
        //创建实例
        Example<Actor> example = Example.of(actor, matcher);
        Page<Actor> pages = actorRepository.findAll(example, pageable);
        System.out.println(pages);
    }

    //
    @Test
    public void testSelfMethods() {
        List<Actor> list = actorRepository.findByName("tom");
        for (Actor actor : list) {
            System.out.println(actor);
        }
    }
}
