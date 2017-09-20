package shacus.edu.seu.com.shacus.Data.Manager;

import android.content.Context;
import android.os.Bundle;
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
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.ForumItemModel;
import shacus.edu.seu.com.shacus.Data.Model.ForumModel;
import shacus.edu.seu.com.shacus.Data.Model.ImageData;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.RemarkModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;

/**
 * Created by Administrator on 2017/9/13.
 */

public class ForumManager implements StatusCode,okHttpUtil_JsonResponse {

    private Handler mHandler;
    private ForumListAdapter forumListAdapter;
    private LoginDataModel loginData;
    private UserModel userModel;
    private Context context;
    private int id;
    private ACache cache;
    private int mb;
    private static final String TAG = "ForumManager";

    public ForumManager(Context temp, Handler handler, int listid){
        mHandler=handler;
        context=temp;
        id=listid;
        ACache cache=ACache.get(temp);
        LoginDataModel loginmodel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginmodel.getUserModel();
    }

    public void onLoad(){
        Log.d(TAG, "onLoad: ");
        Log.e("onLoad forum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_FORUM_DETAIL));//
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                okHttpUtil.instance.post(context, CommonUrl.getFCInfo,map,ForumManager.this);
            }
        }.start();
    }

    public void sendRemark(final String remark_content){
        Log.e("sendRemark forum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_SEND_REMARK));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                map.put("content",remark_content);
                okHttpUtil.instance.post(context, CommonUrl.sendComment,map,ForumManager.this);
            }
        }.start();

    }
    public void addFavor(){
        Log.e("addFavor forum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_ADD_FAVOR));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                okHttpUtil.instance.post(context, CommonUrl.getFavor,map,ForumManager.this);
            }
        }.start();
    }
    public void cancelFavor(){
        Log.e("cancelFavorforum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_CANCEL_FAVOR));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                okHttpUtil.instance.post(context, CommonUrl.getFavor,map,ForumManager.this);
            }
        }.start();
    }
    public void addCollect(){
        Log.e("addCollect forum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_ADD_COLLECT));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                okHttpUtil.instance.post(context, CommonUrl.getCollect,map,ForumManager.this);
            }
        }.start();
    }
    public void cancelCollect(){
        Log.e("cancelC forum detail","thread");
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String, String> map = new HashMap<>();
                map.put("type", Integer.toString(WANT_CANCEL_COLLECT));
                map.put("authkey", userModel.getAuth_key());
                map.put("uid", userModel.getId());
                map.put("cqid", String.valueOf(id));
                okHttpUtil.instance.post(context, CommonUrl.getCollect,map,ForumManager.this);
            }
        }.start();
    }
    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {

        Log.e("forum detail response", jsonObject.toString());

        int code = (int) Integer.parseInt(jsonObject.getString("code"));
        switch (code) {
            case REQUEST_FORUM_REMARKLESS_SUCCESS:
            case REQUEST_FORUM_WITHOUT_REMARK_SUCCESS:
            case REQUEST_FORUM_DETAIL_SUCCESS:
                List<RemarkModel> remarkList = new ArrayList<RemarkModel>();
                ForumModel forumModel = new ForumModel();
                JSONObject content = jsonObject.getJSONObject("contents");
                JSONArray questionArray = content.getJSONArray("Question");
                JSONObject question = questionArray.getJSONObject(0);

                forumModel.setCQuiscollect(question.getInt("CQuiscollect"));
                forumModel.setCQuimurl(question.getString("CQuimurl"));
                forumModel.setCQuid(question.getInt("CQuid"));
                forumModel.setCQcontent(question.getString("CQcontent"));

                List<String> list = new ArrayList<String>();
                JSONArray imgArray = question.getJSONArray("CQimgurl");
                for (int j = 0; j < imgArray.length(); j++) {
                    list.add(imgArray.getString(j));
                }
                forumModel.setCQimg(list);
                forumModel.setCQcommentN(question.getInt("CQcommentN"));
                forumModel.setCQtitle(question.getString("CQtitle"));
                forumModel.setCQtime(question.getString("CQtime"));
                forumModel.setCQuesid(question.getInt("CQuesid"));
                forumModel.setCQuname(question.getString("CQuname"));
                forumModel.setCQlikedN(question.getInt("CQlikedN"));
                forumModel.setCQuisfavor(question.getInt("CQuisfavorite"));

                if(code==REQUEST_FORUM_DETAIL_SUCCESS) {
                    JSONArray commentArray = content.getJSONArray("Comments");
                    for (int i = 0; i < commentArray.length(); i++) {
                        JSONObject comment = commentArray.getJSONObject(i);
                        RemarkModel remarkModel = new RemarkModel();
                        remarkModel.setRMcmtT(comment.getString("CQcmtT"));
                        remarkModel.setRMcmtuname(comment.getString("CQcmtualais"));
                        remarkModel.setRMcmtcontent(comment.getString("CQcmtcontent"));
                        remarkModel.setRMcmtvalid(comment.getInt("CQcmtvalid"));
                        remarkModel.setRMcmtid(comment.getInt("CQcmtid"));
                        remarkModel.setRMcmtuid(comment.getInt("CQcmtuid"));
                        remarkModel.setRMcmtuimg(comment.getString("CQcmtuimurl"));
                        remarkModel.setRMcmtquesid(comment.getInt("CQcmtquesid"));
                        remarkList.add(remarkModel);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = REQUEST_FORUM_DETAIL_SUCCESS;
                msg.obj = remarkList;
                Bundle b = new Bundle();
                b.putSerializable("ForumModel", forumModel);
                msg.setData(b);
                mHandler.sendMessage(msg);
                Log.e("Message fd", "mHandler");
                break;
            case REQUEST_SEND_REMARK_SUCCESS:
                Message msg2 = mHandler.obtainMessage();
                msg2.what = REQUEST_SEND_REMARK_SUCCESS;
                mHandler.sendMessage(msg2);
                Log.e("Message sr", "mHandler");
                break;
            case 850714:
            case REQUEST_ADD_FAVOR_SUCCESS:
                Message msg3 = mHandler.obtainMessage();
                msg3.what = REQUEST_ADD_FAVOR_SUCCESS;
                mHandler.sendMessage(msg3);
                Log.e("Message afs", "mHandler");
                break;
            case 850716:
            case REQUEST_ADD_FAVOR_ERROR:
                Message msg31 = mHandler.obtainMessage();
                msg31.what = REQUEST_ADD_FAVOR_ERROR;
                mHandler.sendMessage(msg31);
                Log.e("Message afe", "mHandler");
                break;
            case 850734:
            case REQUEST_CANCEL_FAVOR_SUCCESS:
                Message msg4 = mHandler.obtainMessage();
                msg4.what = REQUEST_CANCEL_FAVOR_SUCCESS;
                mHandler.sendMessage(msg4);
                Log.e("Message cf", "mHandler");
                break;
            case REQUEST_CANCEL_FAVOR_ERROR:
                Message msg41 = mHandler.obtainMessage();
                msg41.what = REQUEST_CANCEL_FAVOR_ERROR;
                mHandler.sendMessage(msg41);
                Log.e("Message ce", "mHandler");
                break;
            case 851118:
            case 851120:
            case REQUEST_ADD_COLLECT_SUCCESS:
                Message msg5 = mHandler.obtainMessage();
                msg5.what = REQUEST_ADD_COLLECT_SUCCESS;
                mHandler.sendMessage(msg5);
                Log.e("Message acs", "mHandler");
                break;
            case 851114:
            case 851116:
            case REQUEST_ADD_COLLECT_ERROR:
                Message msg51 = mHandler.obtainMessage();
                msg51.what = REQUEST_ADD_COLLECT_ERROR;
                mHandler.sendMessage(msg51);
                Log.e("Message ace", "mHandler");
                break;
            case 851132:
            case REQUEST_CANCEL_COLLECT_SUCCESS:
                Message msg6 = mHandler.obtainMessage();
                msg6.what = REQUEST_CANCEL_COLLECT_SUCCESS;
                mHandler.sendMessage(msg6);
                Log.e("Message cc", "mHandler");
                break;
            case REQUEST_CANCEL_COLLECT_ERROR:
                Message msg61 = mHandler.obtainMessage();
                msg61.what = REQUEST_CANCEL_COLLECT_ERROR;
                mHandler.sendMessage(msg61);
                Log.e("Message cc", "mHandler");
                break;
            case 850600:
            case 850700:
            case 851100:
            case REQUESTT_ALLDONGTAI_ERROR:
                Message msg7 = mHandler.obtainMessage();
                msg7.what = REQUESTT_ALLDONGTAI_ERROR;
                mHandler.sendMessage(msg7);
                Log.e("Message ae", "mHandler");
                break;
        }
    }

    @Override
    public void onFailure(IOException e) {
            Log.d(TAG, "onFailure: "+e);

    }
}
