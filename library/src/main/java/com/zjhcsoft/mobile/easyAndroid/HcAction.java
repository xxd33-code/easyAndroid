package com.zjhcsoft.mobile.easyAndroid;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by finger on 16/3/16.
 * 根据目前交互接口文档定义进行实现的请求类
 */
public class HcAction<T> extends AbstractAction<T> {
    /**
     * SYSID
     **/
    private static String SYSID = "HcMobile";

    /**
     * 签名Key
     **/
    private static String SIGNKEY = "fe473d9c-0cdb-43e2-9086-51145e7065c2";


    /**
     * @param addr 请求地址
     * @param req  请求对象
     */
    public HcAction(ActionAddress addr, Object req) {
        super(addr, req);
    }


    @Override
    protected HashMap<String, Object> build() {
        Gson gson = new Gson();
        if (request != null) {
            HcRequest req = new HcRequest(address.getService(), address.getMethod(), request);
            String json = gson.toJson(req);
            HashMap<String, Object> postData = new HashMap<String, Object>();
            try {
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                String timestamp = format.format(new Date());
                String sign = sign(SYSID, json, timestamp);
                postData.put("sysid", SYSID);
                postData.put("timestamp", timestamp);
                postData.put("sign", sign);
                return postData;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected T parse(String json) {
        if (json != null) {
            Gson gson = new Gson();
            HcResponse response = gson.fromJson(json, HcResponse.class);
            ParameterizedType p = (ParameterizedType) getClass().getGenericSuperclass();
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            return gson.fromJson(response.returnValue, p.getActualTypeArguments()[0]);
        }
        return null;
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
        private String returnValue = "";
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
