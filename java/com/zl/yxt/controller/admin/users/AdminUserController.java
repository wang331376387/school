package com.zl.yxt.controller.admin.users;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.pojo.Moods;
import com.zl.yxt.pojo.School;
import com.zl.yxt.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private InformationMapper informationMapper;

    /**
     * 查询所有用户
     * @param model
     * @param params
     * @return
     */
    @RequestMapping("/findUsers")
    public String findUsers(@RequestParam(defaultValue = "1") Integer pageIndex,
                            Model model,@RequestParam Map<String, String> params){
        //分页查询
        PageHelper.startPage(pageIndex, 3);
        List<Map<String,Object>> users = adminUserService.findUsers(params);
        PageInfo<Map<String,Object>> userPage = new PageInfo<>(users);
        List<School> school = informationMapper.getSchoolAndClass();
        model.addAttribute("schools",school);
        model.addAttribute("userPage",userPage);
        model.addAttribute("param",params);
        return "users_list";
    }

    /**
     * 根据学校编号查询班级
     * @param id
     * @return
     */
    @RequestMapping("/findSchools")
    @ResponseBody
    public List<Map<String,Object>> findSchools(Integer id){
        List<Map<String,Object>> classes = adminUserService.selectMyClasses(id);
        return classes;
    }

    /**
     * 清空认证
     * @param id
     * @return
     */
    @PostMapping("/ClearCertification")
    @ResponseBody
    public String ClearCertification(Integer id){
        adminUserService.ClearCertification(id);
        return "success";
    }

    /**
     * 检测电话号码是否已经存在
     * @param phone
     * @return
     */
    @PostMapping("/checkPhoneExists")
    @ResponseBody
    public String checkPhoneSubmit(String phone){
        int num = adminUserService.checkPhoneExists(phone);
        if (num>0){
            return "fail";
        }else {
            return "success";
        }
    }

    /**
     * 创建管理员
     * @param users
     */
    @PostMapping("/createRoot")
    public void createRoot(Users users){
        users.setRole("root");
        adminUserService.createRoot(users);
    }
}
