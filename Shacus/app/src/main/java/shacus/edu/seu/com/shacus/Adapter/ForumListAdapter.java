package shacus.edu.seu.com.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Data.Manager.ForumManager;
import shacus.edu.seu.com.shacus.Data.Model.ForumItemModel;
import shacus.edu.seu.com.shacus.Data.Model.ImageData;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;

import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_ADD_COLLECT_SUCCESS;
import static shacus.edu.seu.com.shacus.Utils.StatusCode.REQUEST_CANCEL_COLLECT_SUCCESS;

/**
 * Created by Administrator on 2017/9/6.
 */

public class ForumListAdapter extends BaseAdapter {

    private UserModel userModel;
    private List<ForumItemModel> forumList = new ArrayList<ForumItemModel>();
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;


    public ForumListAdapter(Activity activity, List<ForumItemModel> list) {
        this.forumList = list;
        this.activity = activity;
    }

    public void add(List<ForumItemModel> list){
        this.forumList.addAll(list);
    }
    public void refresh(List<ForumItemModel> list){
        this.forumList = list;
    }
    @Override
    public int getCount() {
        return forumList.size();
    }

    @Override
    public ForumItemModel getItem(int position) {
        return forumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==null){
            layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_forum, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        ForumItemModel item = getItem(i);
        viewHolder.setValues(item,activity);
        return view;
    }


    private class ViewHolder {

        CircleImageView photoset_publish_user_avatar;
        TextView publish_user_name;
        TextView tv_photoset_title;
        TextView tv_photoset_content;
        LinearLayout ll_photoset_square_imgs;
        List<ImageView> photoset_img_list;
        //GridView photoset_grid_join_user_scroll;
        FrameLayout photoset_img_frame;
        TextView photoset_img_count;
        TextView tv_photoset_likecount;
        TextView tv_forum_time;
        LinearLayout ll_item_detail__layout;
        Button btn_add_favor;

        public ViewHolder(View view) {
            photoset_img_list = new ArrayList<>();
            photoset_publish_user_avatar = (CircleImageView) view.findViewById(R.id.photoset_publish_user_avatar);
            photoset_img_list.add((ImageView) view.findViewById(R.id.photoset_img_1));
            photoset_img_list.add((ImageView) view.findViewById(R.id.photoset_img_2));
            photoset_img_list.add((ImageView) view.findViewById(R.id.photoset_img_3));
            ll_photoset_square_imgs = (LinearLayout) view.findViewById(R.id.ll_photoset_square_imgs);
            tv_photoset_content = (TextView) view.findViewById(R.id.tv_photoset_content);
            tv_photoset_title = (TextView) view.findViewById(R.id.tv_photoset_title);
            tv_forum_time = (TextView) view.findViewById(R.id.forum_time);
            publish_user_name = (TextView) view.findViewById(R.id.publish_user_name);
            photoset_img_frame = (FrameLayout) view.findViewById(R.id.photoset_img_frame);
            photoset_img_count = (TextView) view.findViewById(R.id.photoset_img_count);
            ll_item_detail__layout = (LinearLayout) view.findViewById(R.id.ll_item_detail__layout);
            tv_photoset_likecount = (TextView) view.findViewById(R.id.tv_photoset_likecount);
            btn_add_favor = (Button) view.findViewById(R.id.btn_add_favor);
        }

        void setValues(final ForumItemModel item, final Context context) {
            //显示头像
            Glide.with(context)
                    .load(item.getFCuserheadphoto().getImageUrl())
                    .into(photoset_publish_user_avatar);
            //显示...
            tv_photoset_title.setText(item.getFCtitle());
            tv_photoset_content.setText(item.getFCcontent());
            publish_user_name.setText(item.getFCusername());
            String likeremarkCount = item.getFCfavornum()+"赞同·"+item.getFCremarknum()+"评论";
            tv_photoset_likecount.setText(likeremarkCount);

            //显示时间
            String remarkTime = item.getFCremarktime();
            Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow(); // 取得系统时间。
            int year = t.year;
            int month = t.month;
            int date = t.monthDay;
            int hour = t.hour; // 0-23
            int minute = t.minute;
            //int second = t.second;

            int ryear = Integer.parseInt(remarkTime.substring(0,4));
            int rmonth = Integer.parseInt(remarkTime.substring(5,7));
            int rdate = Integer.parseInt(remarkTime.substring(8,10));
            int rhour = Integer.parseInt(remarkTime.substring(11,13));
            int rminute = Integer.parseInt(remarkTime.substring(14,16));
            //int rsecond = Integer.parseInt(remarkTime.substring(17,19));

            if(year>ryear) {
                tv_forum_time.setText(ryear + "年");
            }
            else if(month>rmonth){
                tv_forum_time.setText(ryear+"年"+rmonth+"月");
            }else if(date>rdate){
                if(date-1==rdate)
                    tv_forum_time.setText("昨天");
                else
                    tv_forum_time.setText(rmonth+"月"+rdate+"日");
            }else if(hour>rhour){
                tv_forum_time.setText(rhour+"·"+rminute);
            }else if(minute>rminute){
                tv_forum_time.setText((minute-rminute)+"分钟前");
            }else{
                tv_forum_time.setText("刚刚");
            }

            if(item.getFCiscollect()==0)
                btn_add_favor.setText("+关注");
            else
                btn_add_favor.setText("已关注");


            //显示图片
            //处理略缩图的显示逻辑
            List<ImageData> imgs=item.getFCpictures();
            int imgsSize=item.getFCpicnum();
            Log.e(String.valueOf(item.getFCid()), String.valueOf(imgsSize));
            //加载图片

            ll_photoset_square_imgs.setVisibility(View.VISIBLE);
            for (int i=0;i<imgsSize&&i<3;i++) {
                Glide.with(context)
                        .load(imgs.get(i).getImageUrl())
                        //.asBitmap()
                        //.centerCrop()
                        //.placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(i));
            }
            if (imgsSize>=3){//图片大于三张，正常显示
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(1).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.VISIBLE);
                photoset_img_count.setText(String.valueOf(imgsSize));
            }else if (imgsSize==2){//图片只有两张，第三张隐藏
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(1).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
            }else if (imgsSize==1){//图片只有一张，前两张隐藏
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_list.get(1).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
            }else if(imgsSize==0){//没有图片，不显示
                ll_photoset_square_imgs.setVisibility(View.GONE);
            }

            Handler mHandler = new Handler(new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 851120:
                        case REQUEST_ADD_COLLECT_SUCCESS:
                            Log.e("handle acs", "detail");
                            btn_add_favor.setText("已关注");
                            item.setFCiscollect(1);
                            Toast.makeText(context,"关注成功",Toast.LENGTH_SHORT).show();
                            break;
                        case 851132:
                        case REQUEST_CANCEL_COLLECT_SUCCESS:
                            Log.e("handle ccs", "detail");
                            btn_add_favor.setText("+关注");
                            item.setFCiscollect(0);
                            Toast.makeText(context,"取消关注",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            final ForumManager forumManager = new ForumManager(context,mHandler,item.getFCid());
            btn_add_favor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getFCiscollect()==0){
                        forumManager.addCollect();
                    }else if(item.getFCiscollect()==1){
                        forumManager.cancelCollect();
                    }
                }
            });
        }
    }
}
