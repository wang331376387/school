package com.zl.yxt.controller;

import com.zl.yxt.pojo.Users;
import com.zl.yxt.pojo.vo.ResultVO;
import com.zl.yxt.service.userService;
import com.zl.yxt.utils.TxSmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(tags = "登录管理")
public class userController {

    @Autowired
    private userService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TxSmsTemplate txSmsTemplate;

    /**
     * 验证登录
     * @param users
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVO login(@RequestBody Users users){

        ResultVO data = userService.login(users);

        return data;
    }

    /**
     * 获取个人信息
     * @return
     */
    @ApiOperation(value = "获取个人信息")
    @RequestMapping(value = "/getInformation/{id}",method = RequestMethod.GET)
    public ResultVO getInformation(@PathVariable("id")Integer id){
        Users users = userService.getInformation(id);
        return ResultVO.success(users);
    }

    /**
     * 生成验证码
     * @return
     */
    public String getCode(){
        String str="0123456789";
        String uuid=new String();
        for(int i=0;i<4;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            uuid+=ch;
        }
        return uuid;
    }
    /**
     * 发送验证码
     * @return
     */
    @RequestMapping(value = "/register/createCode/{phone}",method = RequestMethod.GET)
    public ResultVO createCode(@PathVariable("phone") String phone){
        String code = getCode();
        log.info("========================>"+code);
        //将验证码保存redis，key为电话号，有效期180s
        redisTemplate.opsForValue().set(phone,code,180, TimeUnit.SECONDS);
        //发送短信，腾讯云短信国企，暂时注释
//        String Msg = txSmsTemplate.sendMesModel(phone, code);
        return ResultVO.success();
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResultVO register(@RequestBody Users user){
        //获取redis中存的验证码
        String redisCode = redisTemplate.opsForValue().get(user.getPhone());
        if (redisCode.equals(user.getCode())){
            user.setRecentlogintime(new Date());
            //添加此用户信息
            userService.register(user);
            return ResultVO.success();
        }else {
            return ResultVO.customize(2002,null,"验证码错误");
        }
    }
}
