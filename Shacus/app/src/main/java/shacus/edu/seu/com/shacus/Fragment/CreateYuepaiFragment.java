package shacus.edu.seu.com.shacus.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Adapter.ImagePagerAdapter;
import shacus.edu.seu.com.shacus.Adapter.PhotoViewAttacher;
import shacus.edu.seu.com.shacus.Adapter.UploadViewPager;
import shacus.edu.seu.com.shacus.Data.Manager.CreateYuepaiManager;
import shacus.edu.seu.com.shacus.MyApplication;
import shacus.edu.seu.com.shacus.Network.OnSingleTapDismissBigPhotoListener;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUtils;
import shacus.edu.seu.com.shacus.View.ImgAddGridView;

/**
 * Created by Mind on 2017/9/11.
 */
public class CreateYuepaiFragment extends BaseFragment implements View.OnClickListener,OnSingleTapDismissBigPhotoListener{
    private FrameLayout edit_photo_fullscreen_layout;
    private Button upload;
    private TextView take_picture,select_local_picture,position_in_total;
    private EditText time,price_edit,theme_desc_edit;
    private RelativeLayout edit_photo_outer_layout,display_big_image_layout,show_upload_pic_layout;
    private ImageView delete_image;
    private ImgAddGridView add_image_gridview;
    private ProgressDialog progressDlg;
    private Spinner leixing;
    private Spinner diqu;
    private Spinner jiage;
    private Animation get_photo_layout_in_from_down;
    private ImagePagerAdapter imagePagerAdapter;
    private String takePictureUrl;


    private CreateYuepaiManager DataManager;

    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2;
    private boolean isBigImageShow=false,isShowUploadPic=false,addPic=false,clearFormerUploadUrlList=true;
    private int YUEPAI_TYPE=1;//约拍的种类，0为约模特，1为约摄影师
    private int YUEPAI_LEIXING=0;//APgroup，1为写真客片，2为记录随拍，3为练手互勉，4为活动跟拍，5为商业跟拍
    private int YUEPAI_JIAGE=0;//APpricetag,0为希望收费，1为最多付费，2为希望互勉，3为价格商议
    private final int SAVE_THEME_IMAGE=8;
    private final int SHOW_TAKE_PICTURE=9;
    private final int SHOW_LOCAL_PICTURE=10;
    private final int UPLOAD_TAKE_PICTURE=5;
    private UploadViewPager image_viewpager;
    private ListView theme_listview;
    private  int picToAdd;

    private static final String TAG = "CreateYuepaiFragment";

    private int addPicCount=1,addTakePicCount=1,viewpagerPosition;
    private Intent intent;
    private Handler handler=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_TAKE_PICTURE://拍了照片后msg的处理
                    addPic=true;
                    DataManager.show_take_picture(clearFormerUploadUrlList,takePictureUrl);
                    clearFormerUploadUrlList=false;
                    DataManager.getImageAddGridViewAdapter().notifyDataSetChanged();
                    addPicCount++;
                    break;
                //在图库选中了本地的图
                case SHOW_LOCAL_PICTURE:
                    if(!DataManager.show_local_picture(intent,clearFormerUploadUrlList))
                        break;
                    addPic=true;
                    clearFormerUploadUrlList=false;
                    DataManager.getImageAddGridViewAdapter().notifyDataSetChanged();
                    addPicCount++;
                    break;
                case UPLOAD_TAKE_PICTURE:
                    Log.d(TAG, "handleMessage: "+"上传图片");
                    ArrayList<String> arr=(ArrayList<String>)msg.obj;
                    picToAdd=DataManager.getUploadImgUrlList().size();
                    if(picToAdd>0){
                        for(int i=0;i<picToAdd;i++ ){
                            UploadManager uploadmgr=new UploadManager();
                            File data=new File(DataManager.getUploadImgUrlList().get(i));
                            String key=DataManager.getImgList().get(i);
                            String token=arr.get(i);
                            //mProgress.setVisibility(View.VISIBLE);
                            progressDlg=ProgressDialog.show(getActivity(), "发布约拍", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    //取消了上传
                                }
                            });
                            progressDlg.setMax(101);
                            uploadmgr.put(data, key, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    //完成，发信息给业务服务器
                                    new Thread() {
                                        public void run() {
                                            Map<String, Object> map = new HashMap<>();
                                            picToAdd -= 1;
                                            if (picToAdd == 0) {
                                                Message msg = handler.obtainMessage();
                                                msg.obj = map;
                                                msg.what = SAVE_THEME_IMAGE;
                                                handler.sendMessage(msg);
                                            }//要上传的图片包装在msg后变成了消息发到handler
                                        }
                                    }.start();
                                }
                            }, new UploadOptions(null, null, false,
                                    new UpProgressHandler() {
                                        public void progress(String key, double percent) {
                                            //mProgress.setProgress((int)percent*100);
                                            progressDlg.setProgress((int) percent * 100);
                                        }
                                    }, null));
                        }
                    }
                    show_upload_pic_layout.setVisibility(View.VISIBLE);
                    isShowUploadPic=true;
                    break;
                case SAVE_THEME_IMAGE://响应第二次msg，将上传图片结果与真约拍信息反馈给业务服务器
                    progressDlg.dismiss();
                    DataManager.finalsecd(YUEPAI_TYPE,theme_desc_edit.getText().toString(),YUEPAI_JIAGE,price_edit.getText().toString(),time.getText().toString(),YUEPAI_LEIXING);
                    progressDlg=ProgressDialog.show(getActivity(), "发布约拍", "正在创建约拍", true, true, new DialogInterface.OnCancelListener(){
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了约拍
                        }
                    });
                    break;
                case 233:
                    Log.d(TAG, "handleMessage: 收不到吗？");
                    if (progressDlg!=null)
                        progressDlg.dismiss();
                    CommonUtils.getUtilInstance().showToast(mActivity,"上传成功");
                    //mActivity.finish();
                    break;
                case 9999:
                    if (progressDlg!=null)
                        progressDlg.dismiss();
                    CommonUtils.getUtilInstance().showToast(mActivity,"发布失败");
                    break;

            }
            return true;
        }
    });

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mRootView = inflater.inflate(R.layout.fragment_create_yuepai,container,false);

        DataManager=new CreateYuepaiManager(this,handler);
        initView();
        initData();

        return mRootView;
    }

    //加监听，等到view完全显示了再去做调整
    public void onViewCreated(final View view, Bundle saved) {
        super.onViewCreated(view, saved);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                ViewGroup.MarginLayoutParams big_picLayoutParam = (ViewGroup.MarginLayoutParams) display_big_image_layout.getLayoutParams();
                big_picLayoutParam.bottomMargin = upload.getHeight();
                display_big_image_layout.setLayoutParams(big_picLayoutParam);
            }
        });
    }

    void initView(){
        ImageDisplayFragment.showNetImg=false;
        edit_photo_fullscreen_layout=(FrameLayout)mRootView.findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        position_in_total=(TextView)mRootView.findViewById(R.id.position_in_total);
        edit_photo_outer_layout=(RelativeLayout)mRootView.findViewById(R.id.edit_photo_outer_layout);
        TextView cancelEditPhoto=(TextView)edit_photo_outer_layout.findViewById(R.id.cancel_upload);
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBigPhotoLayout();
            }
        });
        display_big_image_layout=(RelativeLayout)mRootView.findViewById(R.id.display_big_image_layout);
        show_upload_pic_layout=(RelativeLayout)mRootView.findViewById(R.id.show_upload_pic_layout);
        take_picture=(TextView)mRootView.findViewById(R.id.take_picture);
        price_edit=(EditText)mRootView.findViewById(R.id.theme_price_edit);
        theme_desc_edit= (EditText) mRootView.findViewById(R.id.theme_desc_edit);
        time = (EditText) mRootView.findViewById(R.id.yuepai_time);
        leixing = (Spinner) mRootView.findViewById(R.id.yuepai_leixing);
        jiage = (Spinner) mRootView.findViewById(R.id.yuepai_jiage);
        diqu = (Spinner) mRootView.findViewById(R.id.yuepai_diqu);
        select_local_picture=(TextView)mRootView.findViewById(R.id.select_local_picture);
        upload=(Button) mRootView.findViewById(R.id.upload);
        upload.setVisibility(View.VISIBLE);
        delete_image=(ImageView)mRootView.findViewById(R.id.delete_image);
        add_image_gridview=(ImgAddGridView)mRootView.findViewById(R.id.add_image_gridview);
        add_image_gridview.setExpanded(true);
        image_viewpager=(UploadViewPager)mRootView.findViewById(R.id.image_viewpager);
        theme_listview = (ListView) mRootView.findViewById(R.id.theme_listview);
    }

    void initData(){
        add_image_gridview.setAdapter(DataManager.getImageAddGridViewAdapter());
        //初始化几个下拉菜单
        ArrayAdapter<CharSequence> leixingadapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.yuepai_leixing,R.layout.spinner_yuepai);
        leixingadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leixing.setAdapter(leixingadapter);
        leixing.setOnItemSelectedListener(new leixingspinnerListener());

        ArrayAdapter<CharSequence> diquadapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.yuepai_diqu, R.layout.spinner_yuepai);
        diquadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diqu.setAdapter(diquadapter);
        diqu.setOnItemSelectedListener(new diquspinnerListener());

        ArrayAdapter<CharSequence> jiageadapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.yuepai_jiage,R.layout.spinner_yuepai);
        jiageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jiage.setAdapter(jiageadapter);
        jiage.setOnItemSelectedListener(new jiagespinnerListener());

        upload.setOnClickListener(this);
        delete_image.setOnClickListener(this);

        add_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //这里是添加图片的按钮的回调
                if (position == 0) {
                    if (addPicCount == 10) {
                        CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.no_more_than_9));
                    } else {
                        //点击添加图片
                        edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                        get_photo_layout_in_from_down = AnimationUtils.loadAnimation(getActivity(), R.anim.search_layout_in_from_down);
                        edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);
                    }
                } else {
                    //点击图片查看大图
                    showImageViewPager(position, DataManager.getPictureUrlList(), DataManager.getUploadImgUrlList(), "local", "upload");
                    viewpagerPosition = position - 1;
                }
            }
        });

        take_picture.setOnClickListener(this);
        select_local_picture.setOnClickListener(this);

    }

    public void hideBigPhotoLayout(){
        display_big_image_layout.setVisibility(View.GONE);
        edit_photo_fullscreen_layout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= MyApplication.photo_path+"picture_take_0"
                        +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent,TAKE_PICTURE);
                addTakePicCount++;
                break;
            case R.id.select_local_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent,LOCAL_PICTURE);
                break;
            case R.id.upload://第一次握手：按发表键后
                //检查用户是否登录

               if(!DataManager.judge_login()) {
                   CommonUtils.getUtilInstance().showToast(getActivity(), getString(R.string.publish_theme_after_login));
                   return;
               }
                try {
                    if (!checkInput())//检查用户输入
                    {
                        Log.i("input","input Error");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(!addPic){
                    if(clearFormerUploadUrlList){
                        if(DataManager.getUploadImgUrlList().size()>0){
                            DataManager.getUploadImgUrlList().clear();
                            clearFormerUploadUrlList=false;
                        }
                    }
                }

                if (addPic){
                    DataManager.saveThemeInfo(YUEPAI_TYPE);//发第一次请求
                }else {
                    if (DataManager.getUploadImgUrlList().size() < 1) {
                        CommonUtils.getUtilInstance().showLongToast(getActivity(), "您没有配上图片哦~");
//            return false;
                    }
                }

                if(!clearFormerUploadUrlList){
                    clearFormerUploadUrlList=true;
                }
                break;
            case R.id.delete_image://点击大图后可以删除掉的
                if(DataManager.getUploadImgUrlList().size()==0){
                    return;
                }
                DataManager.getUploadImgUrlList().remove(viewpagerPosition);
                imagePagerAdapter.changeList(DataManager.getUploadImgUrlList());
                imagePagerAdapter.notifyDataSetChanged();
                DataManager.getAddPictureList().remove(viewpagerPosition+1);
                DataManager.getImageAddGridViewAdapter().changeList(DataManager.getAddPictureList());
                DataManager.getImageAddGridViewAdapter().notifyDataSetChanged();
                position_in_total.setText((viewpagerPosition+1)+"/"+DataManager.getAddPictureList().size());
                if(DataManager.getUploadImgUrlList().size()==0){
                    display_big_image_layout.setVisibility(View.GONE);
                    isBigImageShow=false;
                }
                addPicCount--;
                break;
        }
    }

    //关闭activity时候需要关闭的东西
    public RelativeLayout getEdit_big_photo_layout(){
        return display_big_image_layout;
    }

    public FrameLayout getdisplay_big_img(){
        return edit_photo_fullscreen_layout;
    }



    //检查输入状态
    private boolean checkInput() throws java.text.ParseException {

        if (time.getText().toString().equals("")){
            CommonUtils.getUtilInstance().showLongToast(getActivity(),"请描述时间信息");
            return false;
        }
        if (time.getText().length()>25){
            CommonUtils.getUtilInstance().showLongToast(getActivity(),"时间描述信息太长");
            return false;
        }

        if (YUEPAI_JIAGE!= 2 && YUEPAI_JIAGE!= 3 && price_edit.getText().toString().equals("")){
            CommonUtils.getUtilInstance().showLongToast(getActivity(),"请填入具体费用");
            return false;
        }
        if (YUEPAI_JIAGE!= 2 && YUEPAI_JIAGE!= 3 &&price_edit.getText().length()>15){
            CommonUtils.getUtilInstance().showLongToast(getActivity(),"费用信息有误");
            return false;
        }

        if (theme_desc_edit.getText().length()<15){
            CommonUtils.getUtilInstance().showLongToast(getActivity(),"请输入15字以上的详细描述");
            return false;
        }

        return true;
    }

    // 下面函数是选中了要上传图片或者拍照打完钩后运行的
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(SHOW_TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=intent;
            handler.sendEmptyMessage(SHOW_LOCAL_PICTURE);
        }
    }

    public void showImageViewPager(int position,
                                   final List<String> pictureUrlList, final List<String>localUrlList,
                                   final String flag, final String str){
        List<String>urlList;
        if(flag.equals("net")){
            urlList=pictureUrlList;
        }else{
            urlList=localUrlList;
        }
        display_big_image_layout.setVisibility(View.VISIBLE);
        imagePagerAdapter=new ImagePagerAdapter(getActivity().getSupportFragmentManager(),urlList);
        image_viewpager.setAdapter(imagePagerAdapter);
        imagePagerAdapter.notifyDataSetChanged();
        image_viewpager.setOffscreenPageLimit(imagePagerAdapter.getCount());
        image_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {

                viewpagerPosition = position;
                if (flag.equals("net")) {
                    position_in_total.setText((position + 1) + "/" + pictureUrlList.size());
                } else {
                    position_in_total.setText((position + 1) + "/" + localUrlList.size());
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }
        });
        if(str.equals("display")){
            image_viewpager.setCurrentItem(position);
            delete_image.setVisibility(View.GONE);
            position_in_total.setText((position+1)+"/"+urlList.size());
            isBigImageShow=true;

        }else{
            image_viewpager.setCurrentItem(position-1);
            delete_image.setVisibility(View.VISIBLE);
            position_in_total.setText((position)+"/"+urlList.size());
            isBigImageShow=true;

        }
        PhotoViewAttacher.setOnSingleTapToPhotoViewListener(this);
    }
    public void setYUEPAI_TYPE(int YUEPAI_TYPE) {
        this.YUEPAI_TYPE = YUEPAI_TYPE;
    }

    @Override
    public void onDismissBigPhoto() {
        hideDisplayBigImageLayout();
    }

    private void hideDisplayBigImageLayout(){
        display_big_image_layout.setVisibility(View.GONE);
        isBigImageShow=false;
    }


    private  class leixingspinnerListener implements android.widget.AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            YUEPAI_LEIXING = position + 1;
            String selected = parent.getItemAtPosition(position).toString();
            Log.d("CCCCCCCCCCCC",selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class diquspinnerListener implements android.widget.AdapterView.OnItemSelectedListener{


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            Log.d("CCCCCCCCCCCC",selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class jiagespinnerListener implements android.widget.AdapterView.OnItemSelectedListener{


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            YUEPAI_JIAGE = position;
            if(position == 0 || position == 1){
                price_edit.setVisibility(View.VISIBLE);
            }else if(position == 2 || position == 3){
                price_edit.setVisibility(View.GONE);
            }
            String selected = parent.getItemAtPosition(position).toString();
            Log.d("CCCCCCCCCCCC",selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}
