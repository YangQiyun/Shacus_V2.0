package shacus.edu.seu.com.shacus.Data.Manager;

import android.app.Activity;
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
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.YuepaishowAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

/**
 * Created by Mind on 2017/9/10.
 */
public class PlaceholderManager implements StatusCode,okHttpUtil_JsonResponse {
    private Handler mHandler;
    private List<PhotographerModel> yuepaiList = new ArrayList<>();
    private int type=0;//0为全部约拍，其它数字为类型
    private YuepaishowAdapter personAdapter;
    private LoginDataModel loginData;
    private UserModel userModel;
    private Context context;
    private ACache cache;
    private static final String TAG = "PlaceholderManager";
    private int mb;
    private int WANT_LOAD;
    private int LOAD_MORE;
    private int RETURN_SUCCESS;
    private int RETURN_MORE_SUCCESS;
    private int getActivitytype;

    public PlaceholderManager(Context temp, Handler handler, int getlabtype, int getActivitytype){
        context=temp;
        mHandler=handler;
        type=getlabtype;
        this.getActivitytype=getActivitytype;
        if(getActivitytype==0){//摄影师招募会
            WANT_LOAD=WANT_BE_PHOTOGRAPH;
            LOAD_MORE=WANT_BE_PHOTOGRAPH_MORE;
            RETURN_SUCCESS=REQUEST_YUEPAI_MODEL_LIST_SUCCESS;
            RETURN_MORE_SUCCESS=REQUEST_YUEPAI_MORE_MODEL_LIST_SUCCESS;
        }
        if(getActivitytype==1) {//模特招募会
            WANT_LOAD=WANT_TO_PHOTOGRAPH;
            LOAD_MORE=WANT_TO_PHOTOGRAPH_MORE;
            RETURN_SUCCESS=REQUEST_YUEPAI_GRAPH_LIST_SUCCESS;
            RETURN_MORE_SUCCESS=REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS;
        }
        if(getActivitytype==2) {//我的订单管理
            WANT_LOAD=WANT_TO_YUEPAI;
            LOAD_MORE=0;
            RETURN_SUCCESS=WANT_TO_YUEPAI_SUCCESS;
            RETURN_MORE_SUCCESS=0;
        }
        personAdapter=new YuepaishowAdapter((Activity)context, yuepaiList,getActivitytype,getlabtype);
        cache=ACache.get(context);
        loginData= (LoginDataModel) cache.getAsObject("loginModel");
        userModel=loginData.getUserModel();
        doRefresh();
    }

    public List<PhotographerModel> getYuepailist() {
        return yuepaiList;
    }


    public YuepaishowAdapter getPersonAdapter() {
        return personAdapter;
    }

    public void setPersonAdapter(YuepaishowAdapter personAdapter) {
        this.personAdapter = personAdapter;
    }

    public void setYuepailist(List<PhotographerModel> yuepailist) {
        this.yuepaiList = yuepailist;
    }

    public void doRefresh(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_LOAD));//10235想被拍的约拍列表（模特发布的约拍）
                map.put("authkey", userModel.getAuth_key());
                map.put("group",Integer.toString(type));
                map.put("uid", userModel.getId());
                okHttpUtil.instance.post(context, CommonUrl.getYuePaiInfo,map,PlaceholderManager.this);
                Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.getYuePaiInfo,map));
            }
        }.start();
    }

    public void Loadmore(int bootCounter){
        mb=bootCounter;
        if(getActivitytype!=2)
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(LOAD_MORE));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("group",Integer.toString(type));
                map.put("offsetapid", Integer.toString(personAdapter.getItem(mb - 1).getAPid()));
                okHttpUtil.instance.post(context,CommonUrl.getYuePaiInfo,map,PlaceholderManager.this);
                Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.getYuePaiInfo,map));
            }
        }.start();

    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        Log.d(TAG, "onResponse: "+jsonObject.getString("code"));
        int code=(int)Integer.parseInt(jsonObject.getString("code"));
        switch (code){
            case WANT_TO_YUEPAI_SUCCESS:
            case REQUEST_YUEPAI_GRAPH_LIST_SUCCESS:
            case REQUEST_YUEPAI_MODEL_LIST_SUCCESS://第一次加载/刷新的返回
                yuepaiList.clear();
                JSONArray array = jsonObject.getJSONArray("contents");
                for (int i = 0; i < array.length(); i++){
                    JSONObject info = array.getJSONObject(i);
                    PhotographerModel photographerModel = new PhotographerModel();
                    UserModel userModel = new UserModel();
                    userModel.setId(String.valueOf(info.get("sponsorid")));
                    userModel.setLocation(info.getString("Userlocation"));
                    userModel.setNickName(info.getString("Useralais"));
                    userModel.setAge(info.getString("Userage"));
                    userModel.setHeadImage(info.getString("Userimg"));
                    userModel.setSex(info.getString("Usex"));
                    photographerModel.setUserModel(userModel);
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("APimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    photographerModel.setAPimgurl(list);
                    photographerModel.setAPgroup(info.getInt("APgroup"));
                    photographerModel.setAPcontent(info.getString("APcontent"));
                    photographerModel.setAPtime(info.getString("APtime"));
                    photographerModel.setAPcreatetime(info.getString("APcreatetime"));
                    photographerModel.setAPpricetype(info.getInt("APpricetype"));
                    photographerModel.setAPprice(info.getString("APprice"));
                    photographerModel.setAPlikeN(info.getInt("APlikeN"));
                    photographerModel.setAPid(info.getInt("APid"));
                    photographerModel.setAPstatus(info.getInt("APstatus"));
                    yuepaiList.add(photographerModel);
                }
                Message msg=mHandler.obtainMessage();
                msg.what=RETURN_SUCCESS;
                msg.obj=array.length();
                mHandler.sendMessage(msg);
                break;
            case 10253:
            case 10263://加载更多返回
                JSONArray array_2 = jsonObject.getJSONArray("contents");
                List<PhotographerModel> addList = new ArrayList<>();
                for (int i = 0; i < array_2.length(); i++) {
                    JSONObject info = array_2.getJSONObject(i);
                    PhotographerModel photographerModel = new PhotographerModel();
                    UserModel userModel = new UserModel();
                    userModel.setId(String.valueOf(info.get("sponsorid")));
                    userModel.setLocation(info.getString("Userlocation"));
                    userModel.setNickName(info.getString("Useralais"));
                    userModel.setAge(info.getString("Userage"));
                    userModel.setHeadImage(info.getString("Userimg"));
                    userModel.setSex(info.getString("Usex"));
                    photographerModel.setUserModel(userModel);
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("APimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    photographerModel.setAPimgurl(list);
                    photographerModel.setAPgroup(info.getInt("APgroup"));
                    photographerModel.setAPcontent(info.getString("APcontent"));
                    photographerModel.setAPcreatetime(info.getString("APcreatetime"));
                    photographerModel.setAPpricetype(info.getInt("APpricetype"));
                    photographerModel.setAPprice(info.getString("APprice"));
                    photographerModel.setAPlikeN(info.getInt("APlikeN"));
                    photographerModel.setAPtime(info.getString("APtime"));
                    photographerModel.setAPid(info.getInt("APid"));
                    photographerModel.setAPstatus(info.getInt("APstatus"));
                    yuepaiList.add(photographerModel);
                    addList.add(photographerModel);
                }
                Message msg_2 = mHandler.obtainMessage();
                msg_2.arg1=array_2.length();
                msg_2.what = RETURN_MORE_SUCCESS;
                msg_2.obj=addList;
                mHandler.sendMessage(msg_2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.d(TAG, "onFailure: "+e);
    }
}
