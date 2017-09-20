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

public class OtherDisplayManager implements StatusCode,okHttpUtil_JsonResponse {
    public Context context;
    private ACache cache;
    private UserModel userModel;
    private LoginDataModel loginData;
    private Handler sHandler;
    private String name = null;
    private String sign = null;
    private String local = null;
    private String himage = null;
    private String bimage = null;
    private String sex = null;
    private int following = 0;
    private int follower = 0;
    private int yuepai = 0;
    private int GRZP = 1000;
    private int GRZP_NUM = 0;
    private ArrayList<String> GRZP_URL= new ArrayList();
    private int ZPJ = 2000;
    private int ZPJ_NUM = 0;
    private ArrayList<String> ZPJ_URL = new ArrayList<>();
    private ArrayList<Integer> ZPJ_ID = new ArrayList<>();
    private ArrayList<String> ZPJ_TITLE= new ArrayList<>();
    public Map<String,String> m=new HashMap<>();
    private String seeid;
    private Boolean followor;
private int yuepaiNum=0;
    public OtherDisplayManager(Context temp, Handler handler,String seeid) {
        context = temp;
        sHandler = handler;
        cache = ACache.get(context);
        loginData = (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginData.getUserModel();
        this.seeid=seeid;

    }
    public int getGRZP_NUM(){
        return  GRZP_NUM;
    }
    public int getZPJ_NUM(){
        return ZPJ_NUM;
    }
    public ArrayList<String> getGRZP_URL(){
        return GRZP_URL;
    }
    public ArrayList<String> getZPJ_URL(){
        return ZPJ_URL;
    }
    public ArrayList<Integer> getZPJ_ID(){
        return ZPJ_ID;
    }
    public ArrayList<String> getZPJ_TITLE(){
        return ZPJ_TITLE;
    }
    public void showUserInfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                String userId = null;
                String authkey = null;
                userId = userModel.getId();//usermodel==原content
                authkey = userModel.getAuth_key();
                map.put("uid", userId);
                map.put("authkey",authkey);
                map.put("seeid", seeid);
                map.put("type", Integer.toString(StatusCode.REQUEST_OTHERUSER_INFO));
                // requestOthers.httpRequest(map, CommonUrl.otherUserInfo);
                //Log.d("PPPPPPPPPPPPPPPPP",okHttpUtil.instance.pinjieurl(CommonUrl.otherUserInfo,map));
                okHttpUtil.instance.post(context,CommonUrl.otherUserInfo,map,OtherDisplayManager.this);
            }
        }.start();

    }

    public void showGRZP() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, String> map1 = new HashMap<>();
                String userId = null;
                String authkey = null;

                userId = userModel.getId();//usermodel==原content
                authkey = userModel.getAuth_key();
                map1.put("uid", seeid);
                map1.put("authkey", authkey);
                map1.put("type", Integer.toString(StatusCode.REQUEST_USER_GRZP));
                //netRequestG.httpRequest(map, CommonUrl.userImage);
                //Log.d("PPPPPPPPPPPPPPPPP",okHttpUtil.instance.pinjieurl(CommonUrl.userImage,map));
                okHttpUtil.instance.post(context, CommonUrl.userImage,map1,OtherDisplayManager.this);
                Log.d("AAAAAAAAAAAAAAAAA", "发送个人照片请求");
            }
        }.start();
    }

    public void showZPJ() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                String authkey = null;
                authkey = userModel.getAuth_key();

                map.put("uid", seeid);
                map.put("authkey", authkey);
                map.put("type", Integer.toString(StatusCode.REQUEST_USER_ZPJ));
                // netRequestZ.httpRequest(map, CommonUrl.userImage);
                okHttpUtil.instance.post(context, CommonUrl.userImage,map,OtherDisplayManager.this);
                Log.d("AAAAAAAAAAAAAAAAA", "发送照片集请求");
            }
        }.start();
    }
    public Map<String,String> getUserInfo(){
        return m;
    }


    public void onResponse(JSONObject result) throws JSONException {

        Log.d("PPPPPPPPPPPPPPPPP",result.toString());
        int code = Integer.valueOf(result.getString("code"));

        switch(code){
            case StatusCode.RECIEVE_VISIT_SUCCESS:
                Log.d("LQQQQQQQ", "RECIEVE_VISIT_SUCCESS");
                JSONObject object1 = result.getJSONObject("contents");
                JSONObject object2 = object1.getJSONObject("user_info");

                following = object2.getInt("ulikeN");
                follower = object2.getInt("ulikedN");
                name = object2.getString("ualais");
                sign = object2.getString("usign");
                local = object2.getString("ulocation");
                himage = object2.getString("uimage");
                following = object2.getInt("ulikeN");
                follower = object2.getInt("ulikedN");
                yuepai = object2.getInt("uapN");
                followor = object1.getBoolean("follow");
                sex = String.valueOf(object2.get("usex"));

                yuepaiNum=object1.getInt("appnum");
                following = object2.getInt("ulikeN");
                follower = object2.getInt("ulikedN");



                m.put("yuepaiNum",Integer.toString(yuepaiNum));
                Log.d("LQQQQQQQ",Integer.toString(following));

                m.put("following",Integer.toString(following));
                m.put("follower",Integer.toString(follower));
                m.put("name",name);
                m.put("sign",sign);
                m.put("local",local);
                m.put("himage",himage);
                m.put("sex",sex);


                Message message=sHandler.obtainMessage();
                message.what = StatusCode.RECIEVE_VISIT_SUCCESS;
                sHandler.sendMessage(message);

           /* case StatusCode.RECIEVE_VISIT_REJECT:
                Log.d("LQQQQQQQ", "RECIEVE_VISIT_REJECT");
                message.what = StatusCode.RECIEVE_VISIT_REJECT;
                mHandler.sendMessage(message);*/
                break;
            case StatusCode.REQUEST_USER_GRZP_SUCCESS :
                //个人照片
                JSONArray content = result.getJSONArray("contents");
                //Log.d("AAAAAAAAAAAAAAAAA", jsonArray1.toString());
                Log.d("AAAAAAAAAAAAAAAAA", "个人照片response");
                // jsonArray1.getString(i)，get（uicurl）

                //GRZP_NUM = content.length();
                //Log.d("AAAAAAAAAAAAAAAAA",String.valueOf(GRZP_NUM));
                if(content.length()!= 0){
                    for(int i=0; i < content.length(); i++){
                        JSONObject GRZP_TEMP= content.getJSONObject(i);
                        JSONArray grzp_url=GRZP_TEMP.getJSONArray("UCIurl");
                        for(int j=0; j < grzp_url.length(); j++){
                            // Log.d("AAAAAAAAAAAAAAAAA", grzp_url.get(j).toString());
                            GRZP_URL.add(grzp_url.get(j).toString());
                            GRZP_NUM++;
                        }

                    }
                }

//                Images.imageUrls = (String[]) GRZP_URL.toArray(new String[0]);
                Message message_1=sHandler.obtainMessage();
                message_1.what = GRZP;
                sHandler.sendMessage(message_1);
                break;
            case StatusCode.REQUEST_USER_ZPJ_SUCCESS:
                //作品集
                JSONArray content_2 = result.getJSONArray("contents");
                Log.d("BBBBBBBBBBBBBBBBB",content_2.toString());
                ZPJ_NUM = content_2.length();
                Log.d("BBBBBBBBBBBBBBB", String.valueOf(ZPJ_NUM));
                Log.d("AAAAAAAAAAAAAAAAA", "作品集response");
                if(ZPJ_NUM != 0){
                    for(int i = 0; i < ZPJ_NUM; i++){
                        JSONObject jsonObject1 = content_2.getJSONObject(i);
                        ZPJ_URL.add(jsonObject1.getJSONArray("UCIurl").get(0).toString());
                        ZPJ_TITLE.add(jsonObject1.getString("UCtitle"));
                        ZPJ_ID.add(Integer.parseInt(jsonObject1.getString("UCid")));
                    }
                }
                Message message_2=sHandler.obtainMessage();
                message_2.what = ZPJ;
                sHandler.sendMessage(message_2);
                break;
            default :
                Log.d("AAAAAAAAAAAAAAAAA", "什么都没对应上");
                break;
        }
    }
    @Override
    public void onFailure(IOException e) {
        //  Log.d(TAG, "onFailure: "+e);
    }
}
