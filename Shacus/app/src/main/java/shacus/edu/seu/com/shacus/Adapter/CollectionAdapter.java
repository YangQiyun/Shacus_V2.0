package shacus.edu.seu.com.shacus.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import shacus.edu.seu.com.shacus.Data.Model.CollectionModel;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Mind on 2017/9/15.
 */
public class CollectionAdapter extends BaseMultiItemQuickAdapter<CollectionModel, BaseViewHolder> {

    private Context context;
    private int width;
    private int height;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CollectionAdapter(List<CollectionModel> data, Context context) {
        super(data);
        this.context=context;
        Log.d(TAG, "convert: "+data.get(0).getUClurl().size());
        addItemType(CollectionModel.TYPE, R.layout.item_collection_view);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
         width = dm.widthPixels;
         height = dm.heightPixels;
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionModel item) {
        switch (helper.getItemViewType()){
            case CollectionModel.TYPE:
                ImageView imageView=helper.getView(R.id.collection_img);
                switch (helper.getPosition() % 3) {
                    case 0:
                    case 1:
                        imageView.setLayoutParams(new FrameLayout.LayoutParams(width/2, height / 4));
                        break;
                    case 2:
                        imageView.setLayoutParams(new FrameLayout.LayoutParams(width, height / 3));
                        break;
                }
                Glide.with(context).load(item.getUClurl().get(0)).error(R.drawable.loading_error).placeholder(R.drawable.loading_error).into(imageView);
                helper.setText(R.id.collection_title,item.getUCtitile());
                break;
        }
    }
}
