package shacus.edu.seu.com.shacus.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import shacus.edu.seu.com.shacus.Activity.FollowActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.DemoContext;
import shacus.edu.seu.com.shacus.MyApplication;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Administrator on 2017/9/19.
 */

public class MessageFragment extends BaseFragment {

    private static final String TAG = "MessageFragment";
    private ImageButton btn_friends;
    private UserModel userModel;
    private String token;

    public static MessageFragment newInstance(int value){
        Bundle args = new Bundle();
        args.putInt(TAG,value);
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(args);
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_message,container,false);
        initView();

        ACache cache=ACache.get(getActivity());
        LoginDataModel loginmodel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginmodel.getUserModel();

        getToken();

        return  mRootView;
    }

    public void initView(){
        btn_friends=findViewById(R.id.btn_friends);

        btn_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                // Intent intent1 = new Intent(getActivity(), FollowActivity.class);
                intent1.putExtra("activity", "following");
                intent1.putExtra("user","myself");
                intent1.putExtra("isChat",1);
                intent1.setClass(getContext(), FollowActivity.class);

                startActivity(intent1);


            }
        });
    }


    private void getToken() {
        token = userModel.getChattoken();
        connect(token);
        SharedPreferences.Editor edit = DemoContext.getInstance().getSharedPreferences().edit();
        edit.putString("DEMO_TOKEN", token);
        edit.apply();

    }

    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {

        if (getActivity().getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getActivity().getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 MyApplication Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.e("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    Log.e("LoginActivity", "--onSuccess" + userid);
                    isReconnect();
                    /**
                     * 设置当前用户信息，
                     * @param userInfo 当前用户信息
                     */
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userModel.getId(), userModel.getNickName(), Uri.parse(userModel.getHeadImage())));
                    /**
                     * 设置消息体内是否携带用户信息。
                     * @param state 是否携带用户信息，true 携带，false 不携带。
                     */
                    RongIM.getInstance().setMessageAttachedUserInfo(true);

                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(userModel.getId(), userModel.getNickName(), Uri.parse(userModel.getHeadImage())));
//                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//
//                        @Override
//                        public UserInfo getUserInfo(String userId) {
//
//                            return findUserById(userId);//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
//                        }
//
//                    }, true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.e("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }


    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment() {

        ConversationListFragment fragment = (ConversationListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);
    }


    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect() {

        Intent intent = getActivity().getIntent();
        String token = null;

        if (DemoContext.getInstance() != null) {

            token = DemoContext.getInstance().getSharedPreferences().getString("DEMO_TOKEN", "default");
        }

        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment();
                }
            }
        }
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getActivity().getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getActivity().getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
//    private UserInfo findUserById(String userId){
//        ACache cache = ACache.get(getActivity());
//        UserModel userModel = (UserModel) cache.getAsObject("rongusermodel"+userId);
//        String defaultname="追影用户";
//        String defaultimage="http://oci8c6557.bkt.clouddn.com/user-default-image.jpg?imageView2/1/w/200/h/200&e=1505900651&token=yzAza_Cm87nXkh9IyFfpg7LL7qKJ097VK5IOpLj0:S6iMexrqPL-wUd1Lh60Vg81s4ws=";
//        Uri temp=Uri.parse(defaultimage);
//        if(userModel==null){
//            UserModel userModel1=new UserModel();
//            userModel1.setNickName("追影用户");
//            userModel1.setId(userId);
//            userModel1.setHeadImage(defaultimage);
//            cache.put("rongusermodel"+userId,userModel1);
//            return new UserInfo(userId,defaultname,temp);}
//        if(userModel.getHeadImage()!=null)
//         temp=Uri.parse(userModel.getHeadImage());
//        return new UserInfo(userId, userModel.getNickName(),temp);
//
//    }

}
