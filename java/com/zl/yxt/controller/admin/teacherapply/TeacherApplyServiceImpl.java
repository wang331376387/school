package com.zl.yxt.controller.admin.teacherapply;

import com.zl.yxt.mapper.admin.AdminUserMapper;
import com.zl.yxt.mapper.admin.TeacherApplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TeacherApplyServiceImpl implements TeacherApplyService {

    @Autowired
    private TeacherApplyMapper teacherApplyMapper;

    @Override
    public List<Map<String, Object>> getAllTeacherApply(String sid) {
        List<Map<String,Object>> allTeacherApplys = teacherApplyMapper.getAllTeacherApply(sid);
        for (Map<String,Object> applys: allTeacherApplys){
            if (applys.containsKey("role") && applys.get("role").toString().equals("teacher")){
                List<String> cnames = teacherApplyMapper.getApplyClassName(applys.get("classid").toString().split(","));
                if (cnames.size()>=0){
                    StringBuilder names = new StringBuilder(cnames.get(0));
                    for (int i=1; i<cnames.size();i++){
                        names.append(",").append(cnames.get(i));
                    }
                    applys.put("cname",names.toString());
                }
            }
        }
        return allTeacherApplys;
    }
}
