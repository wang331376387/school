package com.zl.yxt.config;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Minioconfiguration {

    private final Logger log = LoggerFactory.getLogger(Minioconfiguration.class.getName());

    //minio的信息
    @Value("${minio.host}")
    private String host;
    @Value("${minio.uname}")
    private String accessKey;
    @Value("${minio.pwd}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            return new MinioClient(host, accessKey, secretKey);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

}
