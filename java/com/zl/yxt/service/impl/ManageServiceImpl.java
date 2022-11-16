package com.zl.yxt.service.impl;

import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.mapper.ManageMapper;
import com.zl.yxt.pojo.Manages;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.ManageService;
import com.zl.yxt.utils.AliyunOSSUtil;
import com.zl.yxt.utils.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Service
@Slf4j
public class ManageServiceImpl implements ManageService {

    @Autowired
    private QuartzUtils quartzUtils;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Autowired
    private ManageMapper manageMapper;

    @Autowired
    private InformationMapper informationMapper;

    //创建任务
    @Override
    public ResultVO createManage(Manages manages) throws Exception {

        String jobName = manages.getMname();
        String jobGroup = manages.getTid().toString(); //分组即发布教师编号
        String jobClass = manages.getJobClass();
        Date overTime = manages.getOvertime();
        try{
            manageMapper.saveManage(manages); //保存任务信息
        }catch (Exception e){
            return ResultVO.customize(220,null,null);
        }
        manageMapper.saveManageClass(manages.getClassesvalue(), manages.getId()); //保存任务班级信息
        try {
            quartzUtils.addJob(jobName,jobGroup,jobClass,overTime); //创建定时任务
            return ResultVO.success();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return ResultVO.error();
    }

    //上传文件至OSS
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        String name = file.getOriginalFilename(); //文件名称
        String fileUrl = aliyunOSSUtil.uploadOther(inputStream,name,"manage/"); //上传OSS的图片路径
        return fileUrl;
    }

    //查询任务列表
    @Override
    public ResultVO showMyManageList(Map<String,String> map) throws UnsupportedEncodingException {
        List<Map<String,Object>> manageList = new ArrayList<>();
        if (map.containsKey("mid")){
            //查询单个
            manageList = manageMapper.showManage(map.get("mid"));
            if (manageList.get(0).containsKey("file") && !manageList.get(0).get("file").toString().isEmpty()){
                String path = manageList.get(0).get("file").toString();
                int a = path.lastIndexOf("/");
                String fileName = path.substring(a+1,path.length());
                fileName = URLDecoder.decode(fileName,"UTF-8");
                manageList.get(0).put("fileName",fileName);
            }

        }else {
            if (map.get("role").equals("teacher")){
                //查询全部(教师）
                manageList = manageMapper.showMyManageList(map.get("id"));
            }else {
                //查询全部(学生）
                manageList = manageMapper.showStudentList(map);
                //查询我回复过的
                List<Integer> reads = new ArrayList<>();
                reads = manageMapper.showMyReads(map.get("id"));
                for (int i=0;i<reads.size();i++){
                    for (Map<String,Object> task : manageList){
                        if (reads.get(i)==task.get("id")){
                            task.put("noread","no"); //已读
                            break;
                        }
                    }
                }
            }
        }
        return ResultVO.success(manageList);
    }

    //创建回复
    @Override
    public ResultVO CreateFeedBack(Map<String, String> param) {
        param.put("read","no");
        if (!param.containsKey("message")){
            param.put("message","");
        }
        String life="";
        if (param.containsKey("life")){
            life = param.get("life");
        }
        Integer num = manageMapper.selectFeedBack(param);
        if (num == 0 && life.equals("1")){
            manageMapper.CreateFeedBack(param); //创建
        }
        if (num == 1 && param.get("message").length()>0 && life.equals("1")){
            manageMapper.updateFeedBack(param); //修改
        }
        return ResultVO.success();
    }

    // 查看学生（1参与/0未参与）
    @Override
    public ResultVO showStudentList(Map<String, String> param) {
        List<Map<String,Object>> data = new ArrayList<>();
        if (param.get("type").equals("1")){
            data = manageMapper.showJoin(param.get("mid"));
        }else {
            data = manageMapper.showNoJoin(param.get("mid"));
        }
        return ResultVO.success(data);
    }

    //查看学生回复
    @Override
    public ResultVO showFeedBackList(Map<String, String> param) {
        List<Map<String,Object>> data = manageMapper.showFeedBackList(param.get("mid"));
        return ResultVO.success(data);
    }

    //查看我的所有学神申请
    @Override
    public ResultVO getAllApply(String tid) {
        List<Map<String,Object>> data = manageMapper.getAllApply(tid);
        return ResultVO.success(data);
    }

    /**
     * 根据ID查询申请信息
     * @param id
     * @return
     */
    @Override
    public ResultVO getApplyById(Integer id) {
        Map<String,Object> data = manageMapper.getApplyById(id);
        return ResultVO.success(data);
    }

    //同意申请
    @Override
    public ResultVO acceptApply(Integer id) throws Exception {
        //查询申请信息
        Map<String,Object> data = manageMapper.getApplyById(id);
        data.put("id",data.get("uid"));
        String role = data.get("role").toString();
        String classes[] = new String[20];
        //同步到个人信息
        if (role.equals("teacher")){
            classes = data.get("classid").toString().split(",");
            data.remove("cid");
            /*先删除原班级，再添加新班级*/
            informationMapper.deleteClasses(data.get("id").toString());
            informationMapper.insertClasses(data.get("id").toString(),classes);
        }else {
            String cids = data.get("cid").toString();
        }
        informationMapper.updateInfo(data); //更新用户表
        manageMapper.updateUserApply(id); //更新申请
        return ResultVO.success();
    }

}
