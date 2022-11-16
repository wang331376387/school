package com.zl.yxt.utils;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class MinioManager {
    private final static Logger log = LoggerFactory.getLogger(MinioManager.class.getName());

    @Value("${minio.host}")
    private String host;

    /*上传minio*/
    public void uploadFile(String accessKey, String secretKey, String bucketName
            , File file, String destFileName) throws IOException {
        log.info("uploadFile host=" + host + ";accessKey=" + accessKey + ";secretKey=" + secretKey
                + ";bucketName=" + bucketName);
        if (file.exists()) {
            if(StringUtils.isEmpty(destFileName)){
                destFileName = file.getName();
            }
            /*file类型转MultipartFile*/
            FileInputStream fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            try {
                MinioClient minioClient = new MinioClient(host, accessKey, secretKey,"us-east-1");
                boolean isExist = minioClient.bucketExists(bucketName);
                if (!isExist) {
                    minioClient.makeBucket(bucketName);
                }
                minioClient.putObject(bucketName, destFileName, multipartFile.getInputStream()
                        , multipartFile.getContentType());
            } catch (Exception e) {
                log.info("uploadFile error: " + e);
            }
            file.delete();//即刻删除（删除服务器上生成的临时文件）
        }
    }

    /**
     * 调用方法进行文件下载
     * @param request
     * @param response
     * @param accessKey
     * @param secretKey
     * @param bucketName
     * @param objectName
     * @return
     */

    public boolean download(HttpServletRequest request, HttpServletResponse response,
                            String accessKey, String secretKey, String bucketName, String objectName) {
        log.info("downloadFile host=" + host + ";accessKey=" + accessKey + ";secretKey=" + secretKey
                + ";bucketName=" + bucketName + ";objectName=" + objectName);

        try {
            //创建minioClient
            MinioClient minioClient = new MinioClient(host, accessKey, secretKey,"us-east-1");
            //判断桶是否存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                log.info("bucketName not exist:bucketName=" + bucketName);
                return false;
            }
            ObjectStat objectStat = minioClient.statObject(bucketName, objectName);
            return downloadFile(request, response, minioClient.getObject(bucketName, objectName)
                    , objectStat.name(), objectStat.length(), objectStat.contentType(), true, -1);
        } catch (Exception e) {
            log.info("downloadFile error: " + e);
        }
        return false;
    }

    /**
     * 从Minio下载文件
     * @param request
     * @param response
     * @param inputStream
     * @param fileName
     * @param fileLen
     * @param contentType
     * @param closeInputStream
     * @param maxAge
     * @return
     * @throws Exception
     */
    public boolean downloadFile(HttpServletRequest request
            , HttpServletResponse response, InputStream inputStream
            , String fileName, long fileLen, String contentType
            , boolean closeInputStream, long maxAge) throws Exception {
        //对中文文件名进行编码
        fileName = fixStr(request, fileName);
        //判断内容类型
        if (!org.springframework.util.StringUtils.isEmpty(contentType)) {
            response.setContentType(contentType);
        } else {
            //设置为二进制
            response.setContentType("application/octet-stream");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("X-Actual-Content-Length", String.valueOf(fileLen));

        cors(response, maxAge);
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.info("download file error.e=" + e);
            return false;
        } finally {
            if (closeInputStream) {
                inputStream.close();
            }
            out.flush();
            out.close();
        }
        return true;
    }

    /**
     * 设置请求编码方式
     * @param request
     * @param str
     * @return
     * @throws Exception
     */
    public String fixStr(HttpServletRequest request, String str) throws Exception {
        if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
            //对中文以UTF-8进行编码
            str = java.net.URLEncoder.encode(str, "UTF-8");
        } else {
            str = new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return str;
    }

    /**
     * 设置响应头信息
     * @param response
     * @param maxAge
     */
    public void cors(HttpServletResponse response, long maxAge) {
        response.setContentType("APPLICATION/OCTET-STREAM");//返回格式为流
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, HEAD");
        if (maxAge > 0) {
            response.setHeader("Access-Control-Max-Age", String.valueOf(maxAge));
            response.setHeader("Cache-Control", "max-age=" + maxAge);
        }
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers", "Cache-Control, Accept-Ranges, Content-Encoding, Content-Length, Content-Range, X-Actual-Content-Length, Content-Disposition");
    }

}
