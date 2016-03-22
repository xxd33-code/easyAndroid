package com.zjhcsoft.mobile.easyandroid;

/**
 * Created by finger on 16/3/14.
 * 请求服务器的交互地址
 */
public class ActionAddress {
    //服务器域名或地址
    private String url = "127.0.0.1";
    //服务名
    private String service = "";
    //方法名
    private String method = "";

    /**
     * 构造方法
     */
    public ActionAddress() {
    }

    /**
     * 构造方法
     *
     * @param service 服务名
     * @param method  方法名
     */
    public ActionAddress(String service, String method) {
        this.service = service;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionAddress that = (ActionAddress) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (service != null ? !service.equals(that.service) : that.service != null) return false;
        return !(method != null ? !method.equals(that.method) : that.method != null);

    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (service != null ? service.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }
}