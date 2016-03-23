package com.zjhcsoft.mobile.easyandroid.utils;

/**
 * 字符串工具类
 * Created by finger on 16/3/22.
 */
public class StringUtils {

    /**
     * 检查字符串是否为空
     *
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
}
