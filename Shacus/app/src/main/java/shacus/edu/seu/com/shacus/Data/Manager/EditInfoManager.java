package shacus.edu.seu.com.shacus.Data.Manager;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import shacus.edu.seu.com.shacus.Activity.EditInfoActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;

/**
 * Created by ljh on 2017/9/11.
 */

public class EditInfoManager implements StatusCode,okHttpUtil_JsonResponse {
    private ACache cache;
    private Context context;
    private LoginDataModel loginData;
    private UserModel userModel;
    private WeakRefHander mHandler;
    private EditInfoActivity e;
    public EditInfoManager(Context temp, WeakRefHander handler ){
        context=temp;
        mHandler=handler;
        cache=ACache.get(context);
        loginData= (LoginDataModel) cache.getAsObject("loginModel");
        userModel=loginData.getUserModel();
        e= (EditInfoActivity)context;

       // doRefresh();
    }
public UserModel getUserModel(){
    return userModel;
}
    public void changeUserName(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("user_nickname",e.getUserName());
                map.put("user_id",userModel.getId());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_NICKNAME));
              //  netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);
                Log.d("LQQQQQQQ", "user nickname");

            }
        }.start();

    }

    public void changePhoneNumber(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("user_phone",e.getUserPhoneNumber());
                map.put("user_id",userModel.getId());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_PHONENUMBER));
                //netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);
                Log.d("LQQQQQQQ", "Userphone");


            }
        }.start();

    }

    public void changeUserAddress(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("user_location",e.getUserAddress());
                map.put("user_id",userModel.getId());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_ADDRESS));
               // netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);

            }
        }.start();

    }

    public void changeEmail(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("user_mail",e.getUserEmail());
                map.put("user_id",userModel.getId());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_EMAIL));
               // netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);

            }
        }.start();

    }

    public void changeSign(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("authkey",userModel.getAuth_key());
                map.put("uid",userModel.getId());
                map.put("sign",e.getUserSign());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_SIGN));
                //netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);

            }
        }.start();

    }
    public void changeSex(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,String> map=new HashMap<>();
                map.put("authkey",userModel.getAuth_key());
                map.put("uid",userModel.getId());
                map.put("gender",e.getSex());
                map.put("type", Integer.toString(StatusCode.REQUEST_SETTING_CHANGE_SEX));
                //netRequest.httpRequest(map, CommonUrl.settingChangeNetUrl);
                okHttpUtil.instance.post(context, CommonUrl.settingChangeNetUrl,map,EditInfoManager.this);

            }
        }.start();

    }

    public void onResponse(JSONObject jsonObject) throws JSONException {

            String code=jsonObject.getString("code");
            Log.d("LQQQQQ", code);
            Log.d("LQQQQQ", jsonObject.getString("contents"));
            switch (code) {
                case "10503":
                    userModel.setNickName(e.getUserName());
                    break;
                case "10504":
                    Log.d("LQQQQQQQ", "name fail");

                    break;
                case "10505":
                  userModel.setPhone(e.getUserPhoneNumber());
                    break;
                case "10506":
                    Log.d("LQQQQQQQ", "num fail");
                    break;
                case "10507":
                   userModel.setLocation(e.getUserAddress());
                    break;
                case "10508":
                    Log.d("LQQQQQQQ", "adress fail");
                    break;
                case "10509":
                  userModel.setMailBox(e.getUserEmail());
                    break;
                case "10510":
                    Log.d("LQQQQQQQ", "email fail");
                    break;
                case "10518":
                    Log.d("LQQQQQQQ", "sign success");
                    userModel.setSign(e.getUserSign());
                    break;
                case "10519":
                    Log.d("LQQQQQQQ", "sex success");
                    if("男"==e.getSex())
                    userModel.setSex(Integer.toString(1));
                    else
                        userModel.setSex(Integer.toString(0));

                    break;
            }

           /* switch (code) {
                case "10514":
                   // CommonUtils.getUtilInstance().showToast(PersonalInfoEditActivity.this, "获取上传认证失败");
                    return;
                case "10515":
                    imagefinish=false;
                    JSONArray token = jsonObject.getJSONArray("contents");
                    upToken = token.getString(0);//成功获取口令
                    handler.sendEmptyMessage(UPLOAD_TAKE_PICTURE);
                    return;
                case "66666": //66666
                    progressDlg.dismiss();
                    String url = jsonObject.getString("contents");
                    dataModel.setHeadImage(url);
                    imagefinish=true;
                    break;
            }*/
            ACache cache=ACache.get(context);
            LoginDataModel model=(LoginDataModel)cache.getAsObject("loginModel");
            model.setUserModel(userModel);
            cache.put("loginModel", model);

        /*if(imagefinish)
            finish();
        else
            return;*/
    }
    @Override
    public void onFailure(IOException e) {
        //Log.d(TAG, "onFailure: "+e);

    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==NONE)
            return;
        //耗时操作传到handler里去处理
        if(requestCode==TAKE_PICTURE){
            handler.sendEmptyMessage(TAKE_PICTURE);
            return;
        }
        if(resultCode== Activity.RESULT_OK){
            this.intent=data;
            handler.sendEmptyMessage(LOCAL_PICTURE);
        }
    }*/

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
    public static String getDataColumn(Context context,Uri uri,String selection,
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
        final boolean isKitKat=Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT;
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




}
