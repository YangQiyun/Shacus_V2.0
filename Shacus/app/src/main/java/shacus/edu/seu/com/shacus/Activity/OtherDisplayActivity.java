package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.OtherDisplayManager;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;
import shacus.edu.seu.com.shacus.View.SquareImageView;

import static shacus.edu.seu.com.shacus.R.id.followerNum;
import static shacus.edu.seu.com.shacus.R.id.followingNum;
import static shacus.edu.seu.com.shacus.R.id.myYuepaiNum;
import static shacus.edu.seu.com.shacus.R.id.textData_UserAge;
import static shacus.edu.seu.com.shacus.R.id.textData_UserLocal;
import static shacus.edu.seu.com.shacus.R.id.textData_UserName;
import static shacus.edu.seu.com.shacus.R.id.textData_UserSign;

/**
 * Created by ljh on 2017/9/16.
 */

public class OtherDisplayActivity extends BaseActivity{
    LinearLayout button1;//yuepai
    LinearLayout button2;//guanzhu
    LinearLayout button3;//fensi
    UserModel dataModel;
    private LoginDataModel loginData;

    public ACache cache;
    private CircleImageView headimage;
    Button editinfo;
    ImageButton add_btn;
    ImageButton backbtn;
    TextView username;
    TextView age;
    TextView userlocal;
    TextView usersign;
    TextView myyuepainum;
    TextView followingnum;
    TextView followernum;

    String name = null;
    String sign = null;
    String local = null;
    String himage = null;
    String bimage = null;
    String sex = null;
    int following = 0;
    int follower = 0;
    int yuepai = 0;
    int GRZP = 1000;
    int GRZP_NUM = 0;
    int ZPJ_NUM=0;
    LinearLayout grzp;
    LinearLayout zpj;
    SquareImageView zpj_i4;
    SquareImageView zpj_i2;
    SquareImageView zpj_i1;
    SquareImageView grzp_i4;
    SquareImageView grzp_i2;
    SquareImageView grzp_i1;
    TextView grzp_n;
    TextView grzp_im;
    TextView zpj_n;
    TextView zpj_im;
    private ArrayList<String> ZPJ_URL = new ArrayList<>();
    private ArrayList<String> GRZP_URL= new ArrayList();
    private ArrayList<Integer> ZPJ_ID= new ArrayList();
    private ArrayList<String> ZPJ_TITLE= new ArrayList();
    Map<String, String> userInfo;
    private String otherID=Integer.toString(1);
private OtherDisplayManager DataManager;
    private Handler myHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case StatusCode.RECIEVE_VISIT_SUCCESS:
                    Log.d("CCCCCCCCCCCCC", "连到RECIEVE_VISIT_SUCCESS:");
                    freshUserInfo();
                    break;
                case StatusCode.RECIEVE_VISIT_REJECT:
                    // CommonUtils.getUtilInstance().showToast(APP.context, "加载失败");
                    break;
                case 1000:
                    Log.d("CCCCCCCCCCCCC", "连到个人照片handler");
                    initGRZPView();
                    break;
                case 2000:
                    Log.d("CCCCCCCCCCCCC", "连到作品集封面handler");
                    initZPJView();
                    break;
                case 123:
                    Log.d("CCCCCCCCCCCCC", "handler");
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_display);
        Intent intent1 = getIntent();
        // 用intent1.getStringExtra()来得到上一个ACTIVITY发过来的字符串。
        this.otherID = intent1.getStringExtra("otherID");
        initView();
        //initListener();
        DataManager=new OtherDisplayManager(this,myHandler,otherID);
        initReq();
        initGRZP();
        initZPJ();

        //从缓存中取出数据
    }
    private void initView(){
        button1 = (LinearLayout) findViewById(R.id.myYuepiaBtn);
        button2 = (LinearLayout) findViewById(R.id.followingBtn);
        button3 = (LinearLayout) findViewById(R.id.followerBtn);
        headimage=(CircleImageView)findViewById(R.id.imageData_UserImage
        );
        editinfo=(Button)findViewById(R.id.edit_info);
        editinfo.setText("私信");

        add_btn=(ImageButton)findViewById(R.id.add_btn);
        add_btn.setVisibility(View.INVISIBLE);
         backbtn=(ImageButton)findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new BackButton());

        username=(TextView)findViewById(textData_UserName);
        age=(TextView)findViewById(textData_UserAge);
        userlocal=(TextView)findViewById(textData_UserLocal);
        usersign=(TextView)findViewById(textData_UserSign);
        myyuepainum=(TextView)findViewById(myYuepaiNum);
        followingnum=(TextView)findViewById(followingNum);
        followernum=(TextView)findViewById(followerNum);
        grzp = (LinearLayout) findViewById(R.id.grzp);
        zpj = (LinearLayout) findViewById(R.id.zpj);
        grzp_i1 = (SquareImageView) findViewById(R.id.grzp_i1);
        grzp_i2 = (SquareImageView) findViewById(R.id.grzp_i2);
//        grzp_i3 = (SquareImageView) findViewById(R.id.grzp_i3);
        grzp_i4 = (SquareImageView) findViewById(R.id.grzp_i4);
        grzp_im = (TextView) findViewById(R.id.grzp_im);
        grzp_n = (TextView) findViewById(R.id.grzp_n);

        zpj_i1 = (SquareImageView) findViewById(R.id.zpj_i1);
        zpj_i2 = (SquareImageView) findViewById(R.id.zpj_i2);
//        zpj_i3 = (ImageView) findViewById(R.id.zpj_i3);
        zpj_i4 = (SquareImageView) findViewById(R.id.zpj_i4);
        zpj_im = (TextView) findViewById(R.id.zpj_im);
        zpj_n = (TextView) findViewById(R.id.zpj_n);
    }
    private void initReq() {

        DataManager.showUserInfo();
    }

    private void initGRZP() {
        DataManager.showGRZP();
    }


    private void initZPJ() {
        DataManager.showZPJ();
    }
    private void freshUserInfo(){
        userInfo=DataManager.getUserInfo();
        Log.d("followingnum",Integer.toString(following) );
        followingnum.setText(userInfo.get("following"));
        followernum.setText(userInfo.get("follower"));
        myyuepainum.setText(userInfo.get("yuepaiNum"));
        username.setText(userInfo.get("name"));
        userlocal.setText(userInfo.get("local"));
        usersign.setText(userInfo.get("sign"));
        himage=userInfo.get("himage");
        Glide.with(this)
                .load(himage)
                .into(headimage);

    }
    private void initGRZPView() {
        GRZP_NUM=DataManager.getGRZP_NUM();
        GRZP_URL=DataManager.getGRZP_URL();
        if(GRZP_NUM == 0){
            grzp_i1.setVisibility(View.GONE);
            grzp_i2.setVisibility(View.GONE);
//            grzp_i3.setVisibility(View.GONE);
            grzp_i4.setVisibility(View.GONE);
            grzp_im.setVisibility(View.GONE);
            grzp_n.setVisibility(View.VISIBLE);
        }else{
            if(GRZP_NUM <= 3){
                switch (GRZP_NUM){
                   /* case 4:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
                        grzp_i3.setVisibility(View.VISIBLE);
                        grzp_i4.setVisibility(View.VISIBLE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(2))
                                .into(grzp_i3);
                        Glide.with(APP.context)
                                .load(GRZP_URL.get(3))
                                .into(grzp_i4);
                        break;*/
                    case 3:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
//                        grzp_i3.setVisibility(View.VISIBLE);
                        grzp_i4.setVisibility(View.VISIBLE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(this)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        Glide.with(this)
                                .load(GRZP_URL.get(2))
                                .into(grzp_i4);
                        break;
                    case 2:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.VISIBLE);
//                        grzp_i3.setVisibility(View.INVISIBLE);
                        grzp_i4.setVisibility(View.GONE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(this)
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        break;
                    case 1:
                        grzp_i1.setVisibility(View.VISIBLE);
                        grzp_i2.setVisibility(View.GONE);
//                        grzp_i3.setVisibility(View.INVISIBLE);
                        grzp_i4.setVisibility(View.GONE);
                        grzp_im.setVisibility(View.INVISIBLE);
                        grzp_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        break;
                }
            }else {
                grzp_i1.setVisibility(View.VISIBLE);
                grzp_i2.setVisibility(View.VISIBLE);
//                grzp_i3.setVisibility(View.VISIBLE);
                grzp_i4.setVisibility(View.VISIBLE);
                grzp_im.setVisibility(View.VISIBLE);
                grzp_n.setVisibility(View.INVISIBLE);
                Glide.with(this)
                        .load(GRZP_URL.get(0))
                        .into(grzp_i1);
                Glide.with(this)
                        .load(GRZP_URL.get(1))
                        .into(grzp_i2);
                /*Glide.with(APP.context)
                        .load(GRZP_URL.get(2))
                        .into(grzp_i3);*/
                Glide.with(this)
                        .load(GRZP_URL.get(2))
                        .into(grzp_i4);
                int temp = GRZP_NUM - 3;
                grzp_im.setText("+" + String.valueOf(temp));
            }
        }
    }
    private void initZPJView(){
        ZPJ_NUM=DataManager.getZPJ_NUM();
        ZPJ_URL=DataManager.getZPJ_URL();
        ZPJ_ID=DataManager.getZPJ_ID();
        ZPJ_TITLE=DataManager.getZPJ_TITLE();

        if (ZPJ_NUM == 0){
            zpj_i1.setVisibility(View.GONE);
            zpj_i2.setVisibility(View.GONE);
//            zpj_i3.setVisibility(View.GONE);
            zpj_i4.setVisibility(View.GONE);
            zpj_im.setVisibility(View.GONE);
            zpj_n.setVisibility(View.VISIBLE);
        }else {
            if(ZPJ_NUM <= 3){
                switch (ZPJ_NUM){
                    /*case 4:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
                        zpj_i3.setVisibility(View.VISIBLE);
                        zpj_i4.setVisibility(View.VISIBLE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(2))
                                .into(zpj_i3);
                        Glide.with(APP.context)
                                .load(ZPJ_URL.get(3))
                                .into(zpj_i4);
                        break;*/
                    case 3:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
//                        zpj_i3.setVisibility(View.VISIBLE);
                        zpj_i4.setVisibility(View.VISIBLE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(this)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        Glide.with(this)
                                .load(ZPJ_URL.get(2))
                                .into(zpj_i4);
                        break;
                    case 2:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.VISIBLE);
//                        zpj_i3.setVisibility(View.INVISIBLE);
                        zpj_i4.setVisibility(View.GONE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(this)
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        break;
                    case 1:
                        zpj_i1.setVisibility(View.VISIBLE);
                        zpj_i2.setVisibility(View.GONE);
//                        zpj_i3.setVisibility(View.INVISIBLE);
                        zpj_i4.setVisibility(View.GONE);
                        zpj_im.setVisibility(View.INVISIBLE);
                        zpj_n.setVisibility(View.INVISIBLE);
                        Glide.with(this)
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        break;
                }
            }else{
                zpj_i1.setVisibility(View.VISIBLE);
                zpj_i2.setVisibility(View.VISIBLE);
//                zpj_i3.setVisibility(View.VISIBLE);
                zpj_i4.setVisibility(View.VISIBLE);
                zpj_im.setVisibility(View.VISIBLE);
                zpj_n.setVisibility(View.INVISIBLE);
                Glide.with(this)
                        .load(ZPJ_URL.get(0))
                        .into(zpj_i1);
                Glide.with(this)
                        .load(ZPJ_URL.get(1))
                        .into(zpj_i2);
                /*Glide.with(APP.context)
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i3);*/
                Glide.with(this)
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i4);
                int temp = ZPJ_NUM- 3;
                zpj_im.setText("+" + String.valueOf(temp));
            }
        }
    }

    class BackButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //返回上级界面
            finish();
        }
    }
}
