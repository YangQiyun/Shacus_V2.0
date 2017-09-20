package shacus.edu.seu.com.shacus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import shacus.edu.seu.com.shacus.Activity.PhotosetOverviewActivity;
import shacus.edu.seu.com.shacus.Data.Model.MyRecord;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;

/**
 * Created by ljh on 2017/9/16.
 */

public class PhotosetOverviewAdapter extends BaseAdapter   {

    private List<MyRecord> photosetlist;

    private PhotosetOverviewActivity activity;
   // FollowInfoManager DataManager;
    //private NetRequest netRequest;
    private Button button1;
    //    private Button button2;
    ViewHolder viewHolder;
    UserModel userModel;
    String type = null;
    String index = null;
    Context context;

    public PhotosetOverviewAdapter(PhotosetOverviewActivity activity, List<MyRecord> list  ) {
        this.photosetlist = list;
        this.activity = activity;

        //DataManager=activity.getFollowManager();
        //  context=temp;
        //   netRequest = new NetRequest(activity,activity);
    }
 @Override
    public int getCount() {
        return photosetlist.size();
    }

    @Override
    public MyRecord getItem(int position) {
        return photosetlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view == null){
            viewHolder = new ViewHolder();
                view = LayoutInflater.from(activity).inflate(R.layout.item_photoset_layout, viewGroup, false);
                //viewHolder.usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
                viewHolder.photoText = (TextView) view.findViewById(R.id.photoset_name);
                viewHolder.photosetImageSrc = (CircleImageView) view.findViewById(R.id.photoset_cover_image);
                //viewHolder.follow = (Button) view.findViewById(R.id.followedbtn);
                button1 = (Button) view.findViewById(R.id.see_photoset_btn);

                  //  button1.setTag(StatusCode.REQUEST_CANCEL_FOLLOWING);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        MyRecord record=getItem(position);

        viewHolder.photoText.setText(record.getLabel());
        //获取用户头像
        Glide.with(activity)
                .load(record.getImageUrl())
                // .error(R.drawable.holder)
                .into(viewHolder.photosetImageSrc);
        return view;
    }

    private class ViewHolder{
        CircleImageView photosetImageSrc;
        TextView photoText;

        Button follow; //已关注或关注

    }



}
