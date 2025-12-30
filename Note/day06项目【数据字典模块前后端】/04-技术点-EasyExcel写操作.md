# 一、创建项目，实现EasyExcel对Excel写操作

## 1、pom中引入xml相关依赖

```java
<dependencies>
    <!-- https://mvnrepository.com/artifact/com.alibaba/easyexcel -->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>easyexcel</artifactId>
        <version>2.1.1</version>
    </dependency>
</dependencies>
```

# 2、创建实体类

**设置表头和添加的数据字段**

```java
@Data
public class Stu {

    //设置表头名称
    @ExcelProperty("学生编号")
    private int sno;

    //设置表头名称
    @ExcelProperty("学生姓名")
    private String sname;

}
```

## 3 、实现写操作

**（1）创建方法循环设置要添加到Excel的数据**

```java
//循环设置要添加的数据，最终封装到list集合中
private static List<Stu> data() {
    List<Stu> list = new ArrayList<Stu>();
    for (int i = 0; i < 10; i++) {
        Stu data = new Stu();
        data.setSno(i);
        data.setSname("张三"+i);
        list.add(data);
    }
    return list;
}
```

**（2）实现最终的添加操作（写法一）**

```java
public static void main(String[] args) throws Exception {
    // 写法1
    String fileName = "F:\\11.xlsx";
    // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
    // 如果这里想使用03 则 传入excelType参数即可
    EasyExcel.write(fileName, Stu.class).sheet("写入方法一").doWrite(data());
}
```

**（3）实现最终的添加操作（写法二）**

```java
public static void main(String[] args) throws Exception {
    // 写法2，方法二需要手动关闭流
    String fileName = "F:\\112.xlsx";
    // 这里 需要指定写用哪个class去写
    ExcelWriter excelWriter = EasyExcel.write(fileName, Stu.class).build();
    WriteSheet writeSheet = EasyExcel.writerSheet("写入方法二").build();
    excelWriter.write(data(), writeSheet);
    /// 千万别忘记finish 会帮忙关闭流
    excelWriter.finish();
}
```
