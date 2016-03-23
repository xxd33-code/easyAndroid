package com.zjhcsoft.mobile.easyandroid;

import com.zjhcsoft.mobile.easyandroid.store.MemoryCookieStore;
import com.zjhcsoft.mobile.easyandroid.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by finger on 16/3/17.
 */
public class EasyClient {

    /**
     * sigle instance
     */
    private volatile static EasyClient defaultInstance = null;

    //服务器地址
    private String mUrl = "";

    protected OkHttpClient client = null;

    /**
     * 获取单例
     *
     * @return 单例对象
     */
    public static EasyClient getDefault() {
        if (defaultInstance == null) {
            synchronized (EasyClient.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EasyClient();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 初始化client
     *
     * @param connectTimeOut 连接超时时间
     * @param cTimeout       交互超时时间
     * @param url            服务器地址
     */
    public void init(String url, int connectTimeOut, int cTimeout) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(cTimeout, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(cTimeout, TimeUnit.MILLISECONDS);
        //cookie enabled
        okHttpClientBuilder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));
        client = okHttpClientBuilder.build();
        mUrl = url;
    }

    /**
     * 修改服务器地址
     *
     * @param url 服务器地址
     */
    public void url(String url) {
        mUrl = url;
    }

    public void doAction(final AbstractAction action) {
        if (action != null) {
            try {
                if (StringUtils.isEmpty(action.getAddress().getUrl()))
                    action.getAddress().setUrl(mUrl);
                Request req = action.build();
                Call call = client.newCall(req);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        action.message = "交互异常:" + e.getMessage();
                        EventBus.getDefault().post(action);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //成功交互处理
                        action.code = response.isSuccessful() ? 0 : -1;
                        action.message = response.message();
                        action.parse(response.body().string());
                        EventBus.getDefault().post(action);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                action.message = "交互异常:" + e.getMessage();
                EventBus.getDefault().post(action);
            }
        }
    }

}
