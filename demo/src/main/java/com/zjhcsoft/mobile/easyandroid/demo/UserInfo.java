package com.zjhcsoft.mobile.easyandroid.demo;

/**
 * Created by finger on 16/3/23.
 */
public class UserInfo {
    private String loginname = "";
    private String passwd = "";
    private long userid = 0l;

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }
}
