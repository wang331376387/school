package com.zl.yxt.service;

import com.zl.yxt.pojo.Comment;
import com.zl.yxt.pojo.Moods;
import com.zl.yxt.pojo.vo.ResultVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface moodService {

    //按时间倒叙查询所有心情
    public ResultVO showAllMoods(Integer pageIndex,String id);

    //创建心情
    @Transactional
    ResultVO creatMood(Moods mood) throws Exception;

    //点赞或者取消
    @Transactional
    ResultVO tolike(String uid, String mid,Integer status);

    //添加评论
    void insertComment(Comment comment);

    //查询一条心情
    ResultVO showOneMoods(Integer mid,Integer id);
}
