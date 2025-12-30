# 一、实现EasyExcel对Excel读操作

## 1、创建实体类

```java
@Data
public class Stu {

    //设置表头名称
    //设置列对应的属性
    @ExcelProperty(value = "学生编号",index = 0)
    private int sno;

    //设置表头名称
    //设置列对应的属性
    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;

}
```

## 2、创建读取操作的监听器

```java
public class ExcelListener extends AnalysisEventListener<Stu> {

    //创建list集合封装最终的数据
    List<Stu> list = new ArrayList<Stu>();

    //一行一行去读取excle内容
    @Override
    public void invoke(Stu user, AnalysisContext analysisContext) {
        System.out.println("***"+user);
        list.add(user);
    }

    //读取excel表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
```

# 3、调用实现最终的读取

```java
   public static void main(String[] args) throws Exception {

        String fileName = "F:\\11.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ReadData.class, new ExcelListener()).sheet().doRead();
}
```
