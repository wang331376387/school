package com.zl.yxt.controller.admin.teacherapply;

import java.util.List;
import java.util.Map;

public interface TeacherApplyService {
    //查询所有教师申请信息
    List<Map<String, Object>> getAllTeacherApply(String sid);
}
