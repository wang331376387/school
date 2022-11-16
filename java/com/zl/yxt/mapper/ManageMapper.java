package com.zl.yxt.mapper;

import com.zl.yxt.mapper.sqlprovider.ManageSqlProvider;
import com.zl.yxt.pojo.Manages;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ManageMapper {

    //查询附件名称个数
    @Select("SELECT COUNT(id) total FROM manages WHERE file LIKE CONCAT(#{preOriginalFilename},'%',#{fixOriginalFilename})")
    Integer findFile(String preOriginalFilename, String fixOriginalFilename);

    //创建任务
    @Insert("INSERT INTO `manages` (`describe`, `mname`, `file`,`type`, `tid`, `life`,`starttime`,`overtime`) VALUES (#{describe}, #{mname}, #{file},#{type}, #{tid}, #{life},#{starttime},#{overtime})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void saveManage(Manages manages);

    //插入任务和班级中间数据
    @InsertProvider(type = ManageSqlProvider.class,method = "saveManageClass")
    void saveManageClass(@Param("classesvalue") String[] classesvalue,@Param("mid") Integer mid);

    //结束任务,0结束1执行
    @Update("UPDATE manages SET life=0 WHERE mname= #{jobName}")
    void OverManageByName(String jobName);

    //查询任务列表，教师
    @Select("SELECT * FROM manages WHERE tid=#{id} ORDER BY overtime DESC")
    List<Map<String, Object>> showMyManageList(String id);

    //按id查询任务信息
    @Select("SELECT * FROM manages WHERE id=#{mid}")
    List<Map<String, Object>> showManage(String mid);

    //查询学生的任务列表
    @Select("SELECT * FROM manages WHERE id in (SELECT mid FROM `manage-class` WHERE cid=(SELECT cid FROM users WHERE id=#{id})) ORDER BY overtime DESC")
    List<Map<String, Object>> showStudentList(Map<String, String> map);

    //查询我已经读过的
    @Select("SELECT mid FROM feedback WHERE myid=#{id}")
    List<Integer> showMyReads(String id);

    //创建回复
    @Insert("INSERT INTO feedback (message,mid,myid,`read`) VALUES (#{message},#{mid},#{myid},#{read})")
    void CreateFeedBack(Map<String, String> param);

    //查询我回复此任务的数量
    @Select("select count(1) from feedback where mid=#{mid} and myid=#{myid}")
    Integer selectFeedBack(Map<String, String> param);

    //更新任务回复
    @Update("UPDATE feedback SET message=#{message},mid=#{mid},myid=#{myid},`read`=#{read} WHERE mid=#{mid} AND myid=#{myid}")
    void updateFeedBack(Map<String, String> param);

    //查询加入
    @Select("SELECT * FROM users WHERE id in (SELECT myid FROM feedback WHERE mid=#{mid})")
    List<Map<String, Object>> showJoin(String mid);

    //查询未加入
    @Select("SELECT * FROM (SELECT * FROM users WHERE cid in (SELECT cid FROM `manage-class` WHERE mid=#{myid})) s WHERE s.id not in (SELECT f.myid FROM feedback f WHERE mid=#{myid})")
    List<Map<String, Object>> showNoJoin(String mid);

    //查询回馈列表
    @Select("SELECT u.name,f.message,f.id FROM users u RIGHT JOIN (SELECT * FROM feedback WHERE mid=#{mid} and message is not null and message!='') f ON f.myid=u.id ORDER BY f.id DESC")
    List<Map<String, Object>> showFeedBackList(String mid);

    //查看所有学生申请
    @Select("SELECT * FROM usersapply WHERE sid=(SELECT sid FROM users WHERE id=#{tid}) and isread='0' and role='student'")
    List<Map<String, Object>> getAllApply(String tid);

    //根据ID查询申请信息
    @Select("SELECT u.*,c.cname AS class FROM usersapply u LEFT JOIN class c ON u.cid=c.id WHERE u.id=#{id}")
    Map<String, Object> getApplyById(Integer id);

    //更新申请信息
    @Update("update usersapply set isread=1 where id=#{id}")
    void updateUserApply(Integer id);
}
