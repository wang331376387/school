package com.zl.yxt.mapper;

import com.zl.yxt.pojo.Comment;
import com.zl.yxt.pojo.Moods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface MoodMapper {

    //查询所有心情
    List<Moods> showAllMoods(@Param("mid") Integer mid);

    //根据登录ID查询喜欢心情编号
    Integer[] selectMoodIds(Integer id);

    //添加心情
    void insertMood(Moods mood);

    //保存图片路劲到数据库
    void insertMoodPic(@Param("mid") Integer mid,@Param("picture") String picture);

    //查询刚才保存的心情
    Integer selectInsertMood(@Param("uid") Integer uid,@Param("createtime") Date createtime);

    //保存点赞信息
    void saveLike(@Param("uid") String uid, @Param("mid") String mid);

    //点赞个数加一
    void likenum(Integer id);

    //删除点赞信息
    void deleteLike(@Param("uid") String uid, @Param("mid") String mid);

    //点赞个数减一
    void lesslikenum(Integer id);

    //添加评论
    void insertComment(Comment comment);

    //查询同名文件个数
    Integer findFile(@Param("preOriginalFilename") String preOriginalFilename,@Param("fixOriginalFilename") String fixOriginalFilename);

    @Select("SELECT id FROM `mood-like` WHERE uid = #{id} AND mid = #{mid}")
    Integer selectIsLike(@Param("mid") Integer mid,@Param("id") Integer id);
}
