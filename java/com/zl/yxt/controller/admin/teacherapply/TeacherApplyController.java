package com.zl.yxt.controller.admin.teacherapply;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.pojo.School;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.ManageService;
import com.zl.yxt.utils.SendNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TeacherApplyController {

    @Autowired
    private TeacherApplyService teacherApplyService;

    @Autowired
    private ManageService manageService;

    @Autowired
    private InformationMapper informationMapper;

    @Autowired
    private SendNotificationUtil sendNotificationUtil;

    @RequestMapping("/getAllTeacherApply")
    private String getAllTeacherApply(@RequestParam(defaultValue = "1") Integer pageIndex, Model model, HttpServletRequest request, String sid){
        String id = request.getSession().getAttribute("loginUser").toString(); //获取登录人编号
        //分页查询
        PageHelper.startPage(pageIndex, 3);
        List<Map<String,Object>> teacherApplys = teacherApplyService.getAllTeacherApply(sid);
        PageInfo<Map<String,Object>> applyPage = new PageInfo<>(teacherApplys);
        List<School> school = informationMapper.getSchoolAndClass();
        model.addAttribute("schools",school);
        model.addAttribute("applyPage",applyPage);
        model.addAttribute("sid",sid);
        return "teacher_applylist";
    }

    /**
     * 同意审批
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/acceptApply/{id}/{uid}" ,method = RequestMethod.GET)
    @ResponseBody
    public int acceptApply(@PathVariable Integer id,@PathVariable Integer uid) throws Exception {
        ResultVO data = manageService.acceptApply(id);
        sendNotificationUtil.SendNoti(uid,"身份已经通过审批");
        return data.code;
    }
}
