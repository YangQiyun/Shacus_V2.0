package shacus.edu.seu.com.shacus.Network;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Mind on 2017/9/1.
 */

public class okHttpUtil {
    public static okHttpUtil_BitmapResponse callBacknei;
    public static okHttpUtil_JsonResponse jsonResponse;
    public static okHttpUtil instance;
    private static File cacheDirectory;
    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient.Builder mOkHttpClientBuilder;
    private static final String TAG = "okHttpUtil";


    static {
        mOkHttpClient=new OkHttpClient();
        mOkHttpClientBuilder=mOkHttpClient.newBuilder();
        mOkHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        mOkHttpClientBuilder.writeTimeout(10, TimeUnit.SECONDS);
        mOkHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        mOkHttpClientBuilder.followRedirects(true);
    }

    public okHttpUtil(File cacheDirectory){
        this.cacheDirectory=cacheDirectory;
        init();
    }

    private static void init() {
        //设置cache
        try {
            int cacheSize=10*1024*1024; //10 MiB
            String okhttpCachePath=cacheDirectory.getPath()+File.separator+"okhttp";
            File okhttpCache=new File(okhttpCachePath);
            if(!okhttpCache.exists()){
                okhttpCache.mkdirs();
            }
            Cache cache = new Cache(okhttpCache, cacheSize);
            mOkHttpClientBuilder.cache(cache);
        }
        catch (Exception e){
            Log.d("okHttpUtil init","error");
        }

    }


    public  void requestNetwork(){
        Request request=new Request.Builder().get().url("http://ovn2e2iaq.bkt.clouddn.com/2.jpg").build();
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //响应体在获取后必须关闭，否则会有资源泄露
                try{
                    Bitmap bitmap= BitmapFactory.decodeStream(response.body().byteStream()) ;

                }
                catch (Exception e){
                    Log.d(TAG, "onResponse: "+e);
                }
                finally {
                    response.body().close();
                }
          }
        });
    }


    /**
     * get 请求
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void get(Context context, final String url, final Map<String, String> params, final NetworkResponse responseHandler) {
        //拼接url
        String get_url = url;
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .tag(context)
                    .build();
        }
        Log.d("发送", "get: "+pinjieurl(url,params));
      mOkHttpClient.newCall(request).enqueue(new MyCallback(responseHandler));
    }

    /**
     * post 请求
     * @param context 发起请求的context
     * @param url url
     * @param params 参数
     * @param responseHandler 回调
     */
    public void post(Context context, final String url, final Map<String, String> params, final NetworkResponse responseHandler) {
        //post builder 参数
        FormBody.Builder builder = new FormBody.Builder();
        if(params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request request;

        //发起request
        if(context == null) {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(context)
                    .build();
        }


        mOkHttpClient.newCall(request).enqueue(new MyCallback( responseHandler));
    }
    //临时查看，拼接url
    public String pinjieurl(String url,Map<String, String> params){
        //拼接url
        String get_url = url;
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        return  get_url;
    }

    /**
     * 取消当前context的所有请求
     * @param context
     */
    public static void cancel(Context context) {
        if(mOkHttpClient != null) {
            for(Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
            for(Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if(call.request().tag().equals(context))
                    call.cancel();
            }
        }
    }

    private class MyCallback implements Callback{
        NetworkResponse networkResponse;
        public MyCallback(NetworkResponse Re){
            networkResponse=Re;
        }
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "onFailure: "+e);
            networkResponse.onFailure(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if(response.isSuccessful()){
                //final String response_body=response.body().string();
                if(networkResponse instanceof okHttpUtil_BitmapResponse){
                    Bitmap bitmap= BitmapFactory.decodeStream(response.body().byteStream()) ;
                    ((okHttpUtil_BitmapResponse) networkResponse).onResponse(bitmap);
                }
                if(networkResponse instanceof okHttpUtil_JsonResponse){
                    try {
                        ((okHttpUtil_JsonResponse) networkResponse).onResponse(new JSONObject(response.body().string()));
                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: Json"+e);
                        e.printStackTrace();
                    }
                }
                if(networkResponse instanceof okHttpUtil_RawResponse) {
                    try {  ((okHttpUtil_RawResponse) networkResponse).onResponse(response.body().string());
                    }catch(JSONException e){

                    }

                }
            }
        }
    }

}