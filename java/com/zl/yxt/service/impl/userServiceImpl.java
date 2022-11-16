package com.zl.yxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zl.yxt.mapper.UserMapper;
import com.zl.yxt.pojo.Users;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.userService;
import com.zl.yxt.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class userServiceImpl extends ServiceImpl<UserMapper, Users> implements userService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResultVO login(Users users) {
        QueryWrapper<Users> wrapper = Wrappers.query();
        wrapper.eq("phone", users.getPhone())
                .eq("password", users.getPassword());
        Users hasUsers = userMapper.selectOne(wrapper);
        if (hasUsers != null && users.password.equals(hasUsers.getPassword())){
            //生成token
            String token = TokenUtil.sign(hasUsers);
            //将token存入redis,存活时间10小时
            redisTemplate.opsForValue().set(users.getPhone()+"_token",token,60*60*10, TimeUnit.SECONDS);
            //更新最近登录时间
            hasUsers.setRecentlogintime(new Date());
            userMapper.updateById(hasUsers);
            return ResultVO.success(hasUsers,token);
        }
        return ResultVO.error();
    }

    @Override
    public Users getInformation(Integer id) {
        Users user = userMapper.getInformation(id);
        String myrole = user.getRole();
        if (myrole!=null && myrole.equals("teacher")){ //教师可能存在多个班级
            String cid[] = userMapper.getClassIds(user.getId());
            if (cid.length>0){
                StringBuilder classid = new StringBuilder(cid[0]);
                for (int i=1;i<cid.length;i++){
                    classid.append(",").append(cid[i]); //将多个班级以逗号分隔
                }
                user.setCid(classid.toString());
            }
        }
        return user;
    }

    @Override
    public void register(Users user) {
        user.setPhoto("https://zllimg.oss-cn-hangzhou.aliyuncs.com/touxiang/nobody.jpg"); //初始化头像
        userMapper.insert(user);
    }
}
