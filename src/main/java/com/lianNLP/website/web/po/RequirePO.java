package com.lianNLP.website.web.po;

/**
 * Created by haolen on 2018/11/20.
 */
public class RequirePO {

    private String require;
    private String nickName;
    private String phone;
    private String weixin;

    public RequirePO() {

    }

    public RequirePO(String require, String nickName, String phone, String weixin) {
        this.require = require;
        this.nickName = nickName;
        this.phone = phone;
        this.weixin = weixin;
    }

    public String getRequire() {
        return require;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
