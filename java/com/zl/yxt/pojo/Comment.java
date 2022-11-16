package com.zl.yxt.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    public Integer id;
    public Integer mid;
    public Integer uid;
    public Integer cid;
    public Date comtime;
    public String comment;
    public String username; //评论人昵称
    public String cname; // 被评论人
}
