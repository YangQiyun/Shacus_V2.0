package shacus.edu.seu.com.shacus.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.bingoogolapple.bgabanner.BGABanner;
import shacus.edu.seu.com.shacus.R;
/**
 * Created by Mind on 2017/9/6.
 */
public class BannerAdapter implements BGABanner.Adapter<ImageView, String>, BGABanner.Delegate<ImageView, String>{
    private  Context mContext;
    public BannerAdapter(Context context){
        mContext=context;
    }
    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
        Glide.with(mContext)
                .load(model)
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .dontAnimate()
                .centerCrop()
                .into(itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
        Toast.makeText(mContext, "点击了第" + (position + 1) + "页", Toast.LENGTH_SHORT).show();
    }


}
