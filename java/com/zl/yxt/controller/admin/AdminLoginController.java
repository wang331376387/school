package com.zl.yxt.controller.admin;

import com.zl.yxt.controller.admin.users.AdminUserService;
import com.zl.yxt.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private AdminUserService adminUserService;

    //首页
    @GetMapping
    public String gotoAdminLogin(){
        return "login";
    }

    /**
     * 登录
     * @param model
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String AdminLogin(Model model,String username, String password, HttpSession session){
        Users users = adminUserService.findAdmin(username,password);
        if (users!=null){
            session.setAttribute("loginUser",users.getId());
            return "index";
        }else {
            model.addAttribute("msg","账号或密码错误");
            return "login";
        }

    }

    /**
     * 注销
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String AdminLogout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");
        return "login";
    }

    @RequestMapping(value = "/introduce",method = RequestMethod.GET)
    public String GoToIntroduce(){
        return "introduce";
    }

    @RequestMapping(value = "/users_list",method = RequestMethod.GET)
    public String GoToHouse_list(){
        return "users_list";
    }

    @RequestMapping(value = "/user_edit",method = RequestMethod.GET)
    public String GoToUser_edit(String id){
        return "user_edit";
    }
}
