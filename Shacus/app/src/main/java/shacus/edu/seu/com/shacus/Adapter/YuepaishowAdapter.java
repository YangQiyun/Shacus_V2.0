package shacus.edu.seu.com.shacus.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Activity.YuepaiDetailActivity;
import shacus.edu.seu.com.shacus.Activity.YuepaiInforActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.PhotographerModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;


public class YuepaishowAdapter extends BaseAdapter {
    private List<PhotographerModel> rankList;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;
    private int getActivitytype;
    private int getlabtype;
    //private Handler handler;

    public YuepaishowAdapter(Activity activity, List<PhotographerModel> list,int getActivitytype,int getlabtype){
        this.rankList = list;
        this.activity = activity;
        this.getActivitytype=getActivitytype;
        this.getlabtype=getlabtype;
        //handler=handle;
    }

    public void add(List<PhotographerModel> persons){
        this.rankList.addAll(persons);
    }
    public void refresh(List<PhotographerModel> persons) {
        this.rankList = persons;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public PhotographerModel getItem(int i) {
        return rankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view ==null){
            layoutInflater = LayoutInflater.from(activity);
            view = layoutInflater.inflate(R.layout.item_rank_layout, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) view.getTag();

        PhotographerModel item = getItem(i);
        viewHolder.setValues(item);

        return view;
    }


    private class ViewHolder {
        CircleImageView user_image;
        TextView user_name;
        TextView user_local;
        TextView interestcount;
        TextView yuepai_content;
        TextView user_age;
        TextView yuepai_fabutime;
        TextView yuepai_time;
        TextView yuepai_jiage;
        FrameLayout photoset_img_frame;
        TextView photoset_img_count;
        List<ImageView> photoset_img_list;
        LinearLayout ll_photoset_square_imgs;
        ImageView user_sex;
        TextView none_jp;
        LinearLayout yuepai;

        public ViewHolder(View view){
            yuepai = (LinearLayout) view.findViewById(R.id.yuepai);
            user_image = (CircleImageView) view.findViewById(R.id.user_image);
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_local = (TextView) view.findViewById(R.id.user_local);
            user_sex = (ImageView) view.findViewById(R.id.user_sex);
            interestcount = (TextView) view.findViewById(R.id.interestcount);
            yuepai_content = (TextView) view.findViewById(R.id.yuepai_content);
            user_age = (TextView) view.findViewById(R.id.user_age);
            yuepai_fabutime = (TextView) view.findViewById(R.id.yuepai_fabutime);
            yuepai_time = (TextView) view.findViewById(R.id.yuepai_time);
            yuepai_jiage = (TextView) view.findViewById(R.id.yuepai_jiage);
            photoset_img_frame = (FrameLayout) view.findViewById(R.id.photoset_img_frame);
            photoset_img_count = (TextView) view.findViewById(R.id.photoset_img_count);
            ll_photoset_square_imgs = (LinearLayout) view.findViewById(R.id.ll_photoset_square_imgs);
            photoset_img_list=new ArrayList<>();
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_1));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_2));
            photoset_img_list.add((ImageView)view.findViewById(R.id.photoset_img_3));
            none_jp = (TextView) view.findViewById(R.id.none_jp);
        }

        public void setValues(final PhotographerModel item){
            //具体的展开内容
            yuepai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ACache cache=ACache.get(activity);
                    cache.put("ypinfo",item);
                    Intent intent;
                    if(getActivitytype==2)
                        intent= new Intent(activity, YuepaiDetailActivity.class);
                    else
                        intent=new Intent(activity, YuepaiInforActivity.class);
                    activity.startActivity(intent);
                }
            });

            //用户头像
            String userheadimg = item.getUserModel().getHeadImage();
            Glide.with(activity)
                    .load(userheadimg)
                    .asBitmap()
                    .centerCrop()
                    .into(user_image);
            user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ACache cache=ACache.get(activity);
                    LoginDataModel loginModel=(LoginDataModel)cache.getAsObject("loginModel");
                    UserModel userModel=loginModel.getUserModel();
                    if (! userModel.getId().equals(item.getUserModel().getId())){
                        //Intent in = new Intent(activity, OtherUserDisplayActivity.class);
                       // in.putExtra("id", item.getUserModel().getId());
                      //  activity.startActivity(in);
                    }

                }
            });
            //用户昵称
            String name = item.getUserModel().getNickName();
            if(!name.equals(""))
                user_name.setText(name);
            else
                user_name.setText("加载中");

            //用户性别
            String sexual = item.getUserModel().getSex();
            if (sexual.equals("男"))
                user_sex.setBackgroundResource(R.drawable.male);
            else if(sexual.equals("女"))
                user_sex.setBackgroundResource(R.drawable.female);

            //用户地点
            String local = item.getUserModel().getLocation();
            if (!local.equals(""))
                user_local.setText(local);
            else
                user_local.setText("江苏");

            //用户年龄
            String age = item.getUserModel().getAge();
            if (!age.equals(""))
                user_age.setText(String.valueOf(age));
            else
                user_age.setText("");

            //用户相册
            List<String> imgs = item.getAPimgurl();
            int imgsSize = imgs.size();
            /*for(int i = 0; i < imgsSize;i++){
                Glide.with(activity)
                        .load((imgs.get(i)))
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(i));
            }*/
            if (imgsSize>=3){//图片大于三张，正常显示
                none_jp.setVisibility(View.GONE);
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(1).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.VISIBLE);
                photoset_img_frame.setVisibility(View.VISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
                if (imgsSize > 3)
                {
                    photoset_img_count.setVisibility(View.VISIBLE);
                    photoset_img_count.setText(String.valueOf(imgsSize - 3));
                }

                for(int i = 0; i < 3;i++){
                    Glide.with(activity)
                            .load((imgs.get(i)))
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.holder)
                            .error(R.drawable.loading_error)
                            .into(photoset_img_list.get(i));
                }

            }else if(imgsSize==2){
                none_jp.setVisibility(View.GONE);
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(1).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
                Glide.with(activity)
                        .load((imgs.get(0)))
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(0));
                Glide.with(activity)
                        .load((imgs.get(1)))
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(1));
            }else if(imgsSize==1){
                none_jp.setVisibility(View.GONE);
                photoset_img_list.get(0).setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.INVISIBLE);
                photoset_img_list.get(1).setVisibility(View.INVISIBLE);
                photoset_img_frame.setVisibility(View.INVISIBLE);
                photoset_img_count.setVisibility(View.INVISIBLE);
                Glide.with(activity)
                        .load((imgs.get(0)))
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.loading_error)
                        .into(photoset_img_list.get(0));
            }else if (imgsSize == 0){
                none_jp.setVisibility(View.VISIBLE);
                photoset_img_list.get(2).setVisibility(View.GONE);
                photoset_img_list.get(1).setVisibility(View.GONE);
                photoset_img_list.get(0).setVisibility(View.GONE);
                photoset_img_frame.setVisibility(View.GONE);
                photoset_img_count.setVisibility(View.GONE);
            }

            //约拍时间
            String time = item.getAPtime();
            if(!time.equals(""))
                yuepai_time.setText(time);
            else
                yuepai_time.setText("暂无");
            //约拍发布时间
            String createtime = item.getAPcreatetime();
            if (!createtime.equals("")){
                yuepai_fabutime.setVisibility(View.VISIBLE);
                yuepai_fabutime.setText(createtime);
            }
            else
                yuepai_fabutime.setVisibility(View.INVISIBLE);

            //约拍内容
            String content = item.getAPcontent();
            if (!content.equals(""))
                yuepai_content.setText(content);

            else
                yuepai_content.setText("暂无描述");

            List<String> pricetype = new ArrayList<>();
            pricetype.add("希望收费");
            pricetype.add("最多付费");
            pricetype.add("希望互勉");
            pricetype.add("价格商议");
            //价格描述
            int pricet = item.getAPpricetype();
            if (pricet==0 || pricet==1){
                yuepai_jiage.setText(String.valueOf(pricetype.get(pricet) + item.getAPprice()));
            }
            else{
                yuepai_jiage.setText(String.valueOf(pricetype.get(pricet)));
            }

//            感兴趣
            if(getActivitytype!=2)
            interestcount.setText(String.valueOf(item.getAPlikeN())+"感兴趣");
            else
            {
                if(0==item.getAPstatus())
                interestcount.setText("报名中");
                if(1==item.getAPstatus())
                    interestcount.setText("进行中");
                if(2==item.getAPstatus())
                    interestcount.setText("已完成");
            }

        }
    }
}
