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
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.DynamicAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.DynamicModel;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

/**
 * Created by Mind on 2017/9/5.
 */
public class DynamicManager implements okHttpUtil_JsonResponse {
    private DynamicAdapter mDynamicAdapter;
    private Context context;
    private List<DynamicModel> trendlist;
    private Handler handler;
    private Map<String,String> map=new HashMap<>();
    private UserModel userModel;
    private LoginDataModel loginDataModel;
    private static final String TAG = "DynamicManager";

    public DynamicManager(Context context,Handler hander){
        this.handler=hander;
        this.context=context;
        ACache cache=ACache.get(context);
        loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel=loginDataModel.getUserModel();
        trendlist=loginDataModel.getTrendList();
        mDynamicAdapter=new DynamicAdapter(context,trendlist);
       // initAdapter();
    }

    private void initAdapter(){
        Message msg=handler.obtainMessage();
        msg.what=StatusCode.REFLASH_TREND_SUCCESS;
        msg.obj=trendlist;
        handler.sendMessage(msg);
    }

    //获取初始化动态列表时个数，仅可调用一次
    public int getinitnumber(){
        return  trendlist.size();
    }

    //下拉刷新所有动态
    public void doRefresh(){
        mDynamicAdapter.setEnableLoadMore(false);
        new Thread(){
            @Override
            public void run() {
                ;
                map.clear();

                map.put("uid",String.valueOf(userModel.getId()));
                map.put("authkey",userModel.getAuth_key());
                map.put("type", String.valueOf(StatusCode.REQUEST_REFLASH_TREND));
                okHttpUtil.instance.post(context, CommonUrl.trend,map,DynamicManager.this);
                Log.d(TAG, "doRefresh: "+okHttpUtil.instance.pinjieurl(CommonUrl.trend,map));
            }
        }.start();

    }

    //上拉请求更多动态
    public void Loadmore(){
        Log.d(TAG, "Loadmore: "+"ing");
        mDynamicAdapter.setEnableLoadMore(true);
        new Thread(){
            @Override
            public void run() {
                map.clear();
                map.put("uid",String.valueOf(userModel.getId()));
                map.put("authkey",userModel.getAuth_key());
                map.put("lasttid", String.valueOf(mDynamicAdapter.getData().get(mDynamicAdapter.getData().size()-1).getTid()));
                map.put("type", String.valueOf(StatusCode.REQUEST_MORE_TREND));
                okHttpUtil.instance.post(context, CommonUrl.trend,map,DynamicManager.this);
                Log.d(TAG, "Loadmore: "+okHttpUtil.instance.pinjieurl(CommonUrl.trend,map));
            }
        }.start();
    }

    public DynamicAdapter getmDynamicAdapter() {
        return mDynamicAdapter;
    }

    public List<DynamicModel> getTrendlist() {
        return trendlist;
    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        Message msg=handler.obtainMessage();
        int code= Integer.parseInt(jsonObject.getString("code"));
        Log.d(TAG, "onResponse: "+code);
        switch (code){
            case StatusCode.REFLASH_TREND_SUCCESS:
                trendlist.clear();
                JSONArray array = jsonObject.getJSONArray("contents");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    DynamicModel model=new DynamicModel();
                    model.setTualais(info.getString("Tualais"));
                    model.setTlikeN(Integer.parseInt(info.getString("TlikeN")));
                    model.setTsponsorimg(info.getString("Tsponsorimg"));
                    model.setTcontent(info.getString("Tcontent"));
                    model.setTislike(Integer.parseInt(info.getString("Tislike")));
                    model.setTcommentN(Integer.parseInt(info.getString("TcommentN")));
                    model.setTsponsorid(Integer.parseInt(info.getString("Tsponsorid")));
                    model.setTsponsT(info.getString("TsponsT"));
                    model.setTtitle(info.getString("Ttitle"));
                    model.setTid(Integer.parseInt(info.getString("Tid")));
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("TIimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    model.setTIimgurl(list);
                    trendlist.add(model);
                }
                msg.what=StatusCode.REFLASH_TREND_SUCCESS;
                msg.obj=trendlist;
                mDynamicAdapter.setEnableLoadMore(true);
                handler.sendMessage(msg);
                break;
            case StatusCode.REFLASH_TREND_FAILURE:
                msg.what=StatusCode.REFLASH_TREND_FAILURE;
                handler.sendMessage(msg);
                break;
            case StatusCode.MORE_TREND_SUCCESS:
                trendlist.clear();
                JSONArray array2 = jsonObject.getJSONArray("contents");
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject info = array2.getJSONObject(i);
                    DynamicModel model=new DynamicModel();
                    model.setTualais(info.getString("Tualais"));
                    model.setTlikeN(Integer.parseInt(info.getString("TlikeN")));
                    model.setTsponsorimg(info.getString("Tsponsorimg"));
                    model.setTcontent(info.getString("Tcontent"));
                    model.setTislike(Integer.parseInt(info.getString("Tislike")));
                    model.setTcommentN(Integer.parseInt(info.getString("TcommentN")));
                    model.setTsponsorid(Integer.parseInt(info.getString("Tsponsorid")));
                    model.setTsponsT(info.getString("TsponsT"));
                    model.setTtitle(info.getString("Ttitle"));
                    model.setTid(Integer.parseInt(info.getString("Tid")));
                    List<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("TIimgurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    model.setTIimgurl(list);
                    trendlist.add(model);
                }
                msg.what=StatusCode.MORE_TREND_SUCCESS;
                msg.obj=trendlist;
                handler.sendMessage(msg);
                break;
            case StatusCode.MORE_TREND_FAILURE:
                msg.what=StatusCode.MORE_TREND_FAILURE;
                handler.sendMessage(msg);
                break;
            case StatusCode.TREND_USRID_FAILURE:
                msg.what=StatusCode.TREND_USRID_FAILURE;
                handler.sendMessage(msg);
                break;

        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.d(TAG, "onFailure: "+"网络请求失败");
        Message msg=handler.obtainMessage();
        msg.what=110;
        handler.sendMessage(msg);
    }
}
