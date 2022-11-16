package com.zl.yxt.utils;

import com.aliyun.oss.OSS;
import com.zl.yxt.mapper.ManageMapper;
import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.mapper.MoodMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@Slf4j
public class AliyunOSSUtil {

    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg",
            ".jpeg", ".gif", ".png"}; //上传类型

    @Autowired
    private OSS ossClient;

    //下面三个主要用于查询文件名称数量，重名在后面加123……
    @Autowired
    private MoodMapper moodMapper;

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private ManageMapper manageMapper;

    @Value("${oss.preView}")
    private String preView;
    @Value("${oss.bucketname}")
    private String bucketname;

    /**
     * 将图片上传oss
     * @param uploadFile
     * @param filehost 路径，文件夹
     * @return
     * @throws Exception
     */
    public String upload(File uploadFile,String filehost) throws Exception {
        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getName(),
                    type)) {
                isLegal = true;
                break;
            }
        }

        if (!isLegal) {
            throw new Exception("格式错误"); //格式不正确
        }
        //文件新路径
        String fileName = getFileName(uploadFile.getName(),filehost);
        String filePath = filehost + fileName;
        // 上传到阿里云
        try {
            ossClient.deleteObject(bucketname,filePath); //先删除文件
            ossClient.putObject(bucketname,filePath,uploadFile); //上传
            String encodeName = URLEncoder.encode(fileName,"UTF-8");
            return preView + filehost + encodeName; //图片访问地址，由oss桶和服务器和文件名拼接成的
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 其他类型文件上传
     * @param inputStream
     * @param name
     * @param filehost
     * @return
     * @throws Exception
     */
    public String uploadOther(InputStream inputStream, String name, String filehost) throws Exception {

        //文件新路径
        String fileName = getFileName(name,filehost);
        String filePath = filehost + fileName;
        // 上传到阿里云
        try {
            ossClient.deleteObject(bucketname,filePath); //先删除文件
            ossClient.putObject(bucketname,filePath,inputStream); //上传
            String encodeName = URLEncoder.encode(fileName,"UTF-8");
            return preView + filehost + encodeName; //访问地址，由oss桶和服务器和文件名拼接成的
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拿到上传oss文件名
     * @param fileName
     * @return
     */
    public String getFileName(String fileName,String filehost) throws UnsupportedEncodingException {
        String encodeName = URLEncoder.encode(fileName,"UTF-8");
        /*最后一个'.'前为名，后为文件类型*/
        int num = encodeName.lastIndexOf(".");
        String preOriginalFilename = preView + filehost + encodeName.substring(0,num); // 访问地址加文件名
        String fixOriginalFilename = encodeName.substring(num,encodeName.length());// 文件类型
        String name;
        Integer fileNum = 0;
        //到数据库查询该文件名数量，filehost是上传到OSS的文件夹，同一个文件夹文件重名处理
        if (filehost.equals("mood/")){
            fileNum = moodMapper.findFile(preOriginalFilename,fixOriginalFilename);
        }else if (filehost.equals("touxiang/")){
            fileNum = informationMapper.findFile(preOriginalFilename,fixOriginalFilename);
        }else {
            fileNum = manageMapper.findFile(preOriginalFilename,fixOriginalFilename);
        }
        if (fileNum > 0){
            //如果已有此文件名那么文件名后面加（1）有两个加（2）……
            name = fileName.substring(0,num) + "(" + fileNum + ")" + fixOriginalFilename;
        }else {
            name = fileName; //原名
        }
        return name;
    }

}
