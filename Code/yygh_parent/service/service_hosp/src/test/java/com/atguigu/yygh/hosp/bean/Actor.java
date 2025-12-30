package com.atguigu.yygh.hosp.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * @author Henry Guan
 * @description
 * @since 2023-03-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "actor")
public class Actor {
    @Id
    private String id;
    @Field(value = "name")
    private String name;
    private boolean gender;
    private Date brith;
}
