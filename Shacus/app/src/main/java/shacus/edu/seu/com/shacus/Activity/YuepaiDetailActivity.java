package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.GridviewAdapter;
import shacus.edu.seu.com.shacus.Adapter.SignUpAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.YuepaiInfoManager;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.CommonUtils;
import shacus.edu.seu.com.shacus.View.MyGridView;

import static shacus.edu.seu.com.shacus.swipecards.util.RetrofitHelper.context;

/**
 * Created by iris1211 on 2017/9/19.
 */

public class YuepaiDetailActivity extends BaseActivity implements okHttpUtil_JsonResponse {
    private int sponsor;//1我发起的；0我报名的
    private int state;
    private String userId;
    private String authkey;
    private String group;
    private String apid;
    private static final String GETYUPAI = "YUEPAIINFOMATION";
    private final int ERROR = 404;
    private PhotographerModel photographerModel;
    private LoginDataModel loginData;
    private UserModel userModel;
    private static final String TAG = "YuepaiInforActivity";
    private YuepaiInfoManager DataManager;
    private Button upload;
    private ListView signup_user;
    private List<UserModel> signup_userList = new ArrayList<>();
    private TextView signup_extras;
    private TextView yuepai_state;
    private LinearLayout signupUserLayout;


    private Handler mhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10257: //成功返回报名人列表
                {
                    Log.d("SSSSSSSSSSSSSSS","Handlerchenggongsdjsa1");

                    SignUpAdapter adapter = new SignUpAdapter(YuepaiDetailActivity.this, signup_userList);
                        signup_user.setAdapter(adapter);
                    break;
                }
                case 10264:
                    //报名列表为空
                    signup_extras.setVisibility(View.VISIBLE);
                    break;

                case  10920: //成功选择约拍对象
                {
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "已成功选择");
                    break;
                }
                case 10923://y用户已取消报名
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "用户已取消报名");
                    break;
                case 10924://该约拍不存在或已过期
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "该约拍不存在或已过期");
                    break;
                case 800038: //成功
                {
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "已成功取消约拍");
                    break;
                }
                case 800039:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "约拍已过期，不能取消");
                    break;


                case 109050:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "双方确认约拍成功");
                    yuepai_state.setText("已完成");
                    break;
                case 109051:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "确认成功,等待对方确认");
                    yuepai_state.setText("等待对方确认完成约拍");
                    break;
                case 109052:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "您已确认过，请勿重复确认");
                    break;
                case 109053:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "该约拍信息不存在");
                    break;
                case 109054:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "确认失败，请确认约拍状态");
                    break;
                case 109056:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "数据库修改错误");
                    break;
                case 109055:
                    CommonUtils.getUtilInstance().showToast(YuepaiDetailActivity.this, "未查到约拍信息");
                    break;
            }

        }
    };
public String getApId()
{
    return apid;
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuepai_detail);
        ACache cache = ACache.get(this);
        photographerModel = (PhotographerModel) cache.getAsObject("ypinfo");
        loginData = (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginData.getUserModel();
       // DataManager = new YuepaiInfoManager(photographerModel, mhandler);
        initView();
    }

    private void initView() {
        ImageView headimge = (ImageView) findViewById(R.id.headimge);
        Glide.with(YuepaiDetailActivity.this).load(photographerModel.getUserModel().getHeadImage()).into(headimge);
        MyGridView gridView = (MyGridView) findViewById(R.id.gridview);
        if (photographerModel.getAPimgurl().size() != 0) {
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
            gridView.setAdapter(new GridviewAdapter(this, photographerModel.getAPimgurl()));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(YuepaiDetailActivity.this, ShowBigimageActivity.class);
                    Bundle temp = new Bundle();
                    ArrayList<String> array = new ArrayList<String>();
                    for (int i = 0; i < photographerModel.getAPimgurl().size(); ++i)
                        array.add(photographerModel.getAPimgurl().get(i));
                    temp.putStringArrayList(ShowBigimageActivity.LIST, array);
                    intent.putExtras(temp);
                    startActivity(intent);
                }

            });
        } else {
            gridView.setVisibility(View.GONE);
        }
        headimge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YuepaiDetailActivity.this, OtherDisplayActivity.class);
                intent.putExtra("otherID", String.valueOf(photographerModel.getUserModel().getId()));
                startActivity(intent);
            }
        });
        TextView textView = (TextView) findViewById(R.id.info_leixing);
        switch (photographerModel.getAPgroup()) {
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
        textView = (TextView) findViewById(R.id.info_part);
        textView.setText("面向地区 : 江苏南京");
        textView = (TextView) findViewById(R.id.info_price);
        switch (photographerModel.getAPpricetype()) {
            case 1:
                textView.setText("费用说明 : 最多收费" + photographerModel.getAPprice());
                break;
            case 2:
                textView.setText("费用说明 : 相互勉励");
                break;
            case 3:
                textView.setText("约拍类型 : 价格商议");
                break;
            case 0:
                textView.setText("费用说明 : 希望收费" + photographerModel.getAPprice());
                break;
        }
        textView = (TextView) findViewById(R.id.info_time);
        textView.setText("时间要求 : " + photographerModel.getAPtime());
        textView = (TextView) findViewById(R.id.time);
        textView.setText(photographerModel.getAPcreatetime());
        textView = (TextView) findViewById(R.id.info_say);
        textView.setText(photographerModel.getAPcontent());
        textView = (TextView) findViewById(R.id.name);
        yuepai_state = (TextView) findViewById(R.id.info_STATE);
        signup_extras = (TextView) findViewById(R.id.yuepai_extra);
        signup_user = (ListView) findViewById(R.id.yuepai_listview);
        signupUserLayout = (LinearLayout) findViewById(R.id.apply_info);
        textView.setText(photographerModel.getUserModel().getNickName());

        ImageButton backbotton = (ImageButton) findViewById(R.id.backbtn);
        upload = (Button) findViewById(R.id.info_upload);
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String userId = userModel.getId();
        String authkey = userModel.getAuth_key();
        String group = Integer.toString(photographerModel.getAPgroup());
        String apid = Integer.toString(photographerModel.getAPid());
        initData();//model中获取初始数据
        checkSponsor();
        if (sponsor == 1)
            initView_sponsor();
        else
            initView_signup();

    }

    private void initData() {
        userId = userModel.getId();
        authkey = userModel.getAuth_key();
        group = Integer.toString(photographerModel.getAPgroup());
        apid = Integer.toString(photographerModel.getAPid());
        state = photographerModel.getAPstatus();
    }

    private void initView_sponsor()
    {
        switch (state) {
            case 0:
                yuepai_state.setText("报名中");
                upload.setVisibility(View.INVISIBLE);
                signup_extras.setVisibility(View.INVISIBLE);

                signup_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //点击进入其他用户主界面
                        Intent intent = new Intent(YuepaiDetailActivity.this, OtherDisplayActivity.class);
                        UserModel next = signup_userList.get(position);
                        String otherId = next.getId();
                        intent.putExtra("otherID", otherId);
                        startActivity(intent);

                    }
                });
                initSignUp();//网络请求报名者列表



                break;

            case 1:
                signupUserLayout.setVisibility(View.INVISIBLE);
                yuepai_state.setText("进行中");
                upload.setText("完成约拍");
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishYuepai();
                    }
                });
                break;
            case 2:
                signupUserLayout.setVisibility(View.INVISIBLE);
                yuepai_state.setText("已完成");
                upload.setVisibility(View.INVISIBLE);
                break;

        }

        //根据不同状态设置点击button的网络请求
                  /*  ACache cache=ACache.get(YuepaiDetailActivity.this);
                    LoginDataModel loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("type", String.valueOf(StatusCode.REQUEST_BAOMIN));
                    map.put("uid",loginDataModel.getUserModel().getId());
                    map.put("authkey",loginDataModel.getUserModel().getAuth_key());
                    map.put("apid", String.valueOf(photographerModel.getAPid()));
                    okHttpUtil.instance.post(YuepaiDetailActivity.this, CommonUrl.joinYuepai,map,YuepaiDetailActivity.this);
                    Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.joinYuepai,map));*/


    }


private void initView_signup(){
    switch (state)
    {
        case 0:
            signupUserLayout.setVisibility(View.INVISIBLE);
            yuepai_state.setText("报名中");
            upload.setText("取消该约拍");
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //发送取消约拍网络请求
                            cancelBaoming();
                        }
                    }).start();
                }
            });
            break;

        case 1:
            yuepai_state.setText("进行中");
            upload.setText("完成约拍");
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            finishYuepai();
                            //发送完成约拍网络请求
                     }
            });
            break;
        case 2:
            signupUserLayout.setVisibility(View.INVISIBLE);
            yuepai_state.setText("已完成");
            upload.setVisibility(View.INVISIBLE);
            break;

    }
}
private void checkSponsor(){
    //判断是不是约拍发起者
    int mid=Integer.parseInt(userModel.getId());
    int sponsorid=Integer.parseInt(photographerModel.getUserModel().getId());
  if(mid==sponsorid)
      sponsor=1;
    else
        sponsor=0;
}
private void  initSignUp(){
    new Thread(){
        @Override
        public void run() {
            super.run();
            Map<String, String> map = new HashMap<>();


        map.put("uid", userId);
        map.put("authkey", authkey);
        map.put("type", Integer.toString(10245) );
        map.put("apid", apid);
        // request.httpRequest(map1, CommonUrl.getFollowInfo);
        okHttpUtil.instance.post(context, CommonUrl.askYuepai,map,YuepaiDetailActivity.this);
            Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.askYuepai,map));
    }
        }.start();

}
private void finishYuepai(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            //发送完成约拍网络请求
            /*new AlertDialog.Builder(context).setTitle("完成约拍")
                    .setMessage("确定完成约拍吗？" +"\n此操作不可撤销！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){//确定按钮响应事件*/

                            Map<String,String> map=new HashMap<String, String>();
                            map.put("apid",apid);
                            map.put("authkey",authkey);
                            map.put("uid", userId);
                            map.put("type", Integer.toString(10905) );
                          //  dialog.dismiss();
                            okHttpUtil.instance.post(YuepaiDetailActivity.this, CommonUrl.getOrdersInfo,map,YuepaiDetailActivity.this);
                            Log.d(TAG, "run: "+okHttpUtil.instance.pinjieurl(CommonUrl.getOrdersInfo,map));
                  /*      }
                    }).setNegativeButton("不", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();*/
        }
    }).start();
}
private void cancelBaoming(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            Map<String,String> map=new HashMap<String, String>();
    map.put("authkey",userModel.getAuth_key());
    map.put("uid",userModel.getId());
    map.put("type",Integer.toString(80004));
    map.put("apid",apid);
    //request.httpRequest(map,CommonUrl.joinYuepai);
            okHttpUtil.instance.post(YuepaiDetailActivity.this, CommonUrl.joinYuepai,map,YuepaiDetailActivity.this);
        }
    }).start();
}
    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {

        int code= Integer.parseInt(jsonObject.getString("code"));
        Log.d("SSSSSSSSSSSSSSS",jsonObject.toString());
        Message msg = new Message();
        switch (code){
            case 10257: //成功返回报名人列表

          JSONArray content = jsonObject.getJSONArray("contents");
                for(int i = 0;i < content.length();i++){
                    JSONObject user = content.getJSONObject(i);
                    UserModel u = new UserModel();
                    u.setHeadImage(user.getString("uimage"));
                    String str = user.getString("usign");
                    if (str.length()>12){
                        str = str.substring(0,12) + "...";
                    }
                    u.setSign(str);
                    u.setId(user.getString("uid"));
                    u.setNickName(user.getString("ualais"));
                    if(user.getInt("uchoosed")==1) u.setIndex(true);
                    else u.setIndex(false);
                    signup_userList.add(u);
                }


                Message message=mhandler.obtainMessage();
                message.what = 10257;
                mhandler.sendMessage(message);
                Log.d("SSSSSSSSSSSSSSS","发送message");
                break;
            case 10920:
                //选择约拍对象成功
                Message message1=mhandler.obtainMessage();
                message1.what = 10920;
                mhandler.sendMessage(message1);

                break;
            case 10923:
                //用户已取消报名
                Message message4=mhandler.obtainMessage();
                message4.what = 10923;
                mhandler.sendMessage(message4);

                break;
            case 10924:
                //该约拍不存在或已过期
                Message message5=mhandler.obtainMessage();
                message5.what = 10924;
                mhandler.sendMessage(message5);

                break;


            case 109055:
                //未查到该约拍信息
                Message message3=mhandler.obtainMessage();
                message3.what = 109055;
                mhandler.sendMessage(message3);
                break;
            case 109053:
                //该约拍信息不存在

                Message message6=mhandler.obtainMessage();
                message6.what = 109055;
                mhandler.sendMessage(message6);
                break;
            case 109054:
                //确认失败，请确认约拍状态

                msg.what = 109054;
                mhandler.sendMessage(msg);
                break;
            case 109056:
                //数据库修改错误

                msg.what = 109056;
                mhandler.sendMessage(msg);
                break;


            case 109050:
                //双方都确认完成

                Message message2=mhandler.obtainMessage();
                message2.what = 109050;
                mhandler.sendMessage(message2);
                break;
            case 109051:
                //确认成功,等待对方确认
                msg=mhandler.obtainMessage();
                msg.what= 109051;
                break;
            case 109052:
                //您已确认过，请勿重复确认

                msg=mhandler.obtainMessage();
                msg.what= 109052;
                break;
            case 800038:
                //成功取消报名
                msg=mhandler.obtainMessage();
                msg.what= 800038;
                mhandler.sendMessage(msg);
      case 800039:
          //已过期，不能取消报名
                msg=mhandler.obtainMessage();
                msg.what=800039;
          mhandler.sendMessage(msg);
            case  10264:
                msg=mhandler.obtainMessage();
                msg.what= 10264;
                mhandler.sendMessage(msg);
            default:
                msg.what=ERROR;
                break;
        }
        mhandler.sendMessage(msg);
    }

    @Override
    public void onFailure(IOException e) {
        Message msg=mhandler.obtainMessage();
        msg.what=ERROR;
        mhandler.sendMessage(msg);
        Log.d(TAG, "onFailure: "+e);
    }
}
