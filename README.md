# minio使用
  
  对象存储fastDFS是国产的，尽管好用但是安装部署比较麻烦，现出现了一个对象存储minio，使用方法详见[官网](https://docs.min.io/cn/)
  
  功能特性：
  
  - Amazon S3兼容  Minio使用Amazon S3 v2 / v4 API。可以使用Minio SDK，Minio Client，AWS SDK和AWS CLI访问Minio服务器。
  
  - 数据保护  Minio使用Minio Erasure Code来防止硬件故障。也许会损坏一半以上的driver，但是仍然可以从中恢复。
  
  - 高度可用  Minio服务器可以容忍分布式设置中高达（N / 2）-1节点故障。而且，您可以配置Minio服务器在Minio与任意Amazon S3兼容服务器之间存储数据。
  
  - Lambda计算  Minio服务器通过其兼容AWS SNS / SQS的事件通知服务触发Lambda功能。支持的目标是消息队列，如Kafka，NATS，AMQP，MQTT，Webhooks以及Elasticsearch，Redis，Postgres和MySQL等数据库。
  
  - 加密和防篡改  Minio为加密数据提供了机密性，完整性和真实性保证，而且性能开销微乎其微。使用AES-256-GCM，ChaCha20-Poly1305和AES-CBC支持服务器端和客户端加密。加密的对象使用AEAD服务器端加密进行防篡改。
  
  - 可对接后端存储  除了Minio自己的文件系统，还支持DAS、 JBODs、NAS、Google云存储和Azure Blob存储。
  
  - sdk支持  基于Minio轻量的特点，它得到类似Java、Python或Go等语言的sdk支持
  
  ## docker快速部署使用
  
  ```shell
  docker run -p 9000:9000 minio/minio server /data
  ```
  
  ## java使用maven包
  
  ```java
  <dependency>
      <groupId>io.minio</groupId>
      <artifactId>minio</artifactId>
      <version>3.0.10</version>
  </dependency>
  ```
  
  ## 主要代码
  
  ```java
  // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient("https://play.min.io", "Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
  
        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists("asiatrip");
        if(isExist) {
          System.out.println("Bucket already exists.");
        } else {
          // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
          minioClient.makeBucket("asiatrip");
        }
  
        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject("asiatrip","asiaphotos.zip", "/home/user/Photos/asiaphotos.zip");
  ```
  
  ## 前端访问
  
  1、不加密访问
  
  **进行客户端设置存储桶添加访问权限*,就可以使用路径径+存储桶+文件名访问了**。可以使用代码设置
  
  ```java
  minioClient.setBucketPolicy("asiatrip","*", PolicyType.READ_ONLY);
  ```
  
  2、加密访问
  
  ```java
  // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
          MinioClient minioClient = new MinioClient("http://127.0.0.1:9000", "minioadmin", "minioadmin");
          Map<String, String> map = new HashMap<>();
          map.put("from","1");
          String url = minioClient.getPresignedObjectUrl(Method.GET,
                  "asiatrip",//存储桶名
                  "1.jpg",//文件名
                  10,//超时时间——秒为单位
                  map//可以是前端过来的加密数据
          );
          System.out.println(url);
  ```
  
  其它的访问方法
  
  `presignedGetObject`——返回一个预签名的URL，以下载存储桶中具有给定到期时间的对象
  
  `presignedGetObject`——返回一个预签名的URL，以下载存储桶中具有给定到期时间的对象
  
  还有一个默认是7天的，参数没有时间