package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Adapter.ImageAddGridViewAdapter;
import shacus.edu.seu.com.shacus.Adapter.RemarkAdapter;
import shacus.edu.seu.com.shacus.Adapter.RvPicturesAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.ForumManager;
import shacus.edu.seu.com.shacus.Data.Model.ForumModel;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.RemarkModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;
import shacus.edu.seu.com.shacus.View.Custom.ImgAddGridView;

import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUESTT_ALLDONGTAI_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_ADD_COLLECT_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_ADD_COLLECT_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_ADD_FAVOR_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_ADD_FAVOR_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_CANCEL_COLLECT_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_CANCEL_COLLECT_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_CANCEL_FAVOR_ERROR;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_CANCEL_FAVOR_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_FORUM_DETAIL_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_SEND_REMARK_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.WANT_FORUM_DETAIL;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ForumActivity extends BaseActivity implements View.OnClickListener{


    UserModel userModel;

    private ForumModel forumModel;
    private ForumManager forumManager;

    private ImageButton btn_back;
    private TextView tv_theme_forum;
    private ImageButton btn_forum_addcollect;
    private int isCollect;
    private int isFavor;
    private CircleImageView headphoto;
    private TextView tv_forumer_name;
    private TextView tv_forum_time;
    private TextView tv_favor_remark_num;
    private TextView tv_forum_content;
    private TextView tv_noremark;

    private ImageButton btn_favor;
    private ImageButton btn_send;
    private EditText add_remark;

    private String remark_content;

    private int favor_num;
    private int remark_num;
    private int listid;

    private RecyclerView rv_picture;
    private RecyclerView rv_remark;
    private List<RemarkModel> rlist = new ArrayList<RemarkModel>();
    private RemarkAdapter remarkAdapter;
    private RvPicturesAdapter rvPicturesAdapter;

    private List<RemarkModel> remarklist = new ArrayList<RemarkModel>();

    private Handler mHandler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what){
                case REQUEST_FORUM_DETAIL_SUCCESS:
                    Log.e("handle","detail");
                    rlist= (List<RemarkModel>) msg.obj;
                    forumModel = (ForumModel) msg.getData().getSerializable("ForumModel");
                    setValues();
                    break;
                case REQUEST_SEND_REMARK_SUCCESS:
                    Log.e("handle sr","detail");
                    rlist.add(0,remarklist.get(0));
                    remark_num++;
                    refreshRemark();
                    break;
                case REQUEST_ADD_FAVOR_SUCCESS:
                    Log.e("handle af","detail");
                    isFavor=1;
                    btn_favor.setSelected(true);
                    favor_num++;
                    tv_favor_remark_num.setText(favor_num + "赞同·" + remark_num + "评论");
                    Toast.makeText(ForumActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_ADD_FAVOR_ERROR:
                    Log.e("handle afe","detail");
                    Toast.makeText(ForumActivity.this,"点赞失败",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CANCEL_FAVOR_SUCCESS:
                    Log.e("handle cf","detail");
                    isFavor=0;
                    btn_favor.setSelected(false);
                    favor_num--;
                    tv_favor_remark_num.setText(favor_num + "赞同·" + remark_num + "评论");
                    Toast.makeText(ForumActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CANCEL_FAVOR_ERROR:
                    Log.e("handle cfe","detail");
                    Toast.makeText(ForumActivity.this,"取消点赞失败",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_ADD_COLLECT_SUCCESS:
                    Log.e("handle ac","detail");
                    isCollect=1;
                    btn_forum_addcollect.setSelected(true);
                    Toast.makeText(ForumActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_ADD_COLLECT_ERROR:
                    Log.e("handle ace","detail");
                    Toast.makeText(ForumActivity.this,"收藏失败",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CANCEL_COLLECT_SUCCESS:
                    Log.e("handle cc","detail");
                    isCollect=0;
                    btn_forum_addcollect.setSelected(false);
                    Toast.makeText(ForumActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_CANCEL_COLLECT_ERROR:
                    Log.e("handle cce","detail");
                    Toast.makeText(ForumActivity.this,"取消收藏失败",Toast.LENGTH_SHORT).show();
                    break;
                case REQUESTT_ALLDONGTAI_ERROR:
                    Log.e("handle ae","detail");
                    Toast.makeText(ForumActivity.this,"用户认证失败",Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        ACache cache=ACache.get(ForumActivity.this);
        LoginDataModel loginmodel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel = loginmodel.getUserModel();
        Intent intent = this.getIntent();
        listid = intent.getIntExtra("listid",1);
        forumManager = new ForumManager(this,mHandler,listid);
        forumManager.onLoad();

        init();

        //pictures.setExpanded(true);
    }

    void init(){
        btn_back = (ImageButton) findViewById(R.id.btn_forum_back);
        tv_theme_forum = (TextView) findViewById(R.id.theme_forum);
        btn_forum_addcollect = (ImageButton) findViewById(R.id.btn_forum_addcollect);
        headphoto = (CircleImageView) findViewById(R.id.forum_publish_user_avatar);
        tv_forumer_name = (TextView) findViewById(R.id.forumer_name);
        tv_forum_time = (TextView) findViewById(R.id.forum_time);
        tv_favor_remark_num = (TextView) findViewById(R.id.favor_remark_num);
        tv_forum_content = (TextView) findViewById(R.id.tv_forum_content);
        btn_favor = (ImageButton) findViewById(R.id.btn_favor);
        btn_send = (ImageButton) findViewById(R.id.sendremark);
        add_remark = (EditText) findViewById(R.id.et_add_remark);
        rv_picture = (RecyclerView) findViewById(R.id.rv_pictures);
        rv_remark = (RecyclerView) findViewById(R.id.rv_remark);
        tv_noremark = (TextView) findViewById(R.id.noremark);

        btn_back.setOnClickListener(this);
        btn_forum_addcollect.setOnClickListener(this);
        btn_favor.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        headphoto.setOnClickListener(this);
    }

    void setValues() {
        tv_theme_forum.setText(forumModel.getCQtitle());
        tv_forum_content.setText(forumModel.getCQcontent());
        tv_forumer_name.setText(forumModel.getCQuname());

        //显示头像
        Glide.with(this)
                .load(forumModel.getCQuimurl())
                .into(headphoto);

        favor_num = forumModel.getCQlikedN();
        remark_num = forumModel.getCQcommentN();
        tv_favor_remark_num.setText(favor_num + "赞同·" + remark_num + "评论");

        String remarkTime = forumModel.getCQtime();
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;

        int ryear = Integer.parseInt(remarkTime.substring(0, 4));
        int rmonth = Integer.parseInt(remarkTime.substring(5, 7));
        int rdate = Integer.parseInt(remarkTime.substring(8, 10));
        int rhour = Integer.parseInt(remarkTime.substring(11, 13));
        int rminute = Integer.parseInt(remarkTime.substring(14, 16));

        if (year > ryear) {
            tv_forum_time.setText(ryear + "年");
        } else if (month > rmonth) {
            tv_forum_time.setText(ryear + "年" + rmonth + "月");
        } else if (date > rdate) {
            if (date - 1 == rdate)
                tv_forum_time.setText("昨天");
            else
                tv_forum_time.setText(rmonth + "月" + rdate + "日");
        } else if (hour > rhour) {
            tv_forum_time.setText(rhour + "·" + rminute);
        } else if (minute > rminute) {
            tv_forum_time.setText((minute - rminute) + "分钟前");
        } else {
            tv_forum_time.setText("刚刚");
        }

        new Thread(new Runnable() {
            List<String> picturesurl = forumModel.getCQimg();
            List<Drawable> addPictureList = new ArrayList<Drawable>();

            @Override
            public void run() {
                //post() is quite important,update pictures in UI main thread
                for (int i = 0; i < picturesurl.size(); i++) {
                    addPictureList.add(loadImageFromNetwork(picturesurl.get(i)));
                }
                rv_picture.post(new Runnable() {
                    @Override
                    public void run() {
                        //TODO Auto-generated method stub
                        rv_picture.setLayoutManager(new LinearLayoutManager(ForumActivity.this));
                        rvPicturesAdapter = new RvPicturesAdapter(ForumActivity.this,addPictureList);
                        rv_picture.addItemDecoration(new SpaceItemDecoration(5));
                        rv_picture.setAdapter(rvPicturesAdapter);
                    }
                });
            }

            //download image from network using @urladdress
            private Drawable loadImageFromNetwork(String urladdr) {
                Drawable drawable = null;
                try {
                    drawable = Drawable.createFromStream(new URL(urladdr).openStream(), "image.jpg");
                } catch (IOException e) {
                    Log.d("test", e.getMessage());
                }
                if (drawable == null) {
                    Log.d("test", "null drawable");
                } else {
                    Log.d("test", "not null drawable");
                }
                return drawable;
            }
        }).start();  //线程启动

        isCollect = forumModel.getCQuiscollect();
        if(isCollect==0){
            btn_forum_addcollect.setSelected(false);
        }else{
            btn_forum_addcollect.setSelected(true);
        }

        isFavor = forumModel.getCQuisfavor();
        if(isFavor==0){
            btn_favor.setSelected(false);
        }else{
            btn_favor.setSelected(true);
        }

        rv_remark.setLayoutManager(new LinearLayoutManager(this));
        remarkAdapter = new RemarkAdapter(this,rlist);
        rv_remark.addItemDecoration(new SpaceItemDecoration(5));
        rv_remark.setAdapter(remarkAdapter);

        if(rlist.isEmpty())
            tv_noremark.setVisibility(View.VISIBLE);


    }

    private void refreshRemark(){
        remarkAdapter.refresh(rlist);
        remarkAdapter.notifyDataSetChanged();
        if(rlist.isEmpty())
            tv_noremark.setVisibility(View.VISIBLE);
        else{
            tv_noremark.setVisibility(View.GONE);
        }
        tv_favor_remark_num.setText(favor_num + "赞同·" + remark_num + "评论");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_forum_back:
                finish();
                break;
            case R.id.btn_forum_addcollect:
                if(isCollect==0){
                    forumManager.addCollect();
                }else{
                    forumManager.cancelCollect();
                }
                break;
            case R.id.btn_favor:
                if(isFavor==0){
                    forumManager.addFavor();
                }else{
                    forumManager.cancelFavor();
                }
                break;
            case R.id.sendremark:
                RemarkModel remarkModel = new RemarkModel();
                remark_content = add_remark.getText().toString();
                remarkModel.setRMcmtuimg(userModel.getHeadImage());
                remarkModel.setRMcmtuname(userModel.getNickName());
                remarkModel.setRMcmtcontent(remark_content);
                add_remark.setText(null);
                Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
                t.setToNow(); // 取得系统时间。
                int year = t.year;
                int month = t.month+100;
                int date = t.monthDay+100;
                int hour = t.hour+100; // 0-23
                int minute = t.minute+100;
                String remarktime = year+""+month+""+date+""+hour+""+minute;
                remarkModel.setRMcmtT(remarktime);
                remarklist.add(0,remarkModel);
                forumManager.sendRemark(remark_content);
                break;
            case R.id.forum_publish_user_avatar:
                Intent intent = new Intent(this,OtherDisplayActivity.class);
                String id= String.valueOf(forumModel.getCQuid());
                intent.putExtra("otherID", id);
                startActivity(intent);
                break;

        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        /**
         * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
         * the number of pixels that the item view should be inset by, similar to padding or margin.
         * The default implementation sets the bounds of outRect to 0 and returns.
         * <p>
         * <p>
         * If this ItemDecoration does not affect the positioning of item views, it should set
         * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
         * before returning.
         * <p>
         * <p>
         * If you need to access Adapter for additional data, you can call
         * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
         * View.
         *
         * @param outRect Rect to receive the output.
         * @param view    The child view to decorate
         * @param parent  RecyclerView this ItemDecoration is decorating
         * @param state   The current state of RecyclerView.
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }
            outRect.set(mSpace,mSpace,mSpace,mSpace);
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }


}
