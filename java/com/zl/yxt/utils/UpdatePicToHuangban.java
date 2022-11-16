package com.zl.yxt.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

/**
 * 调用第三方接口，将图片上传至慌伴Img，但是效果不稳定，此方法弃用改用OSS了
 * 里面的删除本地暂存方法还在使用
 */
@Component
@Slf4j
public class UpdatePicToHuangban {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 从慌伴Img获取Token
     * @return
     */
    public String getToken(){
        JSONObject param = new JSONObject();
        param.put("email","a_feng321@163.com");
        param.put("password","假数据");
        JSONObject tokenData = restTemplate.postForObject("https://pic.liesio.com/api/token", param, JSONObject.class);
        String token = tokenData.getJSONObject("data").getString("token");
        return token;
    }

    /**
     * 上传图片至慌伴Img
     */
    public String uploadImage(File file) throws Exception {

        try {
            String url = "https://pic.liesio.com/api/upload";
            String token = getToken(); //拿到令牌
            JSONObject res = JPost(url,file,token);
            //TODO 需要将返回的图片路径进行保存
            log.info("上传名片接口调用结束结束,响应结果{}", res);
            JSONObject urlData = res.getJSONObject("data");
            String path = urlData.getString("url");
            System.err.println(path);
            return path;
        } catch (Exception e) {
            log.info("调用上传图片接口异常,错误信息{}", e.getMessage());
            throw new Exception("调用上传图片接口异常");
        }
    }

    /**
     *  发送文件MultipartFile类型的参数请求第三方接口
     * @param url  请求url
     * @param file 参数
     * @return 字符流
     * @throws IOException
     */
    public JSONObject JPost(String url, File file, String token) throws Exception {
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", new FileSystemResource(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "*/*");
        headers.add("connection", "Keep-Alive");
        headers.add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        headers.add("Accept-Charset", "utf-8");
        headers.add("Content-Type", "application/json; charset=utf-8");
//        headers.add("Content-Type", "multipart/form-data");
        headers.add("token", token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
        JSONObject body = response.getBody();
        return body;
    }

    /**
     * 接收处理传过来的文件，MultipartFile转File
     * @param file MultipartFile 类型的文件
     * @return
     */
    public File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            //以分隔符结尾
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + File.separator + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + File.separator + tempList[i]);//再删除空文件夹
            }
        }
        if (file.list().length == 0){
            flag = true;
        }
        return flag;
    }
}
