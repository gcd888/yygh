package com.atguigu.mp.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "User")
public class User {
    @TableId(value = "id")
    private Long id;
    @TableField(value = "name")
    private String name;
    private Integer age;
    private String email;
}
