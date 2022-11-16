package com.zl.yxt.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
@Component
//阿里OSS
public class OSSConfiguration {

    private volatile OSS ossClient;
    private volatile OSSClientBuilder ossClientBuilder;

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.keyid}")
    private String accessKeyId;
    @Value("${oss.keysecret}")
    private String accessKeySecret;

    public OSSClientBuilder initOSSClientBuilder() {
        if (ossClientBuilder == null) {
            synchronized (OSSConfiguration.class) {
                if (ossClientBuilder == null) {
                    ossClientBuilder = new OSSClientBuilder();
                }
            }
        }
        return ossClientBuilder;
    }
    @Bean
    @Scope("prototype") //表示每次获得bean都会生成一个新的对象
    public OSS oSSClient() {
        if (ossClient == null) {
            synchronized (OSSConfiguration.class) {
                if (ossClient == null) {
                    ossClient = initOSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return ossClient;
    }
}
