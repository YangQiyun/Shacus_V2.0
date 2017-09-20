package shacus.edu.seu.com.shacus.Data.Manager;

import android.content.Context;
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

import shacus.edu.seu.com.shacus.Activity.FollowActivity;
import shacus.edu.seu.com.shacus.Adapter.FollowItemAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;

/**
 * Created by ljh on 2017/9/11.
 */

public class FollowInfoManager implements StatusCode,okHttpUtil_JsonResponse {
    private String otherId;
    private List<UserModel> followingList = new ArrayList<>();
    private List<UserModel> followerList = new ArrayList<>();
    private FollowItemAdapter followingItemAdapter;
    private FollowItemAdapter followerItemAdapter;
    public ACache cache;
    private Context context;
    private LoginDataModel loginData;
    private UserModel userModel;
    private WeakRefHander handler;
    private int followerN;
    public FollowInfoManager(Context temp, WeakRefHander mhandler ) {
        context = temp;
        handler=mhandler;
        followingItemAdapter = new FollowItemAdapter((FollowActivity) context, followingList);
        followerItemAdapter = new FollowItemAdapter((FollowActivity) context, followerList);
        cache = ACache.get(context);
        loginData = (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginData.getUserModel();
        //doRefresh();
    }
   public boolean checkUser(int position,String type){
        if (!getOtherId (position,type).equals(userModel.getId())){
            return true;
        }else
            return false;
    }
    public String getOtherId (int position,String type){
if(type.equals("following"))
{
        UserModel next = followingList.get(position);
       otherId=  next.getId();
       return  otherId;
}
else if(type.equals("follower")){
           UserModel next = followerList.get(position);
           otherId=  next.getId();
           return  otherId;

       }
       return "error";
    }
    //初始化关注following或粉丝follower数据
    public void initFollowingInfo() {
        new Thread(){
            @Override
            public void run() {
                super.run();
        Map<String, String> map1 = new HashMap<>();
        String userId = null;
        String authkey = null;

        userId = userModel.getId();//usermodel==原content
        authkey = userModel.getAuth_key();

        map1.put("seeid", userId);
        map1.put("authkey", authkey);
        map1.put("uid", userId);
        Log.d("aaaaaaaaaaaaaa", "发送查看following请求");
        Log.d("aaaaaaaaaaaaaa", map1.toString());
        //向UserList中添加获取到的关注信息
        map1.put("type", Integer.toString(StatusCode.REQUEST_INFO_FOLLOWING));
        // request.httpRequest(map1, CommonUrl.getFollowInfo);
        okHttpUtil.instance.post(context, CommonUrl.getFollowInfo,map1,FollowInfoManager.this);
    }
}.start();
        }
    public void initFollowerInfo() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map2 = new HashMap<>();
                String userId = null;
                String authkey = null;

                userId = userModel.getId();//usermodel==原content
                authkey = userModel.getAuth_key();

                map2.put("seeid", userId);
                map2.put("authkey", authkey);
                map2.put("uid", userId);
                Log.d("aaaaaaaaaaaaaa", map2.toString());
                map2.put("type", Integer.toString(StatusCode.REQUEST_INFO_FOLLOWER));
                // request.httpRequest(map2, CommonUrl.getFollowInfo);
                okHttpUtil.instance.post(context, CommonUrl.getFollowInfo, map2, FollowInfoManager.this);
            }

                }.start();
        }

public FollowItemAdapter getFollowingAdapter(){
    followingItemAdapter = new FollowItemAdapter((FollowActivity) context,followingList);
    return followingItemAdapter;
}
    public FollowItemAdapter getFollowerAdapter(){
        followerItemAdapter = new FollowItemAdapter((FollowActivity) context,followerList);
        return followerItemAdapter;
    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
       //返回我的关注或粉丝信息

            Log.d("SSSSSSSSSSSSSSS",jsonObject.toString());
            int code=Integer.parseInt(jsonObject.getString("code"));

            Message msg = new Message();

            switch (code){
                case StatusCode.REQUEST_FOLLOWING_SUCCESS: //请求我的关注
                {
                    JSONArray content = jsonObject.getJSONArray("contents");
                    for (int i = 0;i < content.length();i++){
                        JSONObject following = content.getJSONObject(i);
                        UserModel userModel = new UserModel();
                        userModel.setHeadImage(following.getString("uimgurl"));
                        userModel.setId(following.getString("uid"));
                        userModel.setNickName(following.getString("ualais"));
                        String str = following.getString("usign");
                        if (str.length()>12){
                            str = str.substring(0,12) + "...";
                        }

                        userModel.setSign(str);
                        followingList.add(userModel);
                        Log.d("aaaaaaaaaaaaaa", "following请求response");
                    }
                    msg.what = StatusCode.REQUEST_FOLLOWING_SUCCESS;
                    handler.sendMessage(msg);

                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_SUCCESS: //请求我的粉丝
                {
                    JSONArray content = jsonObject.getJSONArray("contents");
                    for (int i = 0;i < content.length();i++){
                        JSONObject follower = content.getJSONObject(i);
                        UserModel userModel = new UserModel();
                    userModel.setHeadImage(follower.getString("uimgurl"));
                       // userModel.setHeadImage("head1.jpg");
                        userModel.setId(follower.getString("uid"));
                        userModel.setNickName(follower.getString("ualais"));
                        userModel.setIndex(follower.getBoolean("fansback"));
                        followerList.add(userModel);
                        followerN++;
                        Log.d("粉丝数",Integer.toString(followerN));
                        Log.d("aaaaaaaaaaaaaa", "粉丝请求response");
                    }
                    msg.what = StatusCode.REQUEST_FOLLOWER_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOWING_NONE:{
                    //没有关注
                    msg.what = StatusCode.REQUEST_FOLLOWING_NONE;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_NONE:{
                    //没有粉丝
                    msg.what = StatusCode.REQUEST_FOLLOWER_NONE;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_USER_ILLEGAL:{
                    //用户非法
                    msg.what = StatusCode.REQUEST_USER_ILLEGAL;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_CANCEL_SUCCESS: //取消关注成功
                {Log.d("BBBBBBBBBBBBBBBBB","取消关注response");
                    msg.what = StatusCode.REQUEST_CANCEL_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_SUCCESS://请求关注成功
                {Log.d("BBBBBBBBBBBBBBBBB","关注response");
                    msg.what = StatusCode.REQUEST_FOLLOW_SUCCESS;
                    handler.sendMessage(msg);
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_ERROR://服务器错误
                {
                    msg.what = StatusCode.REQUEST_FOLLOW_ERROR;
                    handler.sendMessage(msg);
                    break;
                }
                default:
                {
                    Log.d("BBBBBBBBBBBBBBBBB","返回码错误");
                }
            }

        }
    @Override
    public void onFailure(IOException e) {
        //Log.d(TAG, "onFailure: "+e);
        Message message = new Message();
        message.what = 88;
        handler.sendMessage(message);
    }

    public List<UserModel> getFollowingList(){
        return followingList;
    }
    public List<UserModel> getFollowerList(){
        return followerList;
    }
    }

