package com.zl.yxt.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class School { //为了适配级联选择做的改变

    public String value; //对应学校ID

    public String text; //对应学校名称

    public List<Classes> children; //班级

}
