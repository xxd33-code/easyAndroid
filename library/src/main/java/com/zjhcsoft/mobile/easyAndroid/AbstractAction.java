package com.zjhcsoft.mobile.easyAndroid;


import java.util.HashMap;

/**
 * Created by finger on 16/3/16.
 * 抽象请求类
 */
public abstract class AbstractAction<T> {
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
     * 请求地址
     */
    protected ActionAddress address = null;

    /**
     * 请求对象
     */
    protected Object request = null;

    /**
     * 网络异常时返回ERROR,业务正常时返回OK,网络正常但业务错误返回>0
     */
    public int code = NSR;

    /**
     * 错误信息
     */
    public String message = "交互尚未请求";

    /**
     * @param addr      请求地址
     * @param req       请求对象
     */
    protected AbstractAction(ActionAddress addr, Object req) {
        address = addr;
        request = req;
    }

    /**
     * @return  交易地址
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
     * 构建请求
     */
    protected abstract HashMap<String, Object> build();

    /**
     * @param json 返回的json数据
     * @return 响应Bean
     */
    protected abstract T parse(String json);
}