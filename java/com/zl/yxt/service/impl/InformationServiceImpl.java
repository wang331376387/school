package com.zl.yxt.service.impl;

import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.pojo.School;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.informationService;
import com.zl.yxt.utils.AliyunOSSUtil;
import com.zl.yxt.utils.UpdatePicToHuangban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class InformationServiceImpl implements informationService {

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private UpdatePicToHuangban updatePicToHuangban;

    @Value("${uploadpath}")
    public String filePath; //暂存区路径

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    //获取年级和班级信息
    @Override
    public ResultVO getSchoolAndClass() {
        List<School> school = informationMapper.getSchoolAndClass();
        return ResultVO.success(school);
    }

    /*更新信息（认证）*/
    @Override
    public ResultVO updateInformation(Map<String, Object> information,String cid[]) throws Exception {
        String pic = uploadPic((String) information.get("id"));
        if (pic != null){
            information.put("photo",pic);
        }
        if (information.get("role").equals("teacher")){
            /*先删除原班级，再添加新班级*/
            informationMapper.deleteClasses((String) information.get("id"));
            informationMapper.insertClasses((String) information.get("id"),cid);
        }
        information.remove("role");
        informationMapper.updateInfo(information); //更新用户表

        return ResultVO.success();
    }

    /**
     * 查询我的班级（教师）
     * @param id
     * @return
     */
    @Override
    public ResultVO selectMyClasses(Integer id) {
        return ResultVO.success(informationMapper.selectMyClasses(id));
    }

    //保存认证申请
    @Override
    public ResultVO certifyApply(Map<String, String> information) {
        information.put("isread","0");//未读
        int num = informationMapper.findUserApply(information.get("uid"));
        //如果改用户没有申请则添加，否则更新
        if (num>0){
            informationMapper.updateApply(information);
        }else {
            informationMapper.insertApply(information);
        }
        return ResultVO.success();
    }

    //更改头像
    @Override
    public ResultVO changeAvatar(String id) throws Exception {
        String picture = uploadPic(id); //上传OSS的图片路径
        informationMapper.updateAvatar(id,picture);
        return ResultVO.success();
    }

    /**
     * 读取暂存区图片，循环上传
     * @throws Exception
     */
    String uploadPic(String uid) throws Exception {
        File file = new File(filePath + File.separator + uid);
        String picture = "";
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            list.add(file);
            File[] files;
            if (!list.isEmpty()){
                files = list.removeFirst().listFiles();
                if (files.length > 0){
                    picture = aliyunOSSUtil.upload(files[0],"touxiang/"); //上传OSS的图片路径
                }else {
                    return null;
                }
            }else {
                return null;
            }
        }
        return picture;
    }
}
