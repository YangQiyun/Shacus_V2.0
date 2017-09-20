package shacus.edu.seu.com.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.R;


/**
 * Created by Administrator on 2017/9/16.
 */

public class RvPicturesAdapter extends RecyclerView.Adapter<RvPicturesAdapter.RpViewHolder>  {

    private List<Drawable> picturesList = new ArrayList<Drawable>();
    private Activity activity;
    private RpViewHolder holder;

    public RvPicturesAdapter(Activity activity, List<Drawable> list){
        this.activity=activity;
        this.picturesList=list;
    }
    @Override
    public RvPicturesAdapter.RpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new RvPicturesAdapter.RpViewHolder(LayoutInflater.from(
                activity).inflate(R.layout.item_rv_picture, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RvPicturesAdapter.RpViewHolder holder, int position) {
        holder.setValues(picturesList.get(position));
    }

    @Override
    public int getItemCount() {
        return picturesList.size();
    }

    class RpViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public RpViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_rv_picture);
        }
        void setValues(Drawable drawable){
            imageView.setImageDrawable(drawable);
        }

    }

}
