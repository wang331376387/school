package com.zl.yxt.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Builder
@ToString
@Data
public class ResultVO {
    /**
     * 响应码
     */
    public int code;
    /**
     * 返回的数据
     */
    public Object data;

    /**
     * 消息
     */
    public String msg;

    public String token;

    /*失败*/
    public static ResultVO error() {
        return ResultVO.builder().code(20001).data(false).build();
    }

    public static ResultVO error(Object data) {
        return ResultVO.builder().code(20001).data(data).build();
    }

    /*成功*/
    public static ResultVO success() {
        return ResultVO.builder().code(20000).data(true).build();
    }

    public static ResultVO success(Object data) {
        return ResultVO.builder().code(20000).data(data).build();
    }
    //成功带数据和令牌
    public static ResultVO success(Object data,String token) {
        return ResultVO.builder().code(20000).data(data).token(token).build();
    }
    //自定义
    public static ResultVO customize(int code,Object data,String msg) {
        return ResultVO.builder().code(code).data(data).msg(msg).build();
    }
}
