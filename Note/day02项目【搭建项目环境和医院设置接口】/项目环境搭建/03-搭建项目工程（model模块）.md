# 一、搭建model模块

## 1、在父工程yygh_parent下面创建模块model

选择父工程点击New Module，Name填写model，点击create

![1678759748212](image/1678759748212.png)

## 2、添加项目需要的依赖

```java
 <dependencies>
 <dependency>
 <groupId>org.projectlombok</groupId>
 <artifactId>lombok</artifactId>
 </dependency>
 <!--mybatis-plus-->
 <dependency>
 <groupId>com.baomidou</groupId>
 <artifactId>mybatis-plus-boot-starter</artifactId>
 <scope>provided </scope>
 </dependency>
 <!--swagger-->
 <dependency>
 <groupId>io.springfox</groupId>
 <artifactId>springfox-swagger2</artifactId>
 <scope>provided </scope>
 </dependency>
 <dependency>
 <groupId>com.alibaba</groupId>
 <artifactId>easyexcel</artifactId>
 <scope>provided </scope>
 </dependency>
 <dependency>
 <groupId>org.springframework.boot</groupId>
 <artifactId>spring-boot-starter-data-mongodb</artifactId>
 <scope>provided </scope>
 </dependency>
 <dependency>
 <groupId>com.alibaba</groupId>
 <artifactId>fastjson</artifactId>
 <scope>provided </scope>
 </dependency>
 </dependencies>
```
## 3、复制项目实体类和VO类
![1678760114253](image/1678760114253.png)
![1678760133554](image/1678760133554.png)