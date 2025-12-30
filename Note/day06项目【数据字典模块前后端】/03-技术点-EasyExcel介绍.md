# 一、EasyExcel介绍

1、数据导入：减轻录入工作量

2、数据导出：统计信息归档

3、数据传输：异构系统之间数据传输

# 二、EasyExcel特点

* Java领域解析、生成Excel比较有名的框架有Apache poi、jxl等。但他们都存在一个严重的问题就是非常的耗内存。如果你的系统并发量不大的话可能还行，但是一旦并发上来后一定会OOM或者JVM频繁的full gc。
* EasyExcel是阿里巴巴开源的一个excel处理框架， **以使用简单、节省内存著称** 。EasyExcel能大大减少占用内存的主要原因是在解析Excel时没有将文件数据一次性全部加载到内存中，而是从磁盘上一行行读取数据，逐个解析。
* EasyExcel采用一行一行的解析模式，并将一行的解析结果以观察者的模式通知处理（AnalysisEventListener）

EasyExcel是一个基于Java的简单、省内存的读写Excel的开源项目。在尽可能节约内存的情况下支持读写百M的Excel。

文档地址：[https://alibaba-easyexcel.github.io/index.html](https://alibaba-easyexcel.github.io/index.html)

github地址：[https://github.com/alibaba/easyexcel](https://github.com/alibaba/easyexcel)

### 1 导出示例

示例链接：[https://alibaba-easyexcel.github.io/quickstart/write.html](https://alibaba-easyexcel.github.io/quickstart/write.html)

### 2 导入示例

示例链接：[https://alibaba-easyexcel.github.io/quickstart/read.html](https://alibaba-easyexcel.github.io/quickstart/read.html)

### 3 EasyExcel集成

项目添加依赖

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
