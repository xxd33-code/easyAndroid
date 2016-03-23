package com.zjhcsoft.mobile.easyandroid;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.net.FileNameMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Created by finger on 16/3/16.
 * 根据目前HC公司交互接口文档定义进行实现的请求类
 */
public class HcAction extends AbstractAction {
    /**
     * 无返回数据
     */
    public static final int UNKNOW_ERROR = 1;

    /**
     * SYSID
     **/
    private static String SYSID = "HcMobile";

    /**
     * 签名Key
     **/
    private static String SIGNKEY = "fe473d9c-0cdb-43e2-9086-51145e7065c2";

    /**
     * 构造函数
     *
     * @param addr        请求地址
     * @param req         请求对象
     * @param responseCls 响应类型
     */
    public HcAction(ActionAddress addr, Object req, Class<?> responseCls) {
        super(addr, req, responseCls);
    }

    /**
     * 构造函数
     *
     * @param service     服务名
     * @param method      方法名
     * @param req         请求对象
     * @param responseCls 响应类型
     */
    public HcAction(String service, String method, Object req, Class<?> responseCls) {
        this(new ActionAddress(service, method), req, responseCls);
    }

    @Override
    protected Request build() {
        Gson gson = new Gson();
        if (request != null) {
            HcRequest req = new HcRequest(address.getService(), address.getMethod(), request);
            String reqJson = gson.toJson(req);
            FormBody.Builder formBody = new FormBody.Builder();
            try {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String timestamp = format.format(new Date());
                String sign = sign(SYSID, reqJson, timestamp);
                formBody.addEncoded("sysid", SYSID);
                formBody.addEncoded("timestamp", timestamp);
                formBody.addEncoded("sign", sign);
                formBody.addEncoded("v", "1.0");
                formBody.addEncoded("req", reqJson);
                return new Request.Builder().url(address.getUrl()).post(formBody.build()).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void parse(String json) {
        if (json != null) {
            Gson gson = new Gson();
            HcResponse hcResponse = gson.fromJson(json, HcResponse.class);
            if (HcResponse.STATUS_OK.equals(hcResponse.status)) {
                code = OK;
            } else {
                code = UNKNOW_ERROR;
            }
            message = hcResponse.message;
            try {
                JSONObject obj = new JSONObject(json);
                response = gson.fromJson(obj.getString("returnValue"), mResponseCls);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成签名
     *
     * @param sysid     服务端分配的ID
     * @param req       请求json
     * @param timestamp 时间戳
     * @return
     * @throws Exception
     */
    private String sign(String sysid, String req, String timestamp)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(SIGNKEY).append(sysid).append(req).append(timestamp)
                .append(SIGNKEY);
        java.security.MessageDigest alga;
        try {
            alga = java.security.MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return null;
        }
        alga.update(sb.toString().getBytes("utf-8"));
        byte[] digesta = alga.digest();
        return byte2hex(digesta);
    }

    /**
     * 字节码转换成16进制字符串
     *
     * @param bytes
     * @return
     */
    protected String byte2hex(byte bytes[]) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < bytes.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
                    .substring(1).toUpperCase());
        }
        return retString.toString();
    }

    /**
     * 将16进制字符串转换成字节码
     *
     * @param hex
     * @return
     */
    protected byte[] hex2byte(String hex) {
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
                    16);
        }
        return bts;
    }

    /**
     * 响应Bean
     */
    protected class HcResponse {
        /**
         * 交易成功返回值
         */
        public static final String STATUS_OK = "OK";

        /**
         * 交易失败返回值
         */
        public static final String STATUS_ERROR = "error";

        /**
         * 交易状态
         */
        private String status = "";

        /**
         * 错误码
         */
        private String errorCode = "";

        /**
         * 错误信息
         */
        private String message = "";

        /**
         * 返回值
         */
        private JSONObject returnValue = null;
    }

    /**
     * 请求bean
     */
    protected class HcRequest {
        /**
         * 服务名
         */
        private String service = "";

        /**
         * 方法名
         */
        private String method = "";

        /**
         * 请求参数
         */
        private Object args = null;

        /**
         * @param service 服务名
         * @param method  方法名
         * @param args    请求参数
         */
        public HcRequest(String service, String method, Object args) {
            this.service = service;
            this.method = method;
            this.args = args;
        }
    }
}
