package shacus.edu.seu.com.shacus.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.RecommandModel;
import shacus.edu.seu.com.shacus.MyApplication;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.CommonUtils;
import shacus.edu.seu.com.shacus.Utils.StatusCode;


public class LoginActivity extends AppCompatActivity implements okHttpUtil_JsonResponse {

    private TextView username;
    private TextView password;
    private TextView forgotpassword;
    private TextView signup;
    private TextView verifycode;
    private Button btn_login;
    //    private Button btn_verifycode;
    private TextView btn_verifycode;
    private Handler mHandler;
    private ProgressDialog loginProgressDlg;
    private CountDownTimer timeCount;
    private String phone;
    private int pflag=0;//是否在guide里点击了注册按钮的flag
    private int eventFlag=1;//1为登录 2为忘记密码 3为验证注册 4为验证注册通过 5为提交注册通过 6为修改密码验证
    TranslateAnimation animationHide=new TranslateAnimation(Animation.RELATIVE_TO_SELF,
            0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f);
    TranslateAnimation animationShow=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(loginProgressDlg!=null)
            loginProgressDlg.dismiss();
        else
            finish();
    }

    View view1;
    View view2;
    View view111;
    String trueCODE;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginProgressDlg=new ProgressDialog(LoginActivity.this);
        loginProgressDlg.setTitle("shacus");
        loginProgressDlg.setMessage("处理中");
        loginProgressDlg.setIndeterminate(true);
        loginProgressDlg.setCancelable(false);
        Intent intent=getIntent();
        int method=intent.getIntExtra("method", StatusCode.STATUS_ERROR);
        switch (method){
            case StatusCode.STATUS_LOGIN:
                eventFlag=1;
                break;
            case StatusCode.STATUS_REGISTER:
                eventFlag=3;
                pflag=1;
                break;
        }

        TextView direct_enter= (TextView) findViewById(R.id.suibiankan);
        direct_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginProgressDlg.isShowing())
                loginProgressDlg.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,String> map = new HashMap();
                        map.put("phone", "15151839690");
                        map.put("password", "000000");
                        map.put("askCode", String.valueOf(StatusCode.REQUEST_LOGIN));
                        okHttpUtil.instance.post(LoginActivity.this, CommonUrl.loginAccount,map,LoginActivity.this);
                    }
                }).start();
            }
        });
        username=(TextView)findViewById(R.id.login_username);
        password=(TextView)findViewById(R.id.login_password);
        verifycode=(TextView)findViewById(R.id.register_verifycode);
        btn_login=(Button)findViewById(R.id.btn_login);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view111 = findViewById(R.id.view111);

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Map<String,String> map = new HashMap();
                if (eventFlag==1) {
                    //检查输入格式，发弹窗请求到handler，并发网络请求
                    String usrnm = username.getText().toString();
                    String pwd = password.getText().toString();
                    if (!usrnm.equals("") && !pwd.equals("")&&judgeNameAndPwd(usrnm,pwd)){
                        map.put("phone", usrnm);
                        map.put("password", pwd);
                        map.put("askCode", String.valueOf(StatusCode.REQUEST_LOGIN));
                        loginProgressDlg.show();
                        okHttpUtil.instance.post(LoginActivity.this,CommonUrl.loginAccount,map,LoginActivity.this);
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this, "请规范用户名和密码：用户名为手机号，密码至少六位");
                        return;
                    }
                }
                //忘记密码
                if (eventFlag==2){
                    String name=username.getText().toString();
                    String code=verifycode.getText().toString();
                    trueCODE = code;
                    if (!code.equals("")&&!name.equals("")){
                        //验证验证码是否正确
                        map.put("phone",name);
                        phone=name;
                        map.put("code",code);
                        map.put("type", String.valueOf(StatusCode.REQUEST_FORGOTPW_YZ));
                        okHttpUtil.instance.post(LoginActivity.this, CommonUrl.forgotpw,map,LoginActivity
                        .this);
                        loginProgressDlg.show();
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名和验证码");
                        return;
                    }
                }

                if (eventFlag==3 ){
                    String name=username.getText().toString();
                    String code=verifycode.getText().toString();
                    if (!code.equals("")&&!name.equals("")){
                        eventFlag=4;
                        //验证验证码是否正确
                        map.put("phone",name);
                        phone=name;
                        map.put("code",code);
                        map.put("type", String.valueOf(StatusCode.REQUEST_REGISTER_VERIFYB));
                        okHttpUtil.instance.post(LoginActivity.this,CommonUrl.registerAccount,map,LoginActivity.this);
                        loginProgressDlg.show();
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名和验证码");
                        return;
                    }
                }
                if (eventFlag==4){
                    String nickName=username.getText().toString();
                    String password=verifycode.getText().toString();
                    if (!password.equals("")&&!nickName.equals("")&&!phone.equals("")&&judgeNameAndPwd(phone,password)){
                        eventFlag=5;
                        //提交注册信息
                        map.put("phone",phone);
                        map.put("nickName",nickName);
                        map.put("password",password);
                        map.put("type", String.valueOf(StatusCode.REQUEST_REGISTER));
                        okHttpUtil.instance.post(LoginActivity.this,CommonUrl.registerAccount,map,LoginActivity.this);
                        Log.d("loginActivity", "onClick: "+okHttpUtil.instance.pinjieurl(CommonUrl.registerAccount,map));
                        loginProgressDlg.show();
                        return;
                    }else {
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请完善信息");
                        return;
                    }
                }

                if(eventFlag==6){//修改密码
                    String password=verifycode.getText().toString();
                    if (!password.equals("")&&!phone.equals("")&&judgeNameAndPwd(phone,password)){
//                        eventFlag=5;
                        //提交注册信息
                        if(!trueCODE.equals("")){
                            map.put("phone",phone);
                            map.put("password",password);
                            map.put("type", String.valueOf(StatusCode.REQUEST_FORGOTPW_SET));
                            map.put("code",trueCODE);
                           okHttpUtil.instance.post(LoginActivity.this, CommonUrl.forgotpw,map,LoginActivity.this);
                            loginProgressDlg.show();
                        }
                        return;
                    }else {
                        if(password!=null&&password.length()<6)
                            CommonUtils.getUtilInstance().showToast(LoginActivity.this,"密码至少为六位");
                        else
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请完善信息");
                        return;
                    }
                }
            }
        });
        btn_verifycode=(TextView)findViewById(R.id.btn_verify_code);
        btn_verifycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map=new HashMap();//忘记密码
                if (eventFlag==3 || eventFlag == 2){
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    String usrnm=username.getText().toString();
                    if (!usrnm.equals("")&&eventFlag==3&&usrnm.length()==11)
                    {
                        map.put("phone",usrnm);
                        map.put("type",String.valueOf(StatusCode.REQUEST_REGISTER_VERIFYA));
                        okHttpUtil.instance.post(LoginActivity.this,CommonUrl.registerAccount,map,LoginActivity.this );
                        Log.d("login", "onClick: "+okHttpUtil.instance.pinjieurl(CommonUrl.registerAccount,map));
                        loginProgressDlg.show();
                        timeCount.start();
                    }else if(!usrnm.equals("")&&eventFlag==2){
                        map.put("phone",usrnm);
                        map.put("type",String.valueOf(StatusCode.REQUEST_FORGOTPW));
                        okHttpUtil.instance.post(LoginActivity.this,CommonUrl.forgotpw,map,LoginActivity.this);
                        loginProgressDlg.show();
                        timeCount.start();
                    }
                    else {
                        if(eventFlag==3)
                        CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请确认手机号是否正确");
                        else
                            CommonUtils.getUtilInstance().showToast(LoginActivity.this,"请输入用户名");
                    }
                }
            }
        });
//        ImageView loginbtnimg = (ImageView) findViewById(R.id.loginimgview);
//        loginbtnimg.bringToFront();
        forgotpassword=(TextView)findViewById(R.id.btn_forgot);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventFlag==2){
                    signup.setVisibility(View.VISIBLE);
                    eventFlag=1;
                    forgotpassword.setText("忘记密码");
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    btn_verifycode.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    password.setVisibility(View.VISIBLE);
                    btn_login.setText("  登   录");
                    return;
                }
                if(eventFlag!=2){
//                signup.setVisibility(View.INVISIBLE);
                    eventFlag=2;
                    animationHide.setDuration(500);
                    password.startAnimation(animationHide);
                    password.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    verifycode.startAnimation(animationShow);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    verifycode.setVisibility(View.VISIBLE);
                    btn_verifycode.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    forgotpassword.setText("去登陆");
                    btn_login.setText("验证账号");
                }
                return;
            }
        });
        signup=(TextView)findViewById(R.id.btn_newuser);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventFlag!=3){
                    eventFlag=3;
                    animationHide.setDuration(500);
                    password.startAnimation(animationHide);
                    password.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    verifycode.startAnimation(animationShow);
                    verifycode.setVisibility(View.VISIBLE);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    signup.setText("老用户登录");
//                forgotpassword.setVisibility(View.INVISIBLE);
                    btn_verifycode.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                    btn_login.setText("  注   册");

                    //下一步
                    return;
                }
                if(eventFlag==3){
                    eventFlag=1;
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    password.setVisibility(View.VISIBLE);
                    signup.setText("新用户注册");
                    btn_verifycode.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);

//                    forgotpassword.setVisibility(View.VISIBLE);
                    btn_login.setText("  登   录");
                    return;
                }
            }
        });
        //if (eventFlag==1)
        //  signup.performClick();
        timeCount=new CountDownTimer(120000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                //btn_verifycode.setBackgroundColor(getResources().getColor(R.drawable.shape_verifycode));
                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.shape_verifycode_clicked));
                btn_verifycode.setClickable(false);
                btn_verifycode.setText("重发 "+millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                btn_verifycode.setText("获取");
                //btn_verifycode.setBackgroundColor(getResources().getColor(R.color.gold));
//                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.shape_verifycode));
                btn_verifycode.setBackground(getResources().getDrawable(R.drawable.loginbtn));
                btn_verifycode.setClickable(true);
            }
        };
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.what==StatusCode.REQUEST_FAILURE){
                    loginProgressDlg.cancel();
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), (String)msg.obj);
                    return;
                }
                if (msg.what==StatusCode.RECIEVE_REGISTER_SUCCESS){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "验证成功!请设置昵称和密码!");
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    btn_verifycode.setVisibility(View.GONE);
                    username.setText("");
                    verifycode.setText("");
                    username.setHint("请指定昵称");
                    verifycode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    verifycode.setHint("请设置密码");
                    btn_login.setText("  注   册");
                }
                if (msg.what == 10008){
                    loginProgressDlg.cancel();
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "该昵称已被使用");
                }
                if (msg.what == 20010){
                  //  loginProgressDlg.cancel();//进度条取消
                    //临时加入初始化模特列表
                    ACache cache=ACache.get(LoginActivity.this);
                    LoginDataModel loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
                    Map<String,String> map=new HashMap<>();
                    map.put("type", String.valueOf(StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST));
                    map.put("authkey",loginDataModel.getUserModel().getAuth_key());
                    okHttpUtil.instance.post(LoginActivity.this, CommonUrl.requestModel,map,LoginActivity.this);
                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                    //intent.putExtra("result", 1);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                   // startActivity(intent);
                    Log.d("loginActivity", "handleMessage: 登录完成");
                    finish();
                }
                if (msg.what == 20015){
                  //  loginProgressDlg.cancel();//进度条取消
                    //临时加入初始化模特列表
                    ACache cache=ACache.get(LoginActivity.this);
                    LoginDataModel loginDataModel= (LoginDataModel) cache.getAsObject("loginModel");
                    Map<String,String> map=new HashMap<>();
                    map.put("type", String.valueOf(StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST));
                    map.put("authkey",loginDataModel.getUserModel().getAuth_key());
                    okHttpUtil.instance.post(LoginActivity.this, CommonUrl.requestModel,map,LoginActivity.this);
                   // Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                    //startActivity(intent);
                   // finish();
                }
                if (msg.what == 20013){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "验证码短信已发送");
                }
                if (msg.what == 20017){
                    loginProgressDlg.dismiss();//进度条取消
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "验证码短信已发送");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "服务器错误");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_NONE){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "该手机号尚未注册");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_YZ){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "验证成功!请重新设置密码!");
                    eventFlag = 6;
                    view1.setVisibility(View.VISIBLE);
                    view111.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    btn_verifycode.setVisibility(View.GONE);
                    username.setText("");
                    verifycode.setText("");
                    username.setVisibility(View.GONE);
                    verifycode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    verifycode.setHint("请设置密码");
                    btn_login.setText("  修改密码");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_YZ_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "验证码验证失败");
                    trueCODE = "";
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_SET){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "修改密码成功，去登陆吧~");
                    eventFlag = 1;
                    animationHide.setDuration(500);
                    verifycode.startAnimation(animationHide);
                    verifycode.setVisibility(View.GONE);
                    animationShow.setDuration(500);
                    password.startAnimation(animationShow);
                    password.setVisibility(View.VISIBLE);
                    password.setHint("密码");
                    password.setText("");
                    username.setText("");
                    username.setVisibility(View.VISIBLE);
                    username.setHint("手机号");
                    view111.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    signup.setText("新用户注册");
                    forgotpassword.setText("忘记密码");
                    btn_login.setText("  登   录");
                }
                if (msg.what == StatusCode.REQUEST_FORGOTPW_SET_ERROR){
                    loginProgressDlg.cancel();//进度条取消
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "修改密码失败，请重试");
                }
                if(msg.what==998){//获取模特列表
                    if(loginProgressDlg!=null)
                    loginProgressDlg.cancel();//进度条取消
                     Intent intent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);;
                    startActivity(intent);
                     finish();
                }
                if(msg.what==100005){
                    CommonUtils.getUtilInstance().showToast(MyApplication.getContext(), "该手机号已被注册过，请直接登录");
                }
            }
        };

        if (pflag==1){
            eventFlag=666;
            signup.performClick();
        }
    }

    private boolean judgeNameAndPwd(String usrnm, String pwd) {
        for (int i = usrnm.length();--i>=0;){
            if (!Character.isDigit(usrnm.charAt(i))){
                return false;
            }
        }
        return !(usrnm.length() != 11 || pwd.length() < 6);
    }


    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
            int code = Integer.valueOf(jsonObject.getString("code"));
        Log.d("LoginActivity", "onResponse: "+code);
            Message msg;
            Gson gson;
        LoginDataModel loginModel;
        ACache cache;
            switch (code){
                case StatusCode.REQUEST_LOGIN_SUCCESS:
                    gson=new Gson();
                    Log.d("ssssssssssssssssss",jsonObject.getJSONArray("contents").getJSONObject(0).toString());
                    loginModel=gson.fromJson(jsonObject.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                    cache=ACache.get(LoginActivity.this);
                    cache.put("loginModel",loginModel,ACache.TIME_WEEK*2);
                    msg=mHandler.obtainMessage();
                    msg.what= 20010;
                    mHandler.sendMessage(msg);
                    break;
                case 10113:
                case 10114:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FAILURE;
                    msg.obj=jsonObject.getString("contents");
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW_ERROR:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_ERROR;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW_NONE:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_NONE;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW_YZ:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_YZ;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW_YZ_ERROR:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_YZ_ERROR;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.REQUEST_FORGOTPW_SET:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_SET;
                    mHandler.sendMessage(msg);
                    break;
                case  StatusCode.REQUEST_FORGOTPW_SET_ERROR:
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FORGOTPW_SET_ERROR;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.RECIEVE_REGISTER_SUCCESS:
                    if (eventFlag==5)
                    {
                        gson=new Gson();
                        loginModel = gson.fromJson(jsonObject.getJSONArray("contents").getJSONObject(0).toString(),LoginDataModel.class);
                        cache=ACache.get(LoginActivity.this);
                        cache.put("loginModel", loginModel, ACache.TIME_WEEK * 2);
                        msg=mHandler.obtainMessage();
                        msg.what= 20015;
                        mHandler.sendMessage(msg);
                        return;
                    }

                    if (eventFlag==3){
                        msg=mHandler.obtainMessage();
                        msg.what= 20013;
                        mHandler.sendMessage(msg);
                        return;
                    }

                    if (eventFlag==4) {

                        msg=new Message();
                        msg.what=StatusCode.RECIEVE_REGISTER_SUCCESS;
                        mHandler.sendMessage(msg);

                        return;
                    }

                    msg=new Message();
                    msg.what=20017;
                    mHandler.sendMessage(msg);
                    break;
                case 10008:
                    eventFlag=4;
                    msg=mHandler.obtainMessage();
                    msg.what= 10008;
                    mHandler.sendMessage(msg);
                    break;
                case StatusCode.RECOMMAND_PHOTOGRAPHER_MODEL_LIST:
                    RecommandModel model_2 = new RecommandModel();
                    Gson gson1 = new Gson();
                    JSONArray data = jsonObject.getJSONArray("contents");
                    ArrayList<RecommandModel> mOurList = new ArrayList<>();
                    for(int i = 0; i< data.length(); i++){
                        if(data.get(i)==null||data.get(i).toString().equals("null"))continue;
                        JSONObject info = data.getJSONObject(i);
                        model_2 = gson1.fromJson(info.toString(),RecommandModel.class);
                        mOurList.add(model_2);
                    }
                    //获得列表后先放入缓存
                    ACache cache1=ACache.get(MyApplication.getContext());
                    cache1.put("mOurList",mOurList,ACache.TIME_HOUR);
                    Message mess=mHandler.obtainMessage();
                    mess.what=998;
                    mHandler.sendMessage(mess);
                    break;
                case 100005://请求注册但是手机号验证过了
                    msg=mHandler.obtainMessage();
                    msg.what= 100005;
                    mHandler.sendMessage(msg);
                default:
                    if (eventFlag!=5)
                        eventFlag=3;
                    else
                        eventFlag=4;
                    msg=mHandler.obtainMessage();
                    msg.what= StatusCode.REQUEST_FAILURE;
                    msg.obj=jsonObject.getString("contents");
                    mHandler.sendMessage(msg);
                    break;

            }

    }

    @Override
    public void onFailure(IOException e) {
        Message msg=new Message();
        msg.what=StatusCode.REQUEST_FAILURE;
        msg.obj="网络请求失败";
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        if(loginProgressDlg!=null)
            loginProgressDlg.dismiss();
        super.onDestroy();
    }
}
