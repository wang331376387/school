package com.zl.yxt.controller;

import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.informationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/information")
@Slf4j
public class informationController {

    @Autowired
    private informationService service;

    @Value("${uploadpath}")
    public String path;

    /**
     * 查询所有的学校和班级
     * @return
     */
    @RequestMapping(value = "/getSchoolAndClass",method = RequestMethod.GET)
    public ResultVO getSchoolAndClass(){
        ResultVO data = service.getSchoolAndClass();
        return data;
    }

    /**
     * 信息认证
     * @param id 用户编号
     * @param name 姓名
     * @param sex 性别
     * @param role 角色
     * @param sid 学校编号
     * @param cid 班级编号
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/certify",method = RequestMethod.POST)
    public ResultVO certify(@RequestParam(value="id")String id,
                            @RequestParam(value="name")String name,
                            @RequestParam(value="sex")String sex,
                            @RequestParam(value="role")String role,
                            @RequestParam(value="sid")String sid,
                            @RequestParam(value="cid") String cid[]) throws Exception {

        Map<String,Object> information = new HashMap<>();
        information.put("id",id);
        information.put("name",name);
        information.put("sex",sex);
        information.put("role",role);
        information.put("sid",sid);
        if (role.equals("student")){
            information.put("cid",cid[0]);
        }
        /*for (String key : information.keySet()) {
            System.err.println("Map.get(" + key + ") = " + information.get(key));
        }*/
        ResultVO data = service.updateInformation(information,cid);
        return data;
    }

    //认证申请
    @RequestMapping(value = "/certifyApply",method = RequestMethod.POST)
    public ResultVO certifyApply(@RequestParam(value="id")String id,
                            @RequestParam(value="name")String name,
                            @RequestParam(value="sex")String sex,
                            @RequestParam(value="role")String role,
                            @RequestParam(value="sid")String sid,
                            @RequestParam(value="cid") String cid[]) throws Exception {

        Map<String,String> information = new HashMap<>();
        information.put("uid",id);
        information.put("name",name);
        information.put("sex",sex);
        information.put("role",role);
        information.put("sid",sid);
        if (role.equals("student")){
            information.put("cid",cid[0]);
        }
        StringBuffer classid = new StringBuffer();
        classid.append(cid[0]);
        for (int i=1;i<cid.length;i++){
            classid.append(",").append(cid[i]);
        }
        information.put("classid",classid.toString());
        ResultVO data = service.certifyApply(information);
        return data;
    }

    /**
     * 查询我的班级（教师）
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectMyClasses/{id}",method = RequestMethod.GET)
    public ResultVO selectMyClasses(@PathVariable("id") Integer id){
        ResultVO data = service.selectMyClasses(id);
        return data;
    }

    /**
     * 更改头像
     * @param file
     * @param id
     * @return
     */
    @RequestMapping(value = "/changeAvatar",method = RequestMethod.POST)
    public ResultVO changeAvatar(@RequestParam("file") MultipartFile file, String id) throws Exception {

        //上传文件存储路径
        String filePath = path;
        //文件夹不存在就创建
        File fileflor = new File(filePath + File.separator + id);
        if (!fileflor.exists()) {
            fileflor.mkdirs();
        }
        if(!file.isEmpty()) {
            //上传文件的全路径
            File tempFile = new File(filePath + File.separator
                    + id + File.separator + file.getOriginalFilename());
            //文件上传
            file.transferTo(tempFile);
        }

        ResultVO data = service.changeAvatar(id);

        return data;
    }
}
