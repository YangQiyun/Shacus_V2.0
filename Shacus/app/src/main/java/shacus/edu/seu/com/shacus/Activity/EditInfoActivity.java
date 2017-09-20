package shacus.edu.seu.com.shacus.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.EditInfoManager;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.MyApplication;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.CommonUtils;
import shacus.edu.seu.com.shacus.Utils.UploadPhotoUtil;
import shacus.edu.seu.com.shacus.View.CircleImageView;

import static shacus.edu.seu.com.shacus.R.id.imageData_UserImage;

/**
 * Created by ljh on 2017/9/6.
 */
//ACACHE TESTMODEL
public class EditInfoActivity extends BaseActivity implements StatusCode,View.OnClickListener,okHttpUtil_JsonResponse {

  //  private EditText userName,userSign,userAddress,userPhoneNumber,userEmail;
    private CircleImageView userImage;
    private FrameLayout edit_photo_fullscreen_layout;
    UserModel dataModel;
    //private NetRequest netRequest;
    private String takePictureUrl,upToken,imageFileName;
    private int addTakePicCount=1;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2,UPLOAD_TAKE_PICTURE=4,SAVE_THEME_IMAGE=5;
    private RelativeLayout edit_photo_outer_layout;
    private Intent intent;
    private boolean headImageChanged=false,imagefinish=true;
    private ProgressDialog progressDlg;
    private RelativeLayout button1;
    //private RelativeLayout   button2;
    private RelativeLayout button3;
    private RelativeLayout button4;
    private RelativeLayout button5;
    private RelativeLayout button6;
    private RelativeLayout button7;
    private RelativeLayout button8;
    private EditText edit_textData_UserName;
    private TextView   edit_sex;
    private EditText   edit_textData_Local;
    private EditText   edit_textData_UserSign;
    private EditText   edit_email;
    private EditText   edit_phoneNumber;
    private ImageButton finish_btn;
    private String name = null;
    private String sign = null;
    private String local = null;
    private String sex = null;
    //ImageButton settingbtn;
    private ImageButton backbtn;

    private LoginDataModel loginData;
    EditInfoManager DataManager;
    public ACache cache;
    TextView take_picture ;
    TextView select_local_picture ;
    TextView cancelEditPhoto;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAVE_THEME_IMAGE://第一次接到请求
                    progressDlg.dismiss();
                    Map<String,String> map=new HashMap<>();
                    map.put("type",Integer.toString(StatusCode.REQUEST_SAVE_CHANGED_HEAD_IMAGE) );
                    map.put("authkey",dataModel.getAuth_key());
                    List<String> temp1= new ArrayList<>();
                    temp1.add(String.valueOf("\""+imageFileName+"\""));
                    // finalImgList.add(String.valueOf("\""+filename+"\""));
                    map.put("image",temp1.toString());
                    map.put("uid",dataModel.getId());
                    okHttpUtil.instance.post(EditInfoActivity.this, CommonUrl.settingChangeNetUrl,map,EditInfoActivity.this);
                   // netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    progressDlg=ProgressDialog.show(EditInfoActivity.this, "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //上传完图片后取消了保存
                        }
                    });
                    Log.d("LQQQQQQQ", "正在保存头像");
                    break;
                case UPLOAD_TAKE_PICTURE://第一次发送请求
                    UploadManager uploadmgr=new UploadManager();
                    File data=new File(takePictureUrl);
                    String key=imageFileName;
                    String token=upToken;
                    //mProgress.setVisibility(View.VISIBLE);
                    progressDlg= ProgressDialog.show(EditInfoActivity.this, "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //取消了上传
                        }
                    });
                    progressDlg.setMax(101);
                    uploadmgr.put(data, key, token, new UpCompletionHandler(){
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            //完成，发信息给业务服务器
                            new Thread(){
                                public void run(){
                                    Map<String, Object> map=new HashMap<>();
                        /* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*/
                                    Message msg=handler.obtainMessage();
                                    msg.obj=map;
                                    msg.what=SAVE_THEME_IMAGE;
                                    handler.sendMessage(msg);//要上传的图片包装在msg后变成了消息发到handler
                                }
                            }.start();
                        }
                    },new UploadOptions(null, null, false,
                            new UpProgressHandler(){
                                public void progress(String key, double percent){
                                    //mProgress.setProgress((int)percent*100);
                                    progressDlg.setProgress((int)percent*100);
                                }
                            },null));

                    break;
                case TAKE_PICTURE:
                    //在这里处理，获取拍到的图

                    Bitmap bitmap= UploadPhotoUtil.getInstance()
                            .trasformToZoomPhotoAndLessMemory(takePictureUrl);
                    BitmapDrawable bd=new BitmapDrawable(getResources(),bitmap);
                    Drawable addPicture = bd;
                    userImage.setImageDrawable(addPicture);
                    headImageChanged=true;
                    Log.d("LQQQQQQQ", "拍照handler");
                    break;

                case LOCAL_PICTURE:
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(EditInfoActivity.this, uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(EditInfoActivity.this,"不支持此格式的上传");
                        break;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomPhotoAndLessMemory(photo_local_file_path);
                    addPicture =new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl=photo_local_file_path;
                    userImage.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        initView();
        initListener();
        initData();
        //从缓存中取出数据
        DataManager=new EditInfoManager(this,handler);
    }
    private void initView(){
        button1 = ( RelativeLayout) findViewById(R.id. button1);
        //button2 = ( RelativeLayout) findViewById(R.id. button2);
        button3 = ( RelativeLayout) findViewById(R.id. button3);
        button4 = ( RelativeLayout) findViewById(R.id. button4);
        button5 = ( RelativeLayout) findViewById(R.id. button5);
        button6 = ( RelativeLayout) findViewById(R.id. button6);
        userImage = ( CircleImageView) findViewById(imageData_UserImage);
        edit_textData_UserName=(EditText) findViewById(R.id. edit_textData_UserName);
        edit_sex=(TextView) findViewById(R.id. edit_sex);
        edit_textData_Local=(EditText) findViewById(R.id. edit_textData_Local);
        edit_textData_UserSign=(EditText) findViewById(R.id. edit_textData_UserSign);
        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_phoneNumber=(EditText)findViewById(R.id.edit_phoneNumber);
        // settingbtn=(ImageButton)findViewById(R.id.setting_btn);
        backbtn=(ImageButton)findViewById(R.id.backbtn);
        finish_btn=(ImageButton)findViewById(R.id.finishbtn);
        userImage= (CircleImageView) findViewById(imageData_UserImage);
         take_picture = (TextView) findViewById(R.id.take_picture);
         select_local_picture = (TextView) findViewById(R.id.select_local_picture);
         cancelEditPhoto=(TextView)findViewById(R.id.cancel_upload);
        edit_photo_fullscreen_layout=(FrameLayout)findViewById(R.id.edit_photo_fullscreen_layout);
        edit_photo_outer_layout=(RelativeLayout)findViewById(R.id.edit_photo_outer_layout);

    }
    private void initListener() {
        backbtn.setOnClickListener(this);
        finish_btn.setOnClickListener(this);
        button1.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        edit_photo_fullscreen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
        cancelEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {edit_photo_fullscreen_layout.setVisibility(View.GONE);
            }
        });
        userImage.setOnClickListener(this);
        take_picture.setOnClickListener(this);
        select_local_picture.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.finishbtn:
                //传入数据
                //验证数据是否为空，或者格式是否正确
                if(getUserName().equals("")) {
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(getUserSign().equals("")) {
                    Toast.makeText(this, "签名不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(getUserEmail());
                if(!matcher.matches()){
                    Toast.makeText(this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    break;
                }
                //检测文本内容是否有变化
                String result=checkChange();
                //有变化的向服务器提交修改数据请求
                if(result.charAt(1)=='1'){
                    DataManager.changeUserName();
                    Log.d("LQQQQQQQ", "name");
                }
                if(result.charAt(2)=='1'){
                    DataManager.changePhoneNumber();
                    Log.d("LQQQQQQQ", "phone");
                }
                if(result.charAt(3)=='1'){
                    DataManager.changeUserAddress();
                    Log.d("LQQQQQQQ", "Userlocation");
                }
                if(result.charAt(4)=='1'){
                    DataManager.changeEmail();
                    Log.d("LQQQQQQQ", "Usermail");
                }

                if(result.charAt(5)=='1'){
                    DataManager.changeSign();
                    Log.d("LQQQQQQQ", "sign");
                }
                DataManager.changeSex();
                //////////////////
                //头像更改
                /////////////////
                if(headImageChanged){
                    Map<String,String> map=new HashMap<>();
                    map.put("uid",dataModel.getId());
                    map.put("authkey",dataModel.getAuth_key());
                    String []ext=takePictureUrl.split("\\.");
                    imageFileName=dataModel.getPhone()+"/"+takePictureUrl.hashCode()+new Random(System.nanoTime()).toString()+ext[ext.length-1];
                    List<String> temp1= new ArrayList<>();
                    temp1.add(String.valueOf("\""+imageFileName+"\""));
                   // finalImgList.add(String.valueOf("\""+filename+"\""));
                    map.put("image",temp1.toString());
                    map.put("type", Integer.toString(StatusCode.REQUEST_CHANGE_HEAD_IMAGE) );
                    okHttpUtil.instance.post(EditInfoActivity.this, CommonUrl.settingChangeNetUrl,map,EditInfoActivity.this);
                    //netRequest.httpRequest(map,CommonUrl.settingChangeNetUrl);
                    Log.d("LQQQQQQQ", "修改头像https");
                    return;
                }

                AlertDialog.Builder dlg = new AlertDialog.Builder(EditInfoActivity.this);
                dlg.setTitle("提示");
                dlg.setMessage("修改个人资料成功");
                dlg.setPositiveButton("确定",null);
                dlg.show();

                if(result.equals("00000")){finish();}
                break;
            case imageData_UserImage:
                edit_photo_fullscreen_layout.setVisibility(View.VISIBLE);
                Animation get_photo_layout_in_from_down = AnimationUtils.loadAnimation(this, R.anim.search_layout_in_from_down);
                edit_photo_outer_layout.startAnimation(get_photo_layout_in_from_down);

                break;
            case R.id.take_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                takePictureUrl= MyApplication.photo_path+"picture_take_0"
                        +addTakePicCount+".jpg";
                File file=new File(takePictureUrl);
                intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent,TAKE_PICTURE);
                addTakePicCount++;
                Log.d("LQQQQQQQ", "点击拍照");
                break;
            case R.id.select_local_picture:
                edit_photo_fullscreen_layout.setVisibility(View.GONE);
                intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent,LOCAL_PICTURE);
                break;


            case R.id.button1:
                break;
            /*case R.id.button2:
                break;*/
            case R.id.button3:
                //昵称
            case R.id.button4:
                change_sex();
                //性别
                break;
            case R.id.button5:
                //地区
            case R.id.button6:
                //签名
                break;
            case R.id.backbtn:
                finish();
                break;

        }
        }
    private void initData(){
        cache= ACache.get(this);
        loginData= (LoginDataModel) cache.getAsObject("loginModel");
        dataModel=loginData.getUserModel();
        edit_textData_UserName.setText(dataModel.getNickName());
        edit_textData_UserSign.setText(dataModel.getSign());
        edit_textData_Local.setText(dataModel.getLocation());
        edit_email.setText(dataModel.getMailBox());
        edit_phoneNumber.setText(dataModel.getPhone());
        if(1==Integer.parseInt(dataModel.getSex()) )
            edit_sex.setText("男");
        else
            edit_sex.setText("女");
        Log.d("LQQQQQQQQQQQ", dataModel.getHeadImage());
        Glide.with(this)
                .load(dataModel.getHeadImage()).centerCrop()
//                .placeholder(R.drawable.holder)
                //      .error(R.drawable.loading_error)
                .into(getUserImage());
    }

    public String getUserName() {
        return edit_textData_UserName.getText().toString();
    }
    public String getUserSign(){return edit_textData_UserSign.getText().toString();}
    public String getUserEmail() {
        return edit_email.getText().toString();
    }
    public String getUserAddress() {
        return edit_textData_Local.getText().toString();
    }
    public String getSex() {
        return edit_sex.getText().toString();
    }
    public String getUserPhoneNumber() {
        return edit_phoneNumber.getText().toString();
    }



    //检测更改情况
    private String checkChange() {
        String result="";
        //头像更改
        //此处待改，保留
        result += "0";
        //其他更改
        if(!dataModel.getNickName().equals(String.valueOf(edit_textData_UserName.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getPhone().equals(String.valueOf(edit_phoneNumber.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getLocation().equals(String.valueOf(edit_textData_Local.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getMailBox().equals(String.valueOf(edit_email.getText()))){result+="1";}else{result+="0";}
        if(!dataModel.getSign().equals(String.valueOf(edit_textData_UserSign.getText()))){result+="1";}else{result+="0";}

        //刷新缓存
        Log.d("LQQQQQQQQQQQ", result);
        return result;
    }

    private void change_sex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //定义一个AlertDialog
        String[] strarr = {"男","女"};
        builder.setItems(strarr, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface arg0, int arg1)
            {
                String sex = "2";

                // 自动生成的方法存根
                if (arg1 == 0) {//男
                    sex = "1";
                    edit_sex.setText("男");
                }else {//女
                    sex = "0";
                    edit_sex.setText("女");
                }

                //DataManager.
                //Manager调用修改性别的函数
                //与服务器连接修改性别。
                //返回之后，fresh性别。
            }
        });
        builder.show();
    }


    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {


            String code=jsonObject.getString("code");
            Log.d("LQQQQQ", code);
            Log.d("LQQQQQ", jsonObject.getString("contents"));


            switch (code) {
                case "10514":
                    CommonUtils.getUtilInstance().showToast(EditInfoActivity.this, "获取上传认证失败");
                    return;
                case "10515":
                    imagefinish=false;
                    JSONArray token = jsonObject.getJSONArray("contents");
                    upToken = token.getString(0);//成功获取口令
                    handler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
                    Log.d("LQQQQQQQ", "成功获取口令");
                    return;
                case "66666": //66666
                    progressDlg.dismiss();
                    String url = jsonObject.getString("contents");
                    dataModel.setHeadImage(url);
                    imagefinish=true;
                    break;
            }
            ACache cache=ACache.get(EditInfoActivity.this);
            LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
            model.setUserModel(dataModel);
            cache.put("loginModel", model);

        if(imagefinish)
            finish();
        else
            return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(TAKE_PICTURE);
            Log.d("LQQQQQQQ", "拍照onActivityResult");
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=data;
            handler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }

    /*************
     */
    //是否为外置存储器
    public static boolean isExternalStorageDocument(Uri uri){
        return"com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri){
        return"com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri){
        return"com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.*/

    public static boolean isGooglePhotosUri(Uri uri){
        return"com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[]selectionArgs){
        Cursor cursor=null;
        final String column="_data";
        final String[]projection={column};
        try{
            cursor=context.getContentResolver().query(uri,projection,selection,selectionArgs, null);
            if(cursor!=null&&cursor.moveToFirst()){
                final int index=cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }finally{
            if(cursor!=null)
                cursor.close();
        }
        return null;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context,final Uri uri){
        final boolean isKitKat= Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if(isKitKat&& DocumentsContract.isDocumentUri(context, uri)){
            // ExternalStorageProvider
            if(isExternalStorageDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                if("primary".equalsIgnoreCase(type)){
                    return Environment.getExternalStorageDirectory()+"/"+split[1];
                }

            }
            // DownloadsProvider
            else if(isDownloadsDocument(uri)){
                final String id=DocumentsContract.getDocumentId(uri);
                final Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context,contentUri,null,null);
            }
            // MediaProvider
            else if(isMediaDocument(uri)){
                final String docId=DocumentsContract.getDocumentId(uri);
                final String[]split=docId.split(":");
                final String type=split[0];
                Uri contentUri=null;
                if("image".equals(type)){
                    contentUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if("video".equals(type)){
                    contentUri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if("audio".equals(type)){
                    contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection="_id=?";
                final String[]selectionArgs=new String[]{
                        split[1]
                };
                return getDataColumn(context,contentUri,selection,selectionArgs);
            }
        }
        // MediaStore (and general)
        else if("content".equalsIgnoreCase(uri.getScheme())){
            // Return the remote address
            if(isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context,uri,null,null);
        }
        // File
        else if("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }


    @Override
    public void onFailure(IOException e) {
        //Log.d(TAG, "onFailure: "+e);

    }

    public CircleImageView getUserImage() {
        return userImage;
    }

}