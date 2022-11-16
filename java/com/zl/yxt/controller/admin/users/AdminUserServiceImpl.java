package com.zl.yxt.controller.admin.users;

import com.zl.yxt.mapper.admin.AdminUserMapper;
import com.zl.yxt.pojo.Users;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public List<Map<String, Object>> findUsers(Map<String, String> params) {
        List<Map<String,Object>> users = adminUserMapper.findUsers(params);
        for (Map<String,Object> user: users){
            if (user.containsKey("role") && user.get("role").toString().equals("teacher")){
                List<String> cnames = adminUserMapper.findCnameByTid(user.get("id").toString());
                if (cnames.size()>=0){
                    StringBuilder names = new StringBuilder(cnames.get(0));
                    for (int i=1; i<cnames.size();i++){
                        names.append(",").append(cnames.get(i));
                    }
                    user.put("cname",names.toString());
                }
            }
        }
        return users;
    }

    //查询学校对应班级信息
    @Override
    public List<Map<String, Object>> selectMyClasses(Integer id) {

        List<Map<String,Object>> classes = adminUserMapper.selectMyClasses(id);
        return classes;
    }

    //清空认证
    @Override
    public void ClearCertification(Integer id) {
        adminUserMapper.ClearCertification(id);
    }

    //电话号码存在数量
    @Override
    public int checkPhoneExists(String phone) {
        return adminUserMapper.checkPhoneExists(phone);
    }

    //创建管理员
    @Override
    public void createRoot(Users users) {
        adminUserMapper.createRoot(users);
    }

    //查询管理员
    @Override
    public Users findAdmin(String username, String password) {
        return adminUserMapper.findAdmin(username,password);
    }
}
