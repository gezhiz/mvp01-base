package com.mvp01.common.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 手机号短信验证网关
 * Created by zhangyueping on 15/4/16.
 */
public class SMS2Util implements Runnable {
    private String SMSCode;
    private String mobilePhone;
    private String SMSDesc; //短信描述

    public static final String SMS_DESC_POINT = "【查找优惠】恭喜您获得%s积分，开始下载体验吧！http://app.021-sdeals.com";

    public String getSMSDesc() {
        return SMSDesc;
    }

    public void setSMSDesc(String SMSDesc) {
        this.SMSDesc = SMSDesc;
    }

    public String getSMSCode() {
        return SMSCode;
    }

    public void setSMSCode(String SMSCode) {
        this.SMSCode = SMSCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public void run() {

        if (StringUtils.isNotBlank(mobilePhone)) {

            String mobiles = mobilePhone;//"13800210021,13800138000";//手机号码，多个号码使用","分割(一次支持5w个手机号码)
            String content = "";//String.format("您的验证码是：%s，请尽快完成验证。如非本人操作，可不用理会！", SMSCode);//短信内容

            if (!StringUtils.isBlank(SMSCode)) {
                if (StringUtils.isBlank(getSMSDesc())) {
                    content = String.format("【查找优惠】您的验证码是：%s，请尽快完成验证。如非本人操作，可不用理会！", SMSCode);
                } else {
                    content = String.format(getSMSDesc(), SMSCode);
                }
            } else {
                if (StringUtils.isBlank(getSMSDesc())) {
                    return;
                } else {
                    content = getSMSDesc();
                }
            }

            ChuanglanSMS client = new ChuanglanSMS();

            CloseableHttpResponse response = null;
            try {
                //发送短信
                response = client.sendMessage(mobiles,content);
                if(response != null && response.getStatusLine().getStatusCode()==200){
                    System.out.println(EntityUtils.toString(response.getEntity()));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.close();


        } else {
        }

    }

    public void send() {
        ThreadExecutor.exec(this);
    }

//    public static void main(String[] args){
//
//        SMS2Util sms2Util = new SMS2Util();
//        sms2Util.setMobilePhone("18601361979");
//        sms2Util.setSMSCode("6666");
//        sms2Util.send();
//
//    }

}
