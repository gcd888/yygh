# 第一步：创建coder模块，导入依赖信息

说明：mysql版本与本机环境一致

```java
<dependencies>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.3.1</version>
    </dependency>

    <dependency>
        <groupId>org.apache.velocity</groupId>
        <artifactId>velocity-engine-core</artifactId>
        <version>2.0</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>
</dependencies>
```
# 第二步：编写代码生成器代码：
```java
package com.atguigu.coder;

import com.baomidou.mybatisplus.annotation.DbType; import com.baomidou.mybatisplus.generator.AutoGenerator; import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


/**
 * ClassName：代码生成器
 * Author：Henry Guan
 * Date：2022/11/16
 * Motto:Ideal is always young
 **/
public class CodeGet {
    public static void main(String[] args) {
        // 1.创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2.全局配置
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
               String projectPath = System.getProperty("user.dir");
        //gc.setOutputDir(projectPath + "/src/main/java");
        gc.setOutputDir("E:\\Study\\Project\\project_hospital\\yygh_parent\\service\\service_hosp" + "/src/main/java");

        gc.setServiceName("%sService");// %s是为了去掉service接口的首字母I（e.g:IUserService）
        gc.setAuthor("Admin");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 3.数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/yygh_hosp?serverTimezone=Asia/Shanghai");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("p@ssw0rd");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4.包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("hosp");// 模块名
        pc.setParent("com.atguigu.yygh");

        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5.策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("hospital_set");// 表名
        strategy.setNaming(NamingStrategy.underline_to_camel);// 数据库表映射到实体的命名策略

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);// 数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true);// lombok模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true);// restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true);// url中驼峰转连字符

        mpg.setStrategy(strategy);

        // 6.执行
        mpg.execute();
    }
```