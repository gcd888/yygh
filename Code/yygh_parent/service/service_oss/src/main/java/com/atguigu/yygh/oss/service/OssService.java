package com.atguigu.yygh.oss.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.yygh.oss.prop.OssProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Henry Guan
 * @description
 * @since 2023-04-24
 */
@Service
public class OssService {

    @Autowired
    private OssProperties ossProperties;

    public String upload(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyId();
        String accessKeySecret = ossProperties.getKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketName();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String filename = new DateTime().toString("yyyy/mm/dd") +
                UUID.randomUUID().toString().replaceAll("-","")+file.getOriginalFilename();
        try {
            ossClient.putObject(bucketName,filename,file.getInputStream());
            return "https://" + ossProperties.getBucketName() + "." + ossProperties.getEndpoint() + "/" + filename;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }  finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
