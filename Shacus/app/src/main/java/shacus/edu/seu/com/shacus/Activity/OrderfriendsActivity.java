package shacus.edu.seu.com.shacus.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.OrderFriendsAdapter;
import shacus.edu.seu.com.shacus.Data.Model.OrderFriendsModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.View.RefreshView;

public class OrderfriendsActivity extends AppCompatActivity implements okHttpUtil_JsonResponse {
    private RefreshView refreshLayout;
    private ListView listView;
    private TextView back,title;
    private List<OrderFriendsModel> mList = new ArrayList<>();
    private OrderFriendsAdapter personAdapter;
    private int type = 80006;
    private static final String TAG = "OrderfriendsActivity";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 80006:
                    personAdapter.refresh(mList);
                    personAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfriends);

        back=(TextView) findViewById(R.id.orderfriends_toolbar_back);
        back.setText("＜返回");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        title=(TextView)findViewById(R.id.orderfriends_toolbar_title);
        title.setText("约拍伴侣");

        refreshLayout = (RefreshView) findViewById(R.id.orderfriends_swiperefresh_layout);
        listView = (ListView)findViewById(R.id.orderfriends_list);

        personAdapter = new OrderFriendsAdapter(this,mList);
        listView.setAdapter(personAdapter);


        doRefresh();

        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(R.color.zero_black);

        refreshLayout.setOnRefreshListener(new RefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                doRefresh();
            }
        });

        refreshLayout.setOnLoadListener(new RefreshView.OnLoadListener() {
            @Override
            public void onLoad() {
                refreshLayout.setRefreshing(true);
                doRefresh();
            }
        });
    }

    private void doRefresh(){
        Map<String, String> map = new HashMap<>();
        map.put("type",Integer.toString(type));
        okHttpUtil.instance.post(this,CommonUrl.companionList,map,this);
        Log.d(TAG, "doRefresh: "+okHttpUtil.instance.pinjieurl(CommonUrl.companionList,map));
    }


    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        String code = jsonObject.getString("code");
        Log.d(TAG, "onResponse: "+code);
        JSONArray array = jsonObject.getJSONArray("contents");
        if(code.equals("800060")){
            mList = new ArrayList<>();
            for(int i =0;i<array.length();i++){
                JSONObject info = array.getJSONObject(i);
                Gson gson = new Gson();
                OrderFriendsModel companion = gson.fromJson(info.toString(), OrderFriendsModel.class);
                mList.add(companion);
            }
            Message msg=mHandler.obtainMessage();
            msg.what= 80006;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onFailure(IOException e) {
        Log.d(TAG, "onFailure: "+e);
    }
}
