package com.zl.yxt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.yxt.mapper.MoodMapper;
import com.zl.yxt.pojo.Comment;
import com.zl.yxt.pojo.Moods;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.moodService;
import com.zl.yxt.utils.AliyunOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class moodServiceImpl implements moodService {

    @Resource
    private MoodMapper moodMapper;

    @Value("${uploadpath}")
    public String filePath; //暂存区路径

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    /**
     * 查询一条心情
     * @param mid 心情编号
     * @param id 用户编号
     * @return
     */
    @Override
    public ResultVO showOneMoods(Integer mid,Integer id) {
        List<Moods> moods = moodMapper.showAllMoods(mid);
        Integer num = moodMapper.selectIsLike(mid,id);
        if (num != null){
            moods.get(0).setIsLike(1);
        }
        return ResultVO.success(moods.get(0));
    }

    //按时间倒叙查询所有心情
    @Override
    public ResultVO showAllMoods(Integer pageIndex,String id) {
        //分页查询
        PageHelper.startPage(pageIndex, 3);
        //关联查询心情和作者列表
        List<Moods> moods = moodMapper.showAllMoods(null);
        PageInfo<Moods> map = new PageInfo<>(moods);
        //根据登录ID查询喜欢心情编号
        Integer[] moodIds = moodMapper.selectMoodIds(Integer.valueOf(id));
        //根据喜欢心情编号标记心情
        for (int i=0; i < moodIds.length; i++){
            for (Moods mood : map.getList()){
                if (mood.getId() == moodIds[i] ){
                    mood.setIsLike(1);
                    continue;
                }
            }
        }
        ResultVO data = ResultVO.success(map);
        return data;
    }

    /**
     * 创建心情
     * @param mood
     * @return
     */
    @Override
    public ResultVO creatMood(Moods mood) throws Exception {
        Calendar calendar = Calendar.getInstance();
        // 将毫秒数设为0
        calendar.set(Calendar.MILLISECOND,0);
        Date nowDate = calendar.getTime(); //获取当前时间
        mood.setCreatetime(nowDate); //设置创建时间
        mood.setLikenum(0); //初始化喜欢个数
        //保存心情
        moodMapper.insertMood(mood);
        //根据创建时间和创建人编号查询刚才保存的心情编号
        Integer mid = moodMapper.selectInsertMood(mood.getUid(),mood.getCreatetime());
        System.err.println("心情编号："+mid);
        try{
            uploadPic(mid,mood.getUid().toString()); //上传图片
        }catch (Exception e){
            throw new RuntimeException("调用上传图片接口异常"); //事务回滚
        }
        return ResultVO.customize(20000,null,"上传成功");
    }

    /**
     * 点赞或者取消
     * @param uid
     * @param mid
     * @param status
     * @return
     */
    @Override
    public ResultVO tolike(String uid, String mid,Integer status) {
        if (status == 1){
            //第一步：保存点赞信息
            moodMapper.saveLike(uid,mid);
            //第二步：心情点赞数加一
            moodMapper.likenum(Integer.valueOf(mid));
        }else {
            //第一步：删除点赞信息
            moodMapper.deleteLike(uid,mid);
            //第二步：心情点赞数减一
            moodMapper.lesslikenum(Integer.valueOf(mid));
        }
        return ResultVO.success();
    }

    /*添加评论*/
    @Override
    public void insertComment(Comment comment) {
        Calendar calendar = Calendar.getInstance();
        // 将毫秒数设为0
        calendar.set(Calendar.MILLISECOND,0);
        Date nowDate = calendar.getTime(); //获取当前时间
        comment.setComtime(nowDate);
        moodMapper.insertComment(comment);
    }

    /**
     * 读取暂存区图片，循环上传
     * @param mid
     * @throws Exception
     */
    void uploadPic(Integer mid,String uid) throws Exception {
        File file = new File(filePath + File.separator + uid);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            list.add(file);
            File[] files;
            while (!list.isEmpty()){
                files = list.removeFirst().listFiles();
                for(File filePic : files){
                    String picture = aliyunOSSUtil.upload(filePic,"mood/"); //上传OSS的图片路径
                    if (picture != null){
                        //保存图片路劲到数据库
                        moodMapper.insertMoodPic(mid,picture);
                    }
                }
            }
        }
    }
}
