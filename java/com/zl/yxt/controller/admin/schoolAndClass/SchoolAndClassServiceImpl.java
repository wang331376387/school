package com.zl.yxt.controller.admin.schoolAndClass;

import com.zl.yxt.mapper.admin.SchoolAndClassMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SchoolAndClassServiceImpl implements SchoolAndClassService {

    @Resource
    private SchoolAndClassMapper schoolAndClassMapper;

    @Override
    public List<Map<String, Object>> getSchools() {
        return schoolAndClassMapper.getSchools();
    }

    @Override
    public List<Map<String, Object>> getClasses() {
        return schoolAndClassMapper.getClasses();
    }

    @Override
    public List<Map<String, Object>> getClassesBySid(String sid) {
        return schoolAndClassMapper.getClassesBySid(sid);
    }

    @Override
    public void deleteClassById(String id) {
        schoolAndClassMapper.deleteClassById(id);
    }

    @Override
    public void SaveClass(String sid, String cname) {
        schoolAndClassMapper.SaveClass(sid,cname);
    }

    @Override
    public void SaveSchool(String scname) {
        schoolAndClassMapper.SaveSchool(scname);
    }

    @Override
    public String getSchoolById(String sid) {
        return schoolAndClassMapper.getSchoolById(sid);
    }
}
