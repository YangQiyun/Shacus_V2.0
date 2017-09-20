package shacus.edu.seu.com.shacus.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import shacus.edu.seu.com.shacus.Activity.EditInfoActivity;
import shacus.edu.seu.com.shacus.Activity.FollowActivity;
import shacus.edu.seu.com.shacus.Activity.MyupaiInforActivity;
import shacus.edu.seu.com.shacus.Activity.PhotosAddActivity;
import shacus.edu.seu.com.shacus.Activity.PhotoselfDetailActivity;
import shacus.edu.seu.com.shacus.Activity.PhotosetOverviewActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.SelfDiaplayManager;
import shacus.edu.seu.com.shacus.Data.Model.Images;
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
 * Created by ljh on 2017/9/5.
 */

public class SelfDisplayFragment  extends BaseFragment {
    LinearLayout button1;//yuepai
    LinearLayout button2;//guanzhu
    LinearLayout button3;//fensi
    UserModel dataModel;
    private LoginDataModel loginData;
    SelfDiaplayManager DataManager;
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

    ImageView btnsex;
    //NetRequest netRequest;



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




    public static SelfDisplayFragment newInstance(int value){
            Bundle args=new Bundle();
            args.putInt("key",value);
            SelfDisplayFragment fragment=new SelfDisplayFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mRootView=inflater.inflate(R.layout.fragment_selfdisplay,container,false);
           initView();
            initData();
            initListener();
            initReq();

            initGRZP();
            initZPJ();
            return  mRootView;
        }


    private void initData(){
        DataManager=new SelfDiaplayManager(this.getActivity(),myHandler);
        cache= ACache.get(getActivity());
        loginData= (LoginDataModel) cache.getAsObject("loginModel");
        dataModel=loginData.getUserModel();
        username.setText(dataModel.getNickName());
        sex=dataModel.getSex();

        if(1==Integer.parseInt(sex))
        {
            Glide.with(this)
                    .load(R.drawable.male).centerCrop()
//                .placeholder(R.drawable.holder)
                    //      .error(R.drawable.loading_error)
                    .into(btnsex);
    }

         else {
            Glide.with(this)
                    .load(R.drawable.female).centerCrop()
//                .placeholder(R.drawable.holder)
                    //      .error(R.drawable.loading_error)
                    .into(btnsex);
        }


        if(dataModel.getSign().equals("")){
            sign = "简介--暂无";
        }else {
            String temp = dataModel.getSign();
            sign = "简介--" + temp;
        }
        usersign.setText(sign);

        if(dataModel.getLocation().equals("")){
            local = "常住--暂无";
        }else {
            String temp = dataModel.getLocation();
            local = "常住--" + temp;
        }
        userlocal.setText(local);

     Glide.with(this)
             .load(dataModel.getHeadImage()).centerCrop()
//                .placeholder(R.drawable.holder)
                //      .error(R.drawable.loading_error)
             .into(getUserImage());
    }
        private void initView(){
            // TextView textView=findViewById(R.id.Tv_test);
            //textView.setText("这是第"+getArguments().get("key").toString()+"页");
            button1 = (LinearLayout) findViewById(R.id.myYuepiaBtn);
            button2 = (LinearLayout) findViewById(R.id.followingBtn);
            button3 = (LinearLayout) findViewById(R.id.followerBtn);
            headimage=(CircleImageView)findViewById(R.id.imageData_UserImage
            );
            editinfo=(Button)findViewById(R.id.edit_info);


            add_btn=(ImageButton)findViewById(R.id.add_btn);

              backbtn=(ImageButton)findViewById(R.id.backbtn);
            backbtn.setVisibility(View.INVISIBLE);
            btnsex=(ImageView)findViewById(R.id.sex);
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
        private void initListener(){
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    PopupMenu pop = new PopupMenu(getActivity(), arg0);
                    pop.getMenuInflater().inflate(R.menu.menu_add_photos, pop.getMenu());
                    pop.show();
                    pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem arg0) {
                            // TODO Auto-generated method stub
                            switch (arg0.getItemId()) {
                                case R.id.about:
                                    // Toast.makeText(getActivity(), "关于",10).show();
                                   /* Intent intent1 = new Intent();

                                    intent1.setClass(getContext(), PhotosAddActivity.class);


                                    startActivity(intent1);
                                    startActivityForResult(intent1, 1000);  */
                                    Intent intent3=new Intent(mActivity, PhotosAddActivity.class);
                                    intent3.putExtra("type",1);
                                    startActivity(intent3);
                                    break;
                                case R.id.exit:
                                    //finish();
                                    Intent intent=new Intent(mActivity, PhotosAddActivity.class);
                                    intent.putExtra("type",2);
                                    startActivity(intent);
                                    break;
                                case R.id.dongtai:
                                    Intent intent2=new Intent(mActivity, PhotosAddActivity.class);
                                    intent2.putExtra("type",5);
                                    startActivity(intent2);
                                    break;
                            }
                            return false;

                        }
                    });
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent3=new Intent(mActivity,MyupaiInforActivity.class);
                     startActivity(intent3);
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //他喜欢的
                    Intent intent1 = new Intent();
                    // Intent intent1 = new Intent(getActivity(), FollowActivity.class);
                    intent1.putExtra("activity", "following");
                    intent1.putExtra("user","myself");

                    intent1.setClass(getContext(), FollowActivity.class);

                   startActivity(intent1);

                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //他的粉丝
                    Intent intent2 = new Intent();
                    // Intent intent2 = new Intent(getActivity(), FollowActivity.class);
                    // intent1.setClass(APP.context, FollowActivity.class);
                    intent2.putExtra("activity", "follower");
                    intent2.putExtra("user","myself");
                    intent2.setClass(getContext(), FollowActivity.class);
                    getActivity().startActivityForResult(intent2, 1000);
                }
            });
            grzp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Images.imageUrls = (String[]) GRZP_URL.toArray(new String[0]);
                    Intent intent=new Intent(getActivity(),PhotoselfDetailActivity.class);
                    // intent.putExtra("uid",otherId);
                    getActivity().startActivityForResult(intent, 1000);
                }
            });


            zpj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), PhotosetOverviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("photoset_cover_url",ZPJ_URL);
                    bundle.putIntegerArrayList("phtotset_cover_uid",ZPJ_ID);
                    bundle.putStringArrayList("phtotset_cover_title",ZPJ_TITLE);
                    intent.putExtras(bundle);
                    //intent.putExtra("uid",otherId);
                    getActivity().startActivityForResult(intent, 1000);
                }
            });
            editinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //编辑个人资料

             Intent intent = new Intent(getActivity(),EditInfoActivity.class);
                    getActivity().startActivityForResult(intent, 1000);
                }
            });


        }

        private void initReq(){
            DataManager.showUserInfo();


        }
    private void initGRZP(){
        DataManager.showGRZP();
    }
    private void initZPJ(){
        DataManager.showZPJ();

    }

    private void freshUserInfo(){
        userInfo=DataManager.getUserInfo();
        followingnum.setText(userInfo.get("following"));
        followernum.setText(userInfo.get("follower"));
        myyuepainum.setText(userInfo.get("yuepaiNum"));
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
                        Glide.with(getActivity())
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(getActivity())
                                .load(GRZP_URL.get(1))
                                .into(grzp_i2);
                        Glide.with(getActivity())
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
                        Glide.with(getActivity())
                                .load(GRZP_URL.get(0))
                                .into(grzp_i1);
                        Glide.with(getActivity())
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
                        Glide.with(getActivity())
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
                Glide.with(getActivity())
                        .load(GRZP_URL.get(0))
                        .into(grzp_i1);
                Glide.with(getActivity())
                        .load(GRZP_URL.get(1))
                        .into(grzp_i2);
                /*Glide.with(APP.context)
                        .load(GRZP_URL.get(2))
                        .into(grzp_i3);*/
                Glide.with(getActivity())
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
                        Glide.with(getActivity())
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(getActivity())
                                .load(ZPJ_URL.get(1))
                                .into(zpj_i2);
                        Glide.with(getActivity())
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
                        Glide.with(getActivity())
                                .load(ZPJ_URL.get(0))
                                .into(zpj_i1);
                        Glide.with(getActivity())
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
                        Glide.with(getActivity())
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
                Glide.with(getActivity())
                        .load(ZPJ_URL.get(0))
                        .into(zpj_i1);
                Glide.with(getActivity())
                        .load(ZPJ_URL.get(1))
                        .into(zpj_i2);
                /*Glide.with(APP.context)
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i3);*/
                Glide.with(getActivity())
                        .load(ZPJ_URL.get(2))
                        .into(zpj_i4);
                int temp = ZPJ_NUM- 3;
                zpj_im.setText("+" + String.valueOf(temp));
            }
        }
    }
        private  void initUserModel(){
            UserModel testModel=new UserModel();
            testModel.setNickName("testModel");
            testModel.setSign("testSign");
            testModel.setSex("男");
            ACache.get(getActivity()).put("testModel",testModel);
        }
    public ImageView getUserImage() {
        return headimage;
    }
    public void onResume(){
        super.onResume();
       // findViewById
        initData();

        initReq();
        initGRZP();
        initZPJ();


      // btnsex=(ImageView)findViewById(sex);

        // netRequest=new NetRequest(SelfDisplay.this,SelfDisplay.this.getActivity());

        //button1.setOnClickListener

    }

}
