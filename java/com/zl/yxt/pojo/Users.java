package com.zl.yxt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description="用户主体")
public class Users {
    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(type = IdType.AUTO)
    public Integer id;

    @ApiModelProperty(value = "学校编号", hidden = true)
    public String sid; // 学校编号

    @ApiModelProperty(value = "班级编号", hidden = true)
    public String cid; //班级编号

    @ApiModelProperty(value = "昵称", hidden = true)
    public String username;

    @ApiModelProperty(value = "密码", name = "password", required = true)
    public String password;

    @ApiModelProperty(value = "姓名", hidden = true)
    public String name;

    @ApiModelProperty(value = "电话，登录号", name = "phone", required = true)
    public String phone;

    @ApiModelProperty(value = "照片路径", hidden = true)
    public String photo;

    @ApiModelProperty(value = "角色", hidden = true)
    public String role; // 角色

    @ApiModelProperty(value = "最近登录时间", hidden = true)
    public Date recentlogintime; //最近登录时间

    @ApiModelProperty(value = "学校名称", hidden = true)
    @TableField(exist = false)
    public String scname; //学校名称

    @ApiModelProperty(value = "班级名称", hidden = true)
    @TableField(exist = false)
    public String cname; //班级名称

    @ApiModelProperty(value = "验证码", hidden = true)
    @TableField(exist = false)
    public String code; //验证码

    @ApiModelProperty(value = "性别", hidden = true)
    @TableField(exist = false)
    public String sex; //性别
}
