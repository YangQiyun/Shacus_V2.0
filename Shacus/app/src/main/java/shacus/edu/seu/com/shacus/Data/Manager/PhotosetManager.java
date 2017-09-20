package shacus.edu.seu.com.shacus.Data.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;

/**
 * Created by ljh on 2017/9/16.
 */

public class PhotosetManager implements StatusCode,okHttpUtil_JsonResponse {
    public ACache cache;
    private Context context;
    private LoginDataModel loginData;
    private UserModel userModel;
    private Handler handler;
    private int ucid;
    private ArrayList<String> ZPJ_DETAIL_URL = new ArrayList<>();

    private int ZPJ_DETAIL_NUM=0;
    public PhotosetManager(Context temp, Handler mhandler,int ucid ) {
        context = temp;
        handler=mhandler;
        this.ucid=ucid;

        cache = ACache.get(context);
        loginData = (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginData.getUserModel();
        //doRefresh();
    }
    public void initPhotosetInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, String> map1 = new HashMap<>();
                String userId = null;
                String authkey = null;

                userId = userModel.getId();//usermodel==原content
                authkey = userModel.getAuth_key();

                map1.put("UCid", Integer.toString(ucid));
                map1.put("authkey", authkey);
                map1.put("uid", userId);

                Log.d("aaaaaaaaaaaaaa", map1.toString());
                //向UserList中添加获取到的关注信息
                map1.put("type", Integer.toString(StatusCode.REQUEST_PHOTOSET_DETAIL));
                // request.httpRequest(map1, CommonUrl.getFollowInfo);
                okHttpUtil.instance.post(context, CommonUrl.userImage, map1, PhotosetManager.this);
            }
        }.start();
    }
    public ArrayList<String> getZPJ_DETAIL_URL(){
        return ZPJ_DETAIL_URL;
    }
    public int getZPJ_DETAIL_NUM(){
        return ZPJ_DETAIL_NUM;
    }

    public void onResponse(JSONObject jsonObject) throws JSONException {
        //返回我的关注或粉丝信息

        Log.d("SSSSSSSSSSSSSSS", jsonObject.toString());
        int code = Integer.parseInt(jsonObject.getString("code"));

        Message msg = new Message();

        switch (code) {
            case StatusCode.REQUEST_PHOTOSET_DETAIL_FAIL: //请求我的关注
                msg.what = StatusCode.REQUEST_PHOTOSET_DETAIL_FAIL;
                handler.sendMessage(msg);
                break;
            case StatusCode.REQUEST_PHOTOSET_DETAIL_SUCCESS: //请求我的关注
            {
                JSONArray content = jsonObject.getJSONArray("contents");
                JSONObject ZPJ_TEMP = content.getJSONObject(0);
                JSONArray zpj_url = ZPJ_TEMP.getJSONArray("UCIurl");
                for (int j = 0; j < zpj_url.length(); j++) {
                     Log.d("AAAAAAAAAAAAAAAAA", zpj_url.get(j).toString());
                    ZPJ_DETAIL_URL.add(zpj_url.get(j).toString());
                    ZPJ_DETAIL_NUM++;
                }
            }
            msg.what = StatusCode.REQUEST_PHOTOSET_DETAIL_SUCCESS;
            handler.sendMessage(msg);
            break;
        }
    }
        public void onFailure(IOException e) {
            //Log.d(TAG, "onFailure: "+e);
            Message message = new Message();
            message.what = 88;
            handler.sendMessage(message);
        }

    }

