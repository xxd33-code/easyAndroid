package com.zjhcsoft.mobile.easyandroid;


import org.json.JSONException;

import okhttp3.Request;

/**
 * Created by finger on 16/3/16.
 * 抽象请求类
 */
public abstract class AbstractAction {
    /**
     * 网络和业务上均正常响应
     */
    public static final int OK = 0;

    /**
     * 网络造成的异常响应
     */
    public static final int ERROR = -1;

    /**
     * 交易未开始执行
     */
    public static final int NSR = -2;

    /**
     * 响应类的类型
     */
    protected final Class<?> mResponseCls;

    /**
     * 请求地址
     */
    protected ActionAddress address = null;

    /**
     * 请求对象
     */
    protected Object request = null;

    /**
     * 响应对象
     */
    protected Object response = null;

    /**
     * 网络异常时返回ERROR,业务正常时返回OK,网络正常但业务错误返回>0
     */
    public int code = NSR;

    /**
     * 错误信息
     */
    public String message = "交互尚未请求或其他异常";

    /**
     * @param addr        请求地址
     * @param req         请求对象
     * @param responseCls 响应类型
     */
    protected AbstractAction(ActionAddress addr, Object req, Class<?> responseCls) {
        address = addr;
        request = req;
        mResponseCls = responseCls;
    }

    /**
     * @return 交易地址
     */
    public ActionAddress getAddress() {
        return address;
    }

    /**
     * @return 响应是否成功
     */
    public boolean isSuccessful() {
        return code >= 0;
    }

    /**
     * 交互返回的响应对象
     *
     * @return 响应对象
     */
    public Object getResponse() {
        return response;
    }

    /**
     * 构建请求
     */
    protected abstract Request build();

    /**
     * @param json 返回的json数据
     * @return 响应Bean
     */
    protected abstract void parse(String json);
}