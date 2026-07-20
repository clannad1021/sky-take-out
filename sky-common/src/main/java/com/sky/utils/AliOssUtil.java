package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.sky.properties.AliOssProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
@Component
public class AliOssUtil {

    /*private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;*/
    @Autowired
    private AliOssProperties aliOssProperties;
    /**
     * 文件上传
     *
     * @param
     * @param
     * @return
     */
    public String upload(MultipartFile file) throws IOException {

        String endpoint = aliOssProperties.getEndpoint();
        String bucketName = aliOssProperties.getBucketName();
        String accessKeyId = aliOssProperties.getAccessKeyId();
        String accessKeySecret = aliOssProperties.getAccessKeySecret();

        //获取输入流
        InputStream inputStream = file.getInputStream();
        //获取文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件类型格式
        String type = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        //UUID防重名
        String fileName = UUID.randomUUID().toString() + type;
        //上传文件到OSS
        //OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //ossClient.putObject(bucketName, fileName, inputStream);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);
        /**
         * 以下全部重写
         */
       /* try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }*/
        //文件访问路径
        String url = "https://staff-admin-backend-testproject.oss-cn-shenzhen.aliyuncs.com/" + fileName;
        //释放资源
        ossClient.shutdown();
        log.info("文件上传到:{}", url);

        return url;




       /* //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);
        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();*/

    }
}
