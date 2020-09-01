package com.hejz.springbootminio.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: hejz
 * @Description: minio客户端配置
 * @Date: 2020/9/1 17:00
 */
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient("http://127.0.0.1:9000", "minioadmin", "minioadmin");
    }
}
