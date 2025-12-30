package com.atguigu.mp;

import com.atguigu.mp.bean.User;
import com.atguigu.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MpApplicationTests {

    @Autowired
    private UserMapper userMapper;

    /**
     * mp主键生成策略
     *     NONE(1):默认的mp主键生成策略，会使用雪花算法生成19位的数值类型递增的唯一标识符
     *     INPUT(2)：要让用户自己输入主键
     *     AUTO(0)：表示使用数据表的自增主键，要求表的列是数值类型，列是递增的
     *     ASSIGN_ID(3)：表示如果用户自己输入id，就用自己输入的id，否则用mp主键生成策略19位的数值类型递增的唯一标识符
     *     ASSIGN_UUID(4);表示如果用户自己输入uuid，就用自己输入的uuid，否则用mp生成uuid
     */

    /**
     * 查询
     */
    @Test
    public void queryAll() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 新增
     * 默认情况下，mp会使用雪花算法生成19位的数值类型递增的唯一标识符
     */
    @Test
    public void addTest() {
        User user = new User(null,"ZhangSan",18,"zhangsan@baomidou.com");
        userMapper.insert(user);
    }

    /**
     * 更新
     */
    @Test
    public void updateTest() {
        User user = new User(1625793803100864514L,null,20,null);
        userMapper.updateById(user);
    }
}
