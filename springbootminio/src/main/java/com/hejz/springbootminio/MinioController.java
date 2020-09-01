package com.hejz.springbootminio;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @author: hejz
 * @Description:
 * @Date: 2020/9/1 17:49
 */
@Controller
public class MinioController {

    @Autowired
    private MinioClient minioClient;
    private String buketName = "contex";

    /**
     * 上传文件
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     * @throws RegionConflictException
     * @throws InvalidArgumentException
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidArgumentException {
        boolean contex = minioClient.bucketExists(buketName);
        //检查存储桶是否存在，不存在建立存储桶
        if(!contex){
            minioClient.makeBucket(buketName);
        }
        int i = file.getOriginalFilename().lastIndexOf(".");
        String suffix = file.getOriginalFilename().substring(i + 1);
        String fileName = UUID.randomUUID().toString().replaceAll("-", "")+"."+suffix;
        minioClient.putObject(buketName, fileName,file.getInputStream(),file.getSize(),file.getOriginalFilename());
        return "上传成功："+fileName;
    }

    /**
     * 获取加密路径
     * @param name
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     * @throws InvalidExpiresRangeException
     */
    @GetMapping("getUrl")
    @ResponseBody
    public String getUrl(@RequestParam("name") String name) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidExpiresRangeException {
        String url = minioClient.presignedGetObject(buketName, name, 10, null);
        return url;
    }
}
