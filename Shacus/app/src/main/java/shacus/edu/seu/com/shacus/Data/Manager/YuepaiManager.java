package shacus.edu.seu.com.shacus.Data.Manager;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.bingoogolapple.bgabanner.BGABanner;
import shacus.edu.seu.com.shacus.Activity.WebviewActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.NavigationModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Mind on 2017/9/12.
 */
public class YuepaiManager {
    ACache cache;
    LoginDataModel loginModel;
    UserModel userModel;
    Context mContext;

    private ArrayList<String> tempurl;

    public YuepaiManager(Context context){
        mContext=context;
        cache=ACache.get(mContext);
        loginModel= (LoginDataModel) cache.getAsObject("loginModel");
        userModel=loginModel.getUserModel();
    }

    //设置导航栏
    public void setBanner(BGABanner mBanner){
        mBanner.setAdapter(new BGABanner.Adapter<ImageView,String>(){

            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(mContext)
                        .load(model)
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.holder)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        ArrayList<String> imgurl=new ArrayList<String>();
        ArrayList<String> tips=new ArrayList<String>();
        for(NavigationModel navigationModel:loginModel.getDaohanglan()){
            imgurl.add(navigationModel.getImgurl());
            tips.add("");
        }
        tempurl=imgurl;
        mBanner.setData(imgurl,tips);
        mBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
                Intent intent=new Intent(mContext,WebviewActivity.class);
                intent.putExtra("Url",loginModel.getDaohanglan().get(position).getWeburl());
                mContext.startActivity(intent);
            }
        });
    }

    public ArrayList<String> getTempurl() {
        return tempurl;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
