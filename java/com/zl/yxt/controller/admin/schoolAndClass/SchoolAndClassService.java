package com.zl.yxt.controller.admin.schoolAndClass;

import java.util.List;
import java.util.Map;

public interface SchoolAndClassService {
    List<Map<String, Object>> getSchools();

    List<Map<String, Object>> getClasses();

    List<Map<String, Object>> getClassesBySid(String sid);

    void deleteClassById(String id);

    void SaveClass(String sid, String cname);

    void SaveSchool(String scname);

    String getSchoolById(String sid);
}
