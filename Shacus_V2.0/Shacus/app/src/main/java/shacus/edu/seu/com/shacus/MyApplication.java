package shacus.edu.seu.com.shacus;

/**
 * 可以利用getInstance直接获取Application全局对象
 * 以及getContext获取Application的context对象
 * Created by Mind on 2017/9/2.
 */

import android.app.Application;
import android.content.Context;

import java.io.File;

import shacus.edu.seu.com.shacus.Network.okHttpUtil;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        File cachefile=getCacheDir();
        if(okHttpUtil.instance==null)
            okHttpUtil.instance=new okHttpUtil(cachefile);
    }
    /**
     * 获取Context
     * @return 返回Context的对象
     */
    public static Context getContext(){
        return instance.getApplicationContext();
    }
}