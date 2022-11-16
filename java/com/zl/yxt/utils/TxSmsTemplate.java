package com.zl.yxt.utils;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 腾讯云发送短信模板对象,封装了发送短信的api
 */
@Slf4j
@Component
public class TxSmsTemplate {

    // AppId  1400开头的
    @Value("${txsms.AppId}")
    private int AppId;
    // 短信应用SDK AppKey
    @Value("${txsms.AppKey}")
    private String AppKey;
    // 短信模板ID
    @Value("${txsms.TemplateId}")
    private int TemplateId;
    // 签名
    @Value("${txsms.signName}")
    private String signName;

    /**
     * 指定正文模板id发送短信
     * @param number 用户的手机号码
     * @return OK 成功  null 失败
     */
    public String sendMesModel(String number,String code) {
        try {
            // 接收生成的验证码，设置5分钟内填写
            String[] params = {code};

            // 构建短信发送器
            SmsSingleSender ssender = new SmsSingleSender(AppId, AppKey);

            SmsSingleSenderResult result = ssender.sendWithParam("86", number,
                    TemplateId, params, signName, "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信

            return result.errMsg; //OK
        } catch (HTTPException e) {
            // HTTP响应码错误
            log.info("短信发送失败,HTTP响应码错误!");
            // e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            log.info("短信发送失败,json解析错误!");
            //e.printStackTrace();
        } catch (IOException | org.json.JSONException e) {
            // 网络IO错误
            log.info("短信发送失败,网络IO错误!");
            // e.printStackTrace();
        }
        return null;
    }
}
