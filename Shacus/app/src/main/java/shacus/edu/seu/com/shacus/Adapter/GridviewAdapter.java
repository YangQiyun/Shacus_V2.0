package shacus.edu.seu.com.shacus.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import shacus.edu.seu.com.shacus.R;

/**
 * Created by Mind on 2017/9/14.
 */
public class GridviewAdapter extends BaseAdapter {
    // 上下文对象
    private Context context;
    // 图片数组
    private List<String> url;
    private ViewGroup.LayoutParams layoutParams;
    private int mImageWidth2;
    private int mImageWidth3;

    public GridviewAdapter(Context context, List<String> url){
        this.context=context;
        this.url=url;
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
      //  mImageWidth3 = (int) ((outMetrics.widthPixels -12* outMetrics.density)/3);
      //  mImageWidth2 = (int) ((outMetrics.widthPixels -12* outMetrics.density)/2);
        mImageWidth2=width/2;
        mImageWidth3=width/3;
    }

    @Override
    public int getCount() {
        return url == null ? 0 : url.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_dynamic_image,null);
            imageView = (ImageView) convertView.findViewById(R.id.imgview);
            switch (url.size()) {
                case 1:
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2*2,mImageWidth2*2));// 设置ImageView对象布局
                    break;
                case 2:
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2,mImageWidth2));
                    break;
                case 4:
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2,mImageWidth2));
                    break;
                case 3:
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth2,mImageWidth2));
                    break;
                default:
                    imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth3,mImageWidth3));// 设置ImageView对象布局
                    break;
            }

            imageView.setAdjustViewBounds(true);// 设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);// 设置刻度的类型
// imageView.setPadding(4, 4, 4, 4);// 设置间距
        } else {
            imageView = (ImageView) convertView;
        }
        Glide.with(imageView.getContext()).load(url.get(position)).placeholder(R.drawable.holder_square).error(R.drawable.holder_square).into(imageView);
        return imageView;
    }

}
