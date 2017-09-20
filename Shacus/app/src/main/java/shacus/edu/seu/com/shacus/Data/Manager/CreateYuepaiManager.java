package shacus.edu.seu.com.shacus.Data.Manager;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import shacus.edu.seu.com.shacus.Adapter.ImageAddGridViewAdapter;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.MyApplication;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_JsonResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.Utils.CommonUtils;
import shacus.edu.seu.com.shacus.Utils.UploadPhotoUtil;

/**
 * Created by Mind on 2017/9/11.
 */
public class CreateYuepaiManager implements okHttpUtil_JsonResponse {
    private static final String TAG = "CreateYuepaiManager";
    private List<Drawable> addPictureList=new ArrayList<>();   //图片 bigmap
    private List<String> uploadImgUrlList=new ArrayList<>();  //本地位置
    private List<String>pictureUrlList=new ArrayList<>();
    private ArrayList<String> finalImgList=new ArrayList<>();  //file name 加上/
    private ArrayList<String> imgList=new ArrayList<>();  //file name
    private ImageAddGridViewAdapter imageAddGridViewAdapter;
    private Fragment mfragment;
    ACache cache;
    private LoginDataModel loginDataModel;
    private UserModel userModel;
    private Handler handler;
    private int picToAdd;

    private final int UPLOAD_TAKE_PICTURE=5;


    public CreateYuepaiManager(Fragment fragment,Handler hander1){
        handler=hander1;
        mfragment=fragment;
        addPictureList.add(mfragment.getResources().getDrawable(R.mipmap.theme_add_picture_icon));
        imageAddGridViewAdapter=new ImageAddGridViewAdapter(mfragment.getActivity(), addPictureList);
        cache=ACache.get(fragment.getActivity());
        loginDataModel=(LoginDataModel)cache.getAsObject("loginModel");
        userModel=loginDataModel.getUserModel();
    }

    //发第二次请求，最终信息传给业务服务器
    public void finalsecd(int YUEPAI_TYPE,String content,int YUEPAI_JIAGE,String price,String time,int grup){
        Map<String,String> map=new HashMap<>();
        map.put("uid",userModel.getId());
        map.put("ap_type", String.valueOf(YUEPAI_TYPE));
        map.put("authkey", userModel.getAuth_key());
        map.put("type", String.valueOf(80001));
        map.put("contents",content);
        map.put("imgs", finalImgList.toString());
        map.put("pricetag",String.valueOf(YUEPAI_JIAGE));
        map.put("price",price);
        map.put("time",time);
        map.put("group",String.valueOf(grup));
        okHttpUtil.instance.post(mfragment.getActivity(), CommonUrl.createYuePaiInfo,map,this);//最后将图片在这里传出去
        Log.d(TAG, "finalsecd: "+pinjieurl( CommonUrl.createYuePaiInfo,map));
    }

    //发第一次请求，仅请求约拍立项
    public void saveThemeInfo(int YUEPAI_TYPE){
        Map<String, String> map=new HashMap<String, String>();
        if (finalImgList!=null)
            finalImgList.clear();
        imgList.clear();
        for (int i=0;i<uploadImgUrlList.size();i++){
            String[] ext=uploadImgUrlList.get(i).split("\\.");
            String extention="."+ext[ext.length-1];
            String filename=userModel.getPhone()+"/"+uploadImgUrlList.get(i).hashCode()+ new Random(System.nanoTime()).toString()+extention;
            imgList.add(filename);
            finalImgList.add(String.valueOf("\""+filename+"\""));
        }
        map.put("uid",userModel.getId());
        map.put("authkey",userModel.getAuth_key());
        ///YUEPAI_TYPE==1? StatusCode.REQUEST_CREATE_YUEPAIA:StatusCode.REQUEST_CREATE_YUEPAIB
        map.put("type",Integer.toString(80000));
        map.put("imgs", finalImgList.toString());
        okHttpUtil.instance.post(mfragment.getActivity(), CommonUrl.createYuePaiInfo,map,this);
        Log.d(TAG, "saveThemeInfo: "+pinjieurl( CommonUrl.createYuePaiInfo,map));
        CommonUtils.getUtilInstance().showToast(mfragment.getActivity(), mfragment.getString(R.string.publish_yuepai_sucess));
    }
    //临时查看，拼接url
    private String pinjieurl(String url,Map<String, String> params){
        //拼接url
        String get_url = url;
        if(params != null && params.size() > 0) {
            int i = 0;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(i++ == 0) {
                    get_url = get_url + "?" + entry.getKey() + "=" + entry.getValue();
                } else {
                    get_url = get_url + "&" + entry.getKey() + "=" + entry.getValue();
                }
            }
        }
        return  get_url;
    }

    //判断登录是否
    public boolean judge_login(){
        if(userModel.getAuth_key()!=null&&userModel.getAuth_key().equals("")){
           return  false;
        }
       return  true;
    }
    //展示本地照片
    public boolean show_local_picture(Intent intent,boolean clearFormerUploadUrlList){
        Uri uri=intent.getData();
        String photo_local_file_path=getPath_above19(MyApplication.getContext(), uri);

        if (!(photo_local_file_path.toString().toLowerCase().endsWith("jpg")||photo_local_file_path.toString().toLowerCase().endsWith("png")
                ||photo_local_file_path.toString().toLowerCase().endsWith("jpeg")||photo_local_file_path.toString().toLowerCase().endsWith("gif"))){
            CommonUtils.getUtilInstance().showToast(mfragment.getActivity(),"不支持此格式的上传");
           return false;
        }
        if(clearFormerUploadUrlList){
            if(uploadImgUrlList.size()>0){
                uploadImgUrlList.clear();
            }
        }
        Bitmap bitmap2= UploadPhotoUtil.getInstance().trasformToZoomBitmapAndLessMemory(photo_local_file_path);
        addPictureList.add(new BitmapDrawable(mfragment.getResources(), bitmap2));
        uploadImgUrlList.add(photo_local_file_path);
        imageAddGridViewAdapter.changeList(addPictureList);
        return  true;
    }
    //展示拍照照片
    public void show_take_picture(boolean clearFormerUploadUrlList,String takePictureUrl){
        if(clearFormerUploadUrlList){
            if(uploadImgUrlList.size()>0){
                uploadImgUrlList.clear();
            }
        }
        //在这里处理，获取拍到的图
        Bitmap bitmap= UploadPhotoUtil.getInstance()
                .trasformToZoomBitmapAndLessMemory(takePictureUrl);
        BitmapDrawable bd=new BitmapDrawable(mfragment.getResources(),bitmap);
        addPictureList.add(bd);
        uploadImgUrlList.add(takePictureUrl);
        imageAddGridViewAdapter.changeList(addPictureList);
    }

    public ImageAddGridViewAdapter getImageAddGridViewAdapter() {
        return imageAddGridViewAdapter;
    }

    public List<String> getPictureUrlList() {
        return pictureUrlList;
    }

    public List<Drawable> getAddPictureList() {
        return addPictureList;
    }

    public List<String> getUploadImgUrlList() {
        return uploadImgUrlList;
    }

    public ArrayList<String> getImgList() {
        return imgList;
    }

    //获取本地资源用的
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
    public static String getPath_above19(final Context context, final Uri uri){
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

    @Override
    public void onResponse(JSONObject jsonObject) throws JSONException {
        int code = Integer.valueOf(jsonObject.getString("code"));
        switch (code){
            case 800003:
                Log.d(TAG, "onResponse: "+"80000" +
                        "3");
                final JSONObject content=jsonObject.getJSONObject("contents");
                try {
                    JSONArray auth_key_arr = content.getJSONArray("auth_key");
                    ArrayList<String> arr=new ArrayList<>();
                    for (int i = 0; i < auth_key_arr.length(); i++) {
                        arr.add(auth_key_arr.getString(i));
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = arr;
                    msg.what = UPLOAD_TAKE_PICTURE;
                    handler.sendMessageDelayed(msg, 100);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 800001:
                Log.d(TAG, "onResponse: "+"80000" +
                        "1");
                Message msg = handler.obtainMessage();
                msg.what = 233;
                handler.sendMessage(msg);
                break;
            case 800002:
                Log.d(TAG, "onResponse: "+"800002");
                msg = handler.obtainMessage();
                msg.what = 9999;
                handler.sendMessage(msg);
                break;

        }
    }

    @Override
    public void onFailure(IOException e) {
        Message msg = handler.obtainMessage();
        msg.what = 886;
        handler.sendMessage(msg);

    }
}
