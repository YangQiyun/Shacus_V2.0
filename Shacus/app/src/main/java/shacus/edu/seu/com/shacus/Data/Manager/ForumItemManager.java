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

import shacus.edu.seu.com.shacus.Adapter.ForumListAdapter;
import shacus.edu.seu.com.shacus.Adapter.ImageAddGridViewAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.ForumItemModel;
import shacus.edu.seu.com.shacus.Data.Model.ImageData;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;

/**
 * Created by Mind on 2017/9/10.
 */
public class ForumItemManager implements StatusCode,okHttpUtil_JsonResponse {
    private Handler mHandler;
    private int type;
    private LoginDataModel loginData;
    private UserModel userModel;
    private Context context;
    private int lastid=0;
    private ACache cache;
    private static final String TAG = "ForumItemManager";
    private int mb;


    public ForumItemManager(Context temp, Handler handler, int t){
        context=temp;
        mHandler=handler;
        type=t;
        cache=ACache.get(context);
        ACache cache=ACache.get(temp);
        LoginDataModel loginmodel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginmodel.getUserModel();
        doRefresh();
    }


    public void doRefresh(){
        Log.d("doRefresh","thread");
        if(type==WANT_COMMUNITY_FORUM_LIST) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Map<String, String> map = new HashMap<>();
                    map.put("type", Integer.toString(WANT_COMMUNITY_FORUM_LIST));
                    map.put("authkey", userModel.getAuth_key());
                    map.put("uid", userModel.getId());
                    okHttpUtil.instance.post(context, CommonUrl.getFCInfo, map, ForumItemManager.this);
                }
            }.start();
        }else if(type==WANT_MINE_FORUM_LIST){
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Map<String, String> map = new HashMap<>();
                    map.put("type", Integer.toString(WANT_MINE_FORUM_LIST));
                    map.put("authkey", userModel.getAuth_key());
                    map.put("uid", userModel.getId());
                    okHttpUtil.instance.post(context, CommonUrl.getFCInfo, map, ForumItemManager.this);
                }
            }.start();
        }
    }

    public void Loadmore(int lastId){
        lastid=lastId;

        Log.d("Loadmore","thread");
        if(type==WANT_COMMUNITY_FORUM_LIST) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Map<String, String> map = new HashMap<>();
                    map.put("type", Integer.toString(WANT_COMMUNITY_MORE_FORUM_LIST));
                    map.put("authkey", userModel.getAuth_key());
                    map.put("uid", userModel.getId());
                    map.put("lastid", Integer.toString(lastid));
                    okHttpUtil.instance.post(context, CommonUrl.getFCInfo, map, ForumItemManager.this);
                }
            }.start();
        }else if(type==WANT_MINE_FORUM_LIST){
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Map<String, String> map = new HashMap<>();
                    map.put("type", Integer.toString(WANT_MINE_MORE_FORUM_LIST));
                    map.put("authkey", userModel.getAuth_key());
                    map.put("uid", userModel.getId());
                    map.put("lastid", Integer.toString(lastid));
                    okHttpUtil.instance.post(context, CommonUrl.getFCInfo, map, ForumItemManager.this);
                }
            }.start();
        }
    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        Log.d("CCCCCCCCCCCCC", jsonObject.toString());

        int code=(int)Integer.parseInt(jsonObject.getString("code"));
        switch (code){
            case REQUEST_COMMUNITY_FORUM_LIST_SUCCESS://第一次加载/刷新的返回
            case REQUEST_MINE_FORUM_LIST_SUCCESS:
                List<ForumItemModel> forumitemList = new ArrayList<ForumItemModel>();
                JSONArray array = jsonObject.getJSONArray("contents");
                for (int i = 0; i < array.length(); i++){
                    JSONObject info = array.getJSONObject(i);
                    ForumItemModel forumItemModel = new ForumItemModel();

                    forumItemModel.setFCtitle(info.getString("CQtitle"));
                    forumItemModel.setFCcontent(info.getString("CQcontent"));
                    forumItemModel.setFCid(info.getInt("CQuesid"));
                    forumItemModel.setFCfavornum(info.getInt("CQlikedN"));
                    forumItemModel.setFCremarknum(info.getInt("CQcommentN"));
                    forumItemModel.setFCremarktime(info.getString("CQtime"));
                    forumItemModel.setFCuserid(info.getInt("CQuid"));
                    forumItemModel.setFCusername(info.getString("CQuname"));
                    forumItemModel.setFCiscollect(info.getInt("CQuiscollect"));
                    ImageData head = new ImageData(info.getString("CQuimurl"));
                    forumItemModel.setFCuserheadphoto(head);

                    List<ImageData>  list = new ArrayList<ImageData>();
                    JSONArray jsonArray = info.getJSONArray("CQimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        ImageData imageData = new ImageData(jsonArray.getString(j));
                        list.add(imageData);
                    }
                    if(list==null)
                        forumItemModel.setFCpicnum(0);
                    else
                        forumItemModel.setFCpicnum(list.size());
                    forumItemModel.setFCpictures(list);

                    forumitemList.add(forumItemModel);
                }
                if(type==WANT_COMMUNITY_FORUM_LIST) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = REQUEST_COMMUNITY_FORUM_LIST_SUCCESS;
                    msg.arg1 = array.length();
                    msg.obj = forumitemList;
                    mHandler.sendMessage(msg);
                    Log.d("Message", "COMMUNITY mHandler");
                }else if(type==WANT_MINE_FORUM_LIST){
                    Message msg = mHandler.obtainMessage();
                    msg.what = REQUEST_MINE_FORUM_LIST_SUCCESS;
                    msg.arg1 = array.length();
                    msg.obj = forumitemList;
                    mHandler.sendMessage(msg);
                    Log.d("Message", "MINE mHandler");
                }
                break;

            case REQUEST_COMMUNITY_MORE_FORUM_LIST_SUCCESS://加载更多返回
            case REQUEST_MINE_MORE_FORUM_LIST_SUCCESS:
                JSONArray array_2 = jsonObject.getJSONArray("contents");
                List<ForumItemModel> addList = new ArrayList<>();
                for (int i = 0; i < array_2.length(); i++) {
                    JSONObject info = array_2.getJSONObject(i);
                    ForumItemModel forumItemModel = new ForumItemModel();
                    forumItemModel.setFCtitle(info.getString("CQtitle"));
                    forumItemModel.setFCcontent(info.getString("CQcontent"));
                    forumItemModel.setFCid(info.getInt("CQuesid"));
                    forumItemModel.setFCfavornum(info.getInt("CQlikedN"));
                    forumItemModel.setFCremarknum(info.getInt("CQcommentN"));
                    forumItemModel.setFCremarktime(info.getString("CQtime"));
                    forumItemModel.setFCuserid(info.getInt("CQuid"));
                    forumItemModel.setFCusername(info.getString("CQuname"));

                    ImageData head = new ImageData(info.getString("CQuimurl"));
                    forumItemModel.setFCuserheadphoto(head);

                    List<ImageData>  list = new ArrayList<ImageData>();
                    JSONArray jsonArray = info.getJSONArray("CQimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        ImageData imageData = new ImageData(jsonArray.getString(j));
                        list.add(imageData);
                    }
                    forumItemModel.setFCpicnum(list.size());
                    forumItemModel.setFCpictures(list);

                    addList.add(forumItemModel);
                }
                if(type==WANT_COMMUNITY_FORUM_LIST) {
                    Message msg_2 = mHandler.obtainMessage();
                    msg_2.arg1 = array_2.length();
                    msg_2.what = REQUEST_COMMUNITY_MORE_FORUM_LIST_SUCCESS;
                    msg_2.obj = addList;
                    mHandler.sendMessage(msg_2);
                    Log.d("Message", "community more");
                }else if(type==WANT_MINE_FORUM_LIST){
                    Message msg_2 = mHandler.obtainMessage();
                    msg_2.arg1 = array_2.length();
                    msg_2.what = REQUEST_MINE_MORE_FORUM_LIST_SUCCESS;
                    msg_2.obj = addList;
                    mHandler.sendMessage(msg_2);
                    Log.d("Message", "mine more");
                }
                break;
            case REQUEST_COMMUNITY_FORUM_LIST_ERROR:
                Message msg_3 = mHandler.obtainMessage();
                msg_3.what = REQUEST_COMMUNITY_FORUM_LIST_ERROR;
                mHandler.sendMessage(msg_3);
                Log.d("Message","error");
                break;
            case REQUEST_COMMUNITY_MORE_FORUM_LIST_ERROR:
                Message msg_4 = mHandler.obtainMessage();
                msg_4.what = REQUEST_COMMUNITY_MORE_FORUM_LIST_ERROR;
                mHandler.sendMessage(msg_4);
                Log.d("Message","more error");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.d(TAG, "onFailure: "+e);
    }

    public void setLastid(int lastid) {
        this.lastid = lastid;
    }
}
