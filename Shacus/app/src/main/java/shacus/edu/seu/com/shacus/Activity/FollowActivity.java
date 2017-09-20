package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.FollowInfoManager;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;

/**
 * Created by ljh on 2017/9/6.
 */

public class FollowActivity extends BaseActivity{

    private String type = null;
    private String index = null;
    private ImageButton backbtn;
    private int isChat=0;
    private ListView followListview;
    public ACache cache;

    private WeakRefHander fHandler=new WeakRefHander(new WeakRefHander.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatusCode.REQUEST_FOLLOWING_SUCCESS: //请求关注信息成功
                {//更新adapter
                    //更新界面
                   // followItemAdapter = new FollowItemAdapter(FollowActivity.this, userList);
                    followListview.setAdapter(followManager.getFollowingAdapter());
                    Log.d("aaaaaaaaaaaaaa", "following请求handler");
                    break;
                }
                case StatusCode.REQUEST_CANCEL_SUCCESS:  //请求取消关注成功
                {
                    cancel_success();
                    if (type.equals("follower"))followManager.initFollowingInfo();
                    else followManager.initFollowerInfo();


                    break;
                }
                case StatusCode.REQUEST_FOLLOW_SUCCESS: //请求关注成功
                {
                    follow_success();
                    followManager.initFollowingInfo();
                 //   CommonUtils.getUtilInstance().showToast(APP.context, "已关注");
                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_SUCCESS: //请求粉丝信息成功
                {
                   // followItemAdapter = new FollowItemAdapter(FollowActivity.this, userList);
                    followListview.setAdapter(followManager.getFollowerAdapter());
                    Log.d("aaaaaaaaaaaaaa", "粉丝请求handler");
                    break;
                }
                case StatusCode.REQUEST_USER_ILLEGAL: {
                   // CommonUtils.getUtilInstance().showToast(APP.context, "身份认证过期，请重新登陆");
                    break;
                }
                case StatusCode.REQUEST_FOLLOW_ERROR: {
                   // CommonUtils.getUtilInstance().showToast(APP.context, "出错啦~请重试");
                    break;
                }
                case StatusCode.REQUEST_FOLLOWING_NONE: {
                    textView.setText("暂无关注用户");
                    textView.setVisibility(View.VISIBLE);
                    break;
                }
                case StatusCode.REQUEST_FOLLOWER_NONE: {
                    textView.setText("暂无粉丝");
                    textView.setVisibility(View.VISIBLE);
                    break;
                }
                case 88: {
                    //CommonUtils.getUtilInstance().showToast(APP.context, "网络请求超时，请重试");
                    break;
                }

            }
            return true;
        }
    });
    private FollowInfoManager followManager;
    private TextView follow;
    Intent intent;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        followManager=new FollowInfoManager(this, fHandler);
        isChat = getIntent().getIntExtra("isChat",0);
        initView();
        //userList=initData();
        checkItemType();


    }
    private void cancel_success(){
        Toast.makeText(this, "已取消关注", Toast.LENGTH_SHORT).show();
    }
    private void follow_success(){
        Toast.makeText(this, "成功关注", Toast.LENGTH_SHORT).show();
    }
    private void initView(){
        backbtn = (ImageButton) findViewById(R.id.backbtn);

        follow = (TextView) findViewById(R.id.follow);
        followListview = (ListView) findViewById(R.id.follow_listview);

        textView = (TextView) findViewById(R.id.follow_extra);
        textView.setVisibility(View.INVISIBLE);

        backbtn.setOnClickListener(new BackButton());
        setListener();


        }
        private void setListener(){

        followListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入其他用户主界面

                if (followManager.checkUser(position,type)) {
                    if(isChat==0) {
                        Intent intent = new Intent(FollowActivity.this, OtherDisplayActivity.class);
                        intent.putExtra("otherID", followManager.getOtherId(position, type));
                        startActivity(intent);
                    }else if(isChat==1){
                        ACache cache = ACache.get(FollowActivity.this);
                        cache.put("rongusermodel"+followManager.getFollowingList().get(position).getId(),followManager.getFollowingList().get(position));
                        String chatId = followManager.getOtherId(position, type);
                        if (RongIM.getInstance() != null)
                            RongIM.getInstance().startPrivateChat(FollowActivity.this, chatId, "title");

                    }
                }
            }
        });
        }
    private void checkItemType() {
        //获得个人主界面点击的按钮名称 关注|粉丝
        intent = getIntent();
        type = intent.getStringExtra("activity");
        index = intent.getStringExtra("user");

        if (index.equals("myself")) {
            if (type.equals("following")) {
                follow.setText("我的关注");
                followManager.initFollowingInfo(); //初始化关注或粉丝数据

               // followItemAdapter = new FollowItemAdapter(FollowActivity.this,userList);
                followListview.setAdapter(followManager.getFollowingAdapter());

            } else if (type.equals("follower")) {
                follow.setText("我的粉丝");
                followManager.initFollowerInfo();
               // followItemAdapter = new FollowItemAdapter(FollowActivity.this,userList);
                followListview.setAdapter(followManager.getFollowerAdapter());
            }
        }
    }
    //在适配器中调用，确定单项布局

    public String getType() {
        return type;
    }

    //在适配器中，判断显示自己还是其他用户信息
    public String getIndex() {
        return index;
    }

    //在配适器中调用，用于关注和取消关注
    public FollowInfoManager getFollowManager(){
        return followManager;
    }
    class BackButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //返回上级界面
            finish();
        }
    }


/*




        }




    }



*/

    //返回一级我的个人主界面
   /*


*/




}
