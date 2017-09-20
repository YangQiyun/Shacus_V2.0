package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.GridviewAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.YuepaiInfoManager;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.StatusCode;
import shacus.edu.seu.com.shacus.View.MyGridView;

/**
 * Created by Mind on 2017/9/19.
 */
public class YuepaiInforActivity extends BaseActivity implements okHttpUtil_JsonResponse {
    private static final String GETYUPAI="YUEPAIINFOMATION";
    private final int ERROR=404;
    private PhotographerModel photographerModel;
    private static final String TAG = "YuepaiInforActivity";
    private YuepaiInfoManager DataManager;
    private Button upload;
    private Handler Handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.REQUEST_BAOMIN_DELAY:
                    upload.setText("该报名已过期");
                    upload.setClickable(false);
                    upload.setTextColor(Color.BLACK);
                    upload.setBackgroundColor(Color.GRAY);
                    break;
                case StatusCode.REQUEST_BAOMIN_DELETE:
                    upload.setText("该报名已被删除");
                    upload.setClickable(false);
                    upload.setTextColor(Color.BLACK);
                    upload.setBackgroundColor(Color.GRAY);
                    break;
                case StatusCode.REQUEST_BAOMIN_DONE:
                    Toast.makeText(YuepaiInforActivity.this,"您已报名过",Toast.LENGTH_SHORT).show();
                    upload.setText("已报名");
                    upload.setClickable(false);
                    break;
                case StatusCode.REQUEST_BAOMIN_ERROR:
                    Toast.makeText(YuepaiInforActivity.this,"报名出错，请尝试重试",Toast.LENGTH_SHORT).show();
                    break;
                case StatusCode.REQUEST_BAOMIN_SUCCESS:
                    Toast.makeText(YuepaiInforActivity.this,"报名成功",Toast.LENGTH_SHORT).show();
                    upload.setText("已报名");
                    upload.setClickable(false);
                    break;
                case ERROR:
                    Toast.makeText(YuepaiInforActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuepai_info);
        ACache cache=ACache.get(this);
        photographerModel= (PhotographerModel) cache.getAsObject("ypinfo");
        DataManager=new YuepaiInfoManager(photographerModel,Handler);
        initView();
    }

    private  void initView(){
        ImageView headimge=(ImageView)findViewById(R.id.headimge);
        Glide.with(YuepaiInforActivity.this).load(photographerModel.getUserModel().getHeadImage()).into(headimge);
        MyGridView gridView= (MyGridView) findViewById(R.id.gridview);
        if (photographerModel.getAPimgurl().size() !=0) {
            switch (photographerModel.getAPimgurl().size()) {
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
            gridView.setAdapter(new GridviewAdapter(this,photographerModel.getAPimgurl()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(YuepaiInforActivity.this,ShowBigimageActivity.class);
                    Bundle temp=new Bundle();
                    ArrayList<String> array=new ArrayList<String>();
                    for(int i=0;i<photographerModel.getAPimgurl().size();++i)
                        array.add(photographerModel.getAPimgurl().get(i));
                    temp.putStringArrayList(ShowBigimageActivity.LIST,array);
                    intent.putExtras(temp);
                    startActivity(intent);
                }

            });
        }else{
            gridView.setVisibility(View.GONE);
        }
        headimge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YuepaiInforActivity.this,OtherDisplayActivity.class);
                intent.putExtra("otherID", String.valueOf(photographerModel.getUserModel().getId()));
                startActivity(intent);
            }
        });
        TextView textView= (TextView) findViewById(R.id.info_leixing);
        switch (photographerModel.getAPgroup()){
            case 1:
                textView.setText("约拍类型 : 写真客片");
                break;
            case 2:
                textView.setText("约拍类型 : 记录随拍");
                break;
            case 3:
                textView.setText("约拍类型 : 练手互勉");
                break;
            case 4:
                textView.setText("约拍类型 : 活动跟拍");
                break;
            case 5:
                textView.setText("约拍类型 : 商业跟拍");
                break;
        }
        textView= (TextView) findViewById(R.id.info_part);
        textView.setText("面向地区 : 江苏南京");
        textView= (TextView) findViewById(R.id.info_price);
        switch (photographerModel.getAPpricetype()){
            case 1:
                textView.setText("费用说明 : 最多收费"+photographerModel.getAPprice());
                break;
            case 2:
                textView.setText("费用说明 : 相互勉励");
                break;
            case 3:
                textView.setText("约拍类型 : 价格商议");
                break;
            case 0:
                textView.setText("费用说明 : 希望收费"+photographerModel.getAPprice());
                break;
        }
        textView= (TextView) findViewById(R.id.info_time);
        textView.setText("时间要求 : "+photographerModel.getAPtime());
        textView= (TextView) findViewById(R.id.time);
        textView.setText(photographerModel.getAPcreatetime());
        textView= (TextView) findViewById(R.id.info_say);
        textView.setText(photographerModel.getAPcontent());
        textView= (TextView) findViewById(R.id.name);
        textView.setText(photographerModel.getUserModel().getNickName());
        upload= (Button) findViewById(R.id.info_upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ACache cache=ACache.get(YuepaiInforActivity.this);
                        LoginDataModel loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("type", String.valueOf(StatusCode.REQUEST_BAOMIN));
                        map.put("uid",loginDataModel.getUserModel().getId());
                        map.put("authkey",loginDataModel.getUserModel().getAuth_key());
                        map.put("apid", String.valueOf(photographerModel.getAPid()));
                        okHttpUtil.instance.post(YuepaiInforActivity.this, CommonUrl.joinYuepai,map,YuepaiInforActivity.this);
                        Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.joinYuepai,map));
                    }
                }).start();
            }
        });

        ImageButton backbotton= (ImageButton) findViewById(R.id.backbtn);
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        Message msg=Handler.obtainMessage();
        int code= Integer.parseInt(jsonObject.getString("code"));
        switch (code){
            case StatusCode.REQUEST_BAOMIN_DELAY:
                msg.what=StatusCode.REQUEST_BAOMIN_DELAY;
                break;
            case StatusCode.REQUEST_BAOMIN_DELETE:
                msg.what=StatusCode.REQUEST_BAOMIN_DELETE;
                break;
            case StatusCode.REQUEST_BAOMIN_DONE:
                msg.what=StatusCode.REQUEST_BAOMIN_DONE;
                break;
            case StatusCode.REQUEST_BAOMIN_ERROR:
                msg.what=StatusCode.REQUEST_BAOMIN_ERROR;
                break;
            case StatusCode.REQUEST_BAOMIN_SUCCESS:
                msg.what=StatusCode.REQUEST_BAOMIN_SUCCESS;
                break;
            default:
                msg.what=ERROR;
                break;
        }
        Handler.sendMessage(msg);
    }

    @Override
    public void onFailure(IOException e) {
        Message msg=Handler.obtainMessage();
        msg.what=ERROR;
        Handler.sendMessage(msg);
        Log.d(TAG, "onFailure: "+e);
    }
}
