package shacus.edu.seu.com.shacus.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Manager.EditInfoManager;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;
import shacus.edu.seu.com.shacus.View.CircleImageView;
import shacus.edu.seu.com.shacus.View.SquareImageView;

/**
 * Created by ljh on 2017/9/6.
 */
//ACACHE TESTMODEL
public class EditInfoActivity extends BaseActivity implements View.OnClickListener{
    private Bitmap head;// 头像Bitmap
    private static String path = "/sdcard/myHead/";// sd路径

    private RelativeLayout button1;
    //private RelativeLayout	button2;
    private RelativeLayout	button3;
    private RelativeLayout	button4;
    private RelativeLayout	button5;
    private RelativeLayout	button6;
    private RelativeLayout	button7;
    private RelativeLayout	button8;

    private CircleImageView imageData_UserImage;
    private SquareImageView edit_display_background;

    private EditText edit_textData_UserName;
    private TextView	edit_sex;
    private EditText	edit_textData_Local;
    private EditText	edit_textData_UserSign;
    private EditText	edit_email;
    private EditText	edit_phoneNumber;
    private ImageButton finish_btn;


    private String name = null;
    private String sign = null;
    private String local = null;
    private String sex = null;
    //ImageButton settingbtn;
    private ImageButton backbtn;
    UserModel dataModel;
    private LoginDataModel loginData;
    EditInfoManager DataManager;
    public ACache cache;
    private final int NONE=0,TAKE_PICTURE=1,LOCAL_PICTURE=2,UPLOAD_TAKE_PICTURE=4,SAVE_THEME_IMAGE=5;
    private WeakRefHander handler=new WeakRefHander(new WeakRefHander.Callback(){
         @Override
          public boolean handleMessage(Message msg) {
           /*  switch (msg.what){
                 case SAVE_THEME_IMAGE://第一次接到请求//请求更改头像
                     progressDlg.dismiss();
                     HashMap map=new HashMap();
                     map.put("type", StatusCode.REQUEST_SAVE_CHANGED_HEAD_IMAGE);
                     map.put("authkey",dataModel.getAuth_key());
                     ArrayList<String> temp=new ArrayList<>();
                     temp.add("\""+imageFileName+"\"");
                     map.put("image",temp);
                     map.put("uid",dataModel.getId());
                     netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                     progressDlg= ProgressDialog.show(PersonalInfoEditActivity.this, "上传头像", "正在保存头像", true, true, new DialogInterface.OnCancelListener() {
                         @Override
                         public void onCancel(DialogInterface dialogInterface) {
                             //上传完图片后取消了保存
                         }
                     });
                     break;
                 case UPLOAD_TAKE_PICTURE://第一次发送想要上传头像的请求
                     UploadManager uploadmgr=new UploadManager();
                     File data=new File(takePictureUrl);
                     String key=imageFileName;
                     String token=upToken;
                     //mProgress.setVisibility(View.VISIBLE);
                     progressDlg= ProgressDialog.show(PersonalInfoEditActivity.this, "上传头像", "正在上传图片", true, true, new DialogInterface.OnCancelListener() {
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
                         *//* map.put("themeId",themeId);
                        map.put("imgBody",UploadPhotoUtil.getInstance().getUploadBitmapZoomString(picUrl));
                        map.put("imgType",UploadPhotoUtil.getInstance().getFileType(picUrl));
                        map.put("type",1);*//*
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
                    break;
                case LOCAL_PICTURE:
                    Uri uri=intent.getData();
                    String photo_local_file_path=getPath_above19(PersonalInfoEditActivity.this, uri);
                    if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                            ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
                        CommonUtils.getUtilInstance().showToast(PersonalInfoEditActivity.this,"不支持此格式的上传");
                        break;
                    }
                    Bitmap bitmap2=UploadPhotoUtil.getInstance().trasformToZoomPhotoAndLessMemory(photo_local_file_path);
                    addPicture =new BitmapDrawable(getResources(), bitmap2);
                    takePictureUrl=photo_local_file_path;
                    userImage.setImageDrawable(addPicture);
                    headImageChanged=true;
                    break;
            }*/
                        return true;
        }
    });


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        initView();
        initListener();
        initData();
        //从缓存中取出数据
         DataManager=new EditInfoManager(this,handler);





        //test();

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
    public CircleImageView getUserImage() {
        return imageData_UserImage;
    }

/* public SquareImageView getBackGroundImage() {
        return edit_display_background;
    }*/
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
               /* if(headImageChanged){
                    HashMap map=new HashMap();
                    map.put("uid",dataModel.getId());
                    map.put("authkey",dataModel.getAuth_key());
                    String []ext=takePictureUrl.split("\\.");
                    imageFileName=dataModel.getPhone()+"/"+takePictureUrl.hashCode()+new Random(System.nanoTime()).toString()+ext[ext.length-1];
                    ArrayList<String> temp=new ArrayList<>();
                    temp.add("\""+imageFileName+"\"");
                    map.put("image",temp);
                    map.put("type", StatusCode.REQUEST_CHANGE_HEAD_IMAGE);
                    netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                    return;
                }*/
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setTitle("提示");
                dlg.setMessage("修改个人资料成功");
                dlg.setPositiveButton("确定",null);
                dlg.show();
                if(result.equals("00000")){finish();}
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
            case R.id.imageData_UserImage:// 更换头像
                showTypeDialog();
                break;
        }

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
    private void initView(){

        button1 = ( RelativeLayout) findViewById(R.id. button1);
        //button2 = ( RelativeLayout) findViewById(R.id. button2);
        button3 = ( RelativeLayout) findViewById(R.id. button3);
        button4 = ( RelativeLayout) findViewById(R.id. button4);
        button5 = ( RelativeLayout) findViewById(R.id. button5);
        button6 = ( RelativeLayout) findViewById(R.id. button6);
        imageData_UserImage = ( CircleImageView) findViewById(R.id. imageData_UserImage);
       // edit_display_background = (  SquareImageView) findViewById(R.id. edit_display_background);



       edit_textData_UserName=(EditText) findViewById(R.id. edit_textData_UserName);
        edit_sex=(TextView) findViewById(R.id. edit_sex);
       	edit_textData_Local=(EditText) findViewById(R.id. edit_textData_Local);
        edit_textData_UserSign=(EditText) findViewById(R.id. edit_textData_UserSign);
        edit_email=(EditText)findViewById(R.id.edit_email);
        edit_phoneNumber=(EditText)findViewById(R.id.edit_phoneNumber);
       // settingbtn=(ImageButton)findViewById(R.id.setting_btn);
        backbtn=(ImageButton)findViewById(R.id.backbtn);
        finish_btn=(ImageButton)findViewById(R.id.finishbtn);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            imageData_UserImage.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
    }
    private void initListener() {

        imageData_UserImage.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        finish_btn.setOnClickListener(this);
        button1.setOnClickListener(this);
        //button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        //button7.setOnClickListener(this);
       // button8.setOnClickListener(this);
    }
    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        imageData_UserImage.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


