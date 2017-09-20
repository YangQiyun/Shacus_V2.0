package shacus.edu.seu.com.shacus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Data.Model.RemarkModel;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class RemarkAdapter extends RecyclerView.Adapter<RemarkAdapter.MyViewHolder> {

    private List<RemarkModel> remarkModelList = new ArrayList<RemarkModel>();
    private Activity activity;
    private MyViewHolder holder;

    public RemarkAdapter(Activity activity, List<RemarkModel> list){
        this.activity=activity;
        this.remarkModelList=list;
    }

    public void refresh(List<RemarkModel> rlist){
        remarkModelList=rlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new MyViewHolder(LayoutInflater.from(
                activity).inflate(R.layout.item_rv_remark, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setValues(remarkModelList.get(position),activity);
    }

    @Override
    public int getItemCount() {
        return remarkModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView remark_user_avatar;
        TextView remark_user_name;
        TextView remark_time;
        TextView remark_content;

        public MyViewHolder(View view){
            super(view);
            remark_user_avatar = (CircleImageView) view.findViewById(R.id.remark_user_avatar);
            remark_user_name = (TextView) view.findViewById(R.id.remark_user_name);
            remark_time = (TextView) view.findViewById(R.id.remark_time);
            remark_content = (TextView) view.findViewById(R.id.tv_remark_content);
        }
        public void setValues(final RemarkModel item, Context context){
            //显示头像
            Glide.with(context)
                    .load(item.getRMcmtuimg())
                    .into(remark_user_avatar);

            remark_user_name.setText(item.getRMcmtuname());
            remark_content.setText(item.getRMcmtcontent());
            //显示时间
            String remarkTime = item.getRMcmtT();
            Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
            t.setToNow(); // 取得系统时间。
            int year = t.year;
            int month = t.month;
            int date = t.monthDay;
            int hour = t.hour; // 0-23
            int minute = t.minute;

            int ryear = Integer.parseInt(remarkTime.substring(0,4));
            int rmonth = Integer.parseInt(remarkTime.substring(5,7));
            int rdate = Integer.parseInt(remarkTime.substring(8,10));
            int rhour = Integer.parseInt(remarkTime.substring(11,13));
            int rminute = Integer.parseInt(remarkTime.substring(14,16));

            if(year>ryear) {
                remark_time.setText(ryear + "年");
            }
            else if(month>rmonth){
                remark_time.setText(ryear+"年"+rmonth+"月");
            }else if(date>rdate){
                if(date-1==rdate)
                    remark_time.setText("昨天");
                else
                    remark_time.setText(rmonth+"月"+rdate+"日");
            }else if(hour>rhour){
                remark_time.setText(rhour+"·"+rminute);
            }else if(minute>rminute){
                remark_time.setText((minute-rminute)+"分钟前");
            }else{
                remark_time.setText("刚刚");
            }
        }


    }
}
