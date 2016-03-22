package com.zjhcsoft.mobile.easyandroid;

import android.content.Context;

import com.zjhcsoft.mobile.easyandroid.store.MemoryCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;


/**
 * Created by finger on 16/3/17.
 */
public class EasyClient {

    /**
     * sigle instance
     */
    private volatile static EasyClient defaultInstance = null;

    protected OkHttpClient client = null;

    /**
     * @return 单例对象
     */
    public static EasyClient getDefault() {
        if (defaultInstance == null) {
            synchronized (EasyClient.class) {
                if (defaultInstance == null) {
                    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
                    //cookie enabled
                    okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
                    okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
                    defaultInstance.client = okHttpClientBuilder.build();
                }
            }
        }
        return defaultInstance;
    }


}
