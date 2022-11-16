package com.zl.yxt.controller.admin.schoolAndClass;

import com.alibaba.fastjson.JSONArray;
import com.zl.yxt.mapper.InformationMapper;
import com.zl.yxt.pojo.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class SchoolAndClassController {

    @Autowired
    private SchoolAndClassService schoolAndClassService;

    @Autowired
    private InformationMapper informationMapper;

    @RequestMapping("/getAllSchoolAndClass")
    private String getAll(Model model,String sid){
        //查询所有学校
        List<Map<String,Object>> schools = schoolAndClassService.getSchools();
        //查询所有班级
        List<Map<String,Object>> classes = schoolAndClassService.getClasses();

        List<Map<String,Object>> datas = new ArrayList<>();
        Map<String,Object> first = new HashMap<>();
        first.put("checked",false);
        first.put("crospID",0);
        first.put("delFlag",1);
        first.put("parentID",69800);//首个，随便填的数字
        first.put("weiduGrade",0);
        first.put("weiduID",0);
        first.put("weiduName","学校列表");
        datas.add(first);
        for (Map<String,Object> school:schools){
            Map<String,Object> ztree = new HashMap<>();
            ztree.put("checked",false);
            ztree.put("crospID",0);
            ztree.put("delFlag",1);
            ztree.put("parentID",0);
            ztree.put("weiduGrade",1);
            ztree.put("weiduID",school.get("id"));
            ztree.put("weiduName",school.get("scname"));
            datas.add(ztree);
        }
        for (Map<String,Object> classd : classes){
            Map<String,Object> ztree = new HashMap<>();
            ztree.put("checked",false);
            ztree.put("crospID",0);
            ztree.put("delFlag",1);
            ztree.put("parentID",classd.get("sid"));
//            ztree.put("weiduGrade",2);
//            ztree.put("weiduID",classd.get("id"));
            ztree.put("weiduName",classd.get("cname"));
            datas.add(ztree);
        }
        model.addAttribute("trees", JSONArray.toJSONString(datas));
        if (sid != null && sid != "") {
            String scname = schoolAndClassService.getSchoolById(sid);
            model.addAttribute("sid",sid);
            model.addAttribute("scname",scname);
        }
        List<Map<String,Object>> allclasses = schoolAndClassService.getClassesBySid(sid);
        model.addAttribute("classes",allclasses);
        return "schoolAndClass";
    }

    @RequestMapping("/deleteClassById/{id}")
    @ResponseBody
    public String deleteClassById(@PathVariable String id, Model model){
        schoolAndClassService.deleteClassById(id);
        return "success";
    }

    @RequestMapping("/school_add")
    public String GotoSchoolAdd(){
       return "school_add";
    }

    @RequestMapping("/class_add")
    public String GotoClassAdd(Model model){
        List<School> school = informationMapper.getSchoolAndClass();
        model.addAttribute("schools",school);
        return "class_add";
    }

    @RequestMapping("/saveClass")
    @ResponseBody
    public String SaveClass(String sid,String cname){
        schoolAndClassService.SaveClass(sid,cname);
        return "success";
    }

    @RequestMapping("/saveSchool")
    @ResponseBody
    public String SaveSchool(String scname){
        schoolAndClassService.SaveSchool(scname);
        return "success";
    }
}
