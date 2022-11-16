package com.zl.yxt.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zl.yxt.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0
 * @author:**
 * @date:2021/12/2
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final Logger log = LoggerFactory.getLogger(TokenInterceptor.class.getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String basePath = scheme + "://" + serverName + ":" + port;

        StringBuffer url = request.getRequestURL();

        if (url.indexOf(basePath+"/admin")==0){ //以admin开头证明后台的请求
            //后台请求检查session
            Object user = request.getSession().getAttribute("loginUser");
            if(user == null){
                //未登陆，返回登陆页面
                request.setAttribute("msg","没有权限请先登陆");
                request.getRequestDispatcher("/admin").forward(request,response);
                return false;
            }else{
                //已登录，放行请求
                return true;
            }
        }else { //移动端的请求
            if(request.getMethod().equals("OPTIONS")){
                response.setStatus(HttpServletResponse.SC_OK);
                return true;
            }
            response.setCharacterEncoding("utf-8");
            String token = request.getHeader("token");
            String username = request.getHeader("username"); //此处为登录号即电话号
            //获取redis中存的token
            String redisToken = redisTemplate.opsForValue().get(username+"_token");
            if(token != null && redisToken != null && token.equals(redisToken)){
                //验证token合法性
                boolean result = TokenUtil.verify(redisToken,username);
                if(result){
//                log.info("===============>通过拦截器");
                    return true;
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            try{
                JSONObject json = new JSONObject();
                json.put("msg","登录过期,或者账号异地登录");
                json.put("code","50000");
                response.getWriter().append(json.toJSONString());
                log.info("===============>认证失败，未通过拦截器");
            }catch (Exception e){
                e.printStackTrace();
                response.sendError(500);
                return false;
            }
        }

        return false;
    }

    //不使用了，放行静态资源在mvc配置
    public boolean isStatic(StringBuffer url) {

        boolean result = false;
        String[] arr = { //定义一个需要放行的数组
                "/.css",
                "/.ico",
                "/.jpg",//图片
                "/.js" //js脚本
        };
        for (String a : arr) { //放行静态资源，注意路径

            if (url.indexOf(a) != -1) { //-1代表没有，0第一个
                result = true;
            }
        }
        return result;
    }
}
