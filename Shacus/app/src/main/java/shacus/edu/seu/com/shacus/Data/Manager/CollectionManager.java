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

import shacus.edu.seu.com.shacus.Adapter.CollectionAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.CollectionModel;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

/**
 * Created by Mind on 2017/9/16.
 */
public class CollectionManager implements okHttpUtil_JsonResponse {
    private CollectionAdapter collectionAdapter;
    private Context context;
    private List<CollectionModel> collectionlist;
    private Handler handler;
    private Map<String,String> map=new HashMap<>();
    private UserModel userModel;
    private LoginDataModel loginDataModel;
    private static final String TAG = "CollectionManager";

    public CollectionManager(Context context,Handler handler){
        this.context=context;
        this.handler=handler;
        ACache cache=ACache.get(context);
        loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel=loginDataModel.getUserModel();
        Log.d(TAG, "CollectionManager: "+loginDataModel.getCollectionList().get(0).getUClurl().size());
        collectionAdapter=new CollectionAdapter(loginDataModel.getCollectionList(),context);
    }

    public void doRefresh(){
        new Thread(){
            @Override
            public void run() {
                map.clear();
                map.put("uid",String.valueOf(userModel.getId()));
                map.put("authkey",userModel.getAuth_key());
                map.put("type", String.valueOf(StatusCode.REQUSET_REFLASH_COLLECTION));
                okHttpUtil.instance.post(context, CommonUrl.trend,map,CollectionManager.this);
                Log.d(TAG, "doRefresh: "+okHttpUtil.instance.pinjieurl(CommonUrl.collection,map));
            }
        }.start();
    }

    public CollectionAdapter getCollectionAdapter() {
        return collectionAdapter;
    }

    public List<CollectionModel> getCollectionlist() {
        return collectionlist;
    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        Message msg=handler.obtainMessage();
        int code= Integer.parseInt(jsonObject.getString("code"));
        Log.d(TAG, "onResponse: "+code);
        switch (code) {
            case StatusCode.REFLASH_COLLECTION_SUCCESS:
                collectionlist.clear();
                JSONArray array = jsonObject.getJSONArray("contents");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject info = array.getJSONObject(i);
                    CollectionModel model=new CollectionModel();
                    model.setUCcreateT(info.getString("UCcreateT"));
                    model.setUCid(Integer.parseInt(info.getString("UCid")));
                    model.setUClikeN(Integer.parseInt(info.getString("UClikeN")));
                    model.setUCtitile(info.getString("UCtitle"));
                    model.setUCualais(info.getString("UCualais"));
                    model.setUCuid(Integer.parseInt(info.getString("UCuid")));
                    model.setUCuimurl(info.getString("UCuimurl"));
                    ArrayList<String>  list = new ArrayList<String>();
                    JSONArray jsonArray = info.getJSONArray("UCIurl");
                    for (int j = 0; j < jsonArray.length();j++){
                        list.add(jsonArray.getString(j));
                    }
                    model.setUClurl(list);
                    collectionlist.add(model);
                }
                msg.what=StatusCode.REFLASH_COLLECTION_SUCCESS;
                msg.obj=collectionlist;
                handler.sendMessage(msg);
                break;
            case StatusCode.REFLASH_COLLECTION_FAILURE:
                msg.what=StatusCode.REFLASH_COLLECTION_FAILURE;
                handler.sendMessage(msg);
                break;
        }
    }

    @Override
    public void onFailure(IOException e) {
        Message msg=handler.obtainMessage();
        msg.what=404;
        handler.sendMessage(msg);
    }
}
