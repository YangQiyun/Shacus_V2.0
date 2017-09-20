package shacus.edu.seu.com.shacus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Activity.OtherDisplayActivity;
import shacus.edu.seu.com.shacus.Activity.ShowBigimageActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.DynamicModel;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.View.MyGridView;

/**
 * Created by Mind on 2017/9/5.
 */
public class DynamicAdapter extends BaseQuickAdapter<DynamicModel,BaseViewHolder> implements okHttpUtil_JsonResponse {
    private static final String TAG = "DynamicAdapter";
    private Context context;
    private UserModel usermodel;
    private List<String> guanzhulist=new ArrayList<>();
    private List<ImageButton> buttonList=new ArrayList<>();
    private int now_positon;
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 233:
                    for(int i=0;i<guanzhulist.size();++i){
                        if(guanzhulist.get(i).equals(String.valueOf(now_positon)))
                            if (!buttonList.get(i).isSelected())
                                buttonList.get(i).setSelected(true);
                    }
                    break;
                case 234:
                    Toast.makeText(context,"您已经关注过该用户",Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    public DynamicAdapter(Context context,List<DynamicModel> list) {
        super(R.layout.item_dynamic, list);
        this.context=context;
        ACache cache=ACache.get(context);
        LoginDataModel login= (LoginDataModel) cache.getAsObject("loginModel");
        usermodel=login.getUserModel();
    }

    @Override
    protected void convert(BaseViewHolder helper, final DynamicModel item) {
        MyGridView gridView= helper.getView(R.id.gridview);
        Log.d(TAG, "convert: "+item.getTIimgurl().size()+"here!!!!!!!!!!!!!!!!!!");
        if (item.getTIimgurl().size() !=0) {
            switch (item.getTIimgurl().size()) {
                case 1:
                    gridView.setNumColumns(1);
                    break;
                case 2:
                    gridView.setNumColumns(2);
                    break;
                case 3:
                    gridView.setNumColumns(2);
                    break;
                case 4:
                    gridView.setNumColumns(2);

                    break;
                default:
                    gridView.setNumColumns(3);
                    break;
            }
            gridView.setVisibility(View.VISIBLE);
            gridView.setAdapter(new GridviewAdapter(context,item.getTIimgurl()));
           gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent=new Intent(context,ShowBigimageActivity.class);
                   Bundle temp=new Bundle();
                   ArrayList<String> get=new ArrayList<String>();
                   for(int i=0;i<item.getTIimgurl().size();++i)
                       get.add(item.getTIimgurl().get(i));
                   temp.putStringArrayList(ShowBigimageActivity.LIST,get);
                   intent.putExtras(temp);
                   context.startActivity(intent);
               }
           });
        }else{
            gridView.setVisibility(View.GONE);
        }
        Glide.with(context).load(item.getTsponsorimg()).into((ImageView) helper.getView(R.id.Imav_touxiang));
        helper.setText(R.id.tv_name,item.getTualais());
        helper.setText(R.id.tv_time,item.getTsponsT());
        helper.setText(R.id.dynamic_title,item.getTtitle());
        helper.setText(R.id.tv_content,item.getTcontent());
        helper.getView(R.id.Imav_touxiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,OtherDisplayActivity.class);
                intent.putExtra("otherID", String.valueOf(item.getTsponsorid()));
                context.startActivity(intent);
            }
        });
        ImageButton guanzhu=helper.getView(R.id.dynamic_guanzhu);
        guanzhulist.add(String.valueOf(item.getTsponsorid()));
        buttonList.add(guanzhu);
        if(item.getTislike()==1)
            guanzhu.setSelected(true);
        else
            guanzhu.setSelected(false);
        guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map=new HashMap<String, String>();
                now_positon=item.getTsponsorid();
                map.put("type", String.valueOf(80021));
                map.put("uid",usermodel.getId());
                map.put("authkey",usermodel.getAuth_key());
                map.put("target", String.valueOf(item.getTsponsorid()));
                okHttpUtil.instance.post(context, CommonUrl.getFollowInfo,map,DynamicAdapter.this);
            }
        });
    }

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        int code= Integer.parseInt(jsonObject.getString("code"));
        Log.d(TAG, "onResponse: "+code);
        switch (code){
            case 800211:
                Log.d(TAG, "onResponse: "+"关注成功了");
                Message msg=handler.obtainMessage();
                msg.what=233;
                handler.sendMessage(msg);
                break;
            case 800210:
                //CommonUtils.getUtilInstance().showToast(MyApplication.getContext(),"您已关注过该用户");
                Message msg1=handler.obtainMessage();
                msg1.what=234;
                handler.sendMessage(msg1);
                break;
        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.d(TAG, "onFailure: "+e);
    }
}
