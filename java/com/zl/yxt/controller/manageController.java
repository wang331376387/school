package com.zl.yxt.controller;

import com.zl.yxt.pojo.Manages;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.ManageService;
import com.zl.yxt.utils.SendNotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/manage")
public class manageController {

    @Autowired
    private ManageService manageService;

    @Autowired
    private SendNotificationUtil sendNotificationUtil;

    /**
     * 创建任务 添加 job和trigger同用名称和分组,使用班级编号分组
     * @param manages
     * @return
     */
    @RequestMapping(value = "/createManage",method = RequestMethod.POST)
    public ResultVO createManage(@RequestBody Manages manages) throws Exception {

        Integer cron = manages.getCron();
        if (cron != null && cron > 10){ //秒数不为空就用秒数
            manages.setOvertime(new Date(System.currentTimeMillis()+1000*cron));
        }
        manages.setJobClass("com.zl.yxt.job.impl.OverManage");
        manages.setLife(1); //初始化状态，0为结束
        manages.setStarttime(new Date()); //开始时间
        manages.setMname(manages.getTid()+"/"+manages.getMname()); //任务名称前面默认加教师编号
        ResultVO data = manageService.createManage(manages);
        return data;
    }

    /**
     * 将文件上传OSS返回文件路径到前台
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    public ResultVO uploadFile(@RequestParam(value = "file") MultipartFile file) throws Exception {
        String fileUrl = "";
        if (!file.isEmpty()){
            fileUrl = manageService.uploadFile(file);
        }
        if (!fileUrl.isEmpty()){
            return ResultVO.success(fileUrl);
        }else {
            return ResultVO.error();
        }
    }

    /**
     * 查询任务信息
     * @param map
     * @return
     */
    @RequestMapping(value = "/showAll" ,method = RequestMethod.POST)
    public ResultVO showMyManageList(@RequestBody Map<String,String> map) throws UnsupportedEncodingException {
        ResultVO data = manageService.showMyManageList(map);
        return data;
    }

    /**
     * 创建任务回复
     * @param param
     * @return
     */
    @RequestMapping(value = "/createFeedBack" ,method = RequestMethod.POST)
    public ResultVO CreateFeedBack(@RequestBody Map<String,String> param){
        ResultVO data = manageService.CreateFeedBack(param);
        return data;
    }

    /**
     * 查看学生信息（1参与/0未参与）
     * @param param
     * @return
     */
    @RequestMapping(value = "/showStudentList" ,method = RequestMethod.POST)
    public ResultVO showStudentList(@RequestBody Map<String,String> param){
        ResultVO data = manageService.showStudentList(param);
        return data;
    }

    /**
     * 查询反馈
     * @param param
     * @return
     */
    @RequestMapping(value = "/showFeedBackList" ,method = RequestMethod.POST)
    public ResultVO showFeedBackList(@RequestBody Map<String,String> param){
        ResultVO data = manageService.showFeedBackList(param);
        return data;
    }

    /**
     * 查询我的所有学生申请
     * @param tid
     * @return
     */
    @RequestMapping(value = "/getAllApply" ,method = RequestMethod.POST)
    public ResultVO getAllApply(String tid){
        return manageService.getAllApply(tid);
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/getApplyById/{id}" ,method = RequestMethod.GET)
    public ResultVO getApplyById(@PathVariable Integer id){
        ResultVO data = manageService.getApplyById(id);
        return data;
    }

    /**
     * 同意审批
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/acceptApply/{id}/{uid}" ,method = RequestMethod.GET)
    public ResultVO acceptApply(@PathVariable Integer id,@PathVariable Integer uid) throws Exception {
        ResultVO data = manageService.acceptApply(id);
        sendNotificationUtil.SendNoti(uid,"身份已经通过审批");
        return data;
    }
}
