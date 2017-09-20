package shacus.edu.seu.com.shacus.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shacus.edu.seu.com.shacus.Activity.OtherDisplayActivity;
import shacus.edu.seu.com.shacus.Activity.YuepaiDetailActivity;
import shacus.edu.seu.com.shacus.Data.Cache.ACache;
import shacus.edu.seu.com.shacus.Data.Model.LoginDataModel;
import shacus.edu.seu.com.shacus.Data.Model.UserModel;
import shacus.edu.seu.com.shacus.Network.StatusCode;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.CommonUrl;
import shacus.edu.seu.com.shacus.View.CircleImageView;

/**
 * Created by iris1211 on 2017/9/19.
 */

public class SignUpAdapter extends BaseAdapter implements StatusCode {

    private List<UserModel> signup_userList;

    private YuepaiDetailActivity activity;
    private int index = 0;

    private Button button1;

    ViewHolder viewHolder;
    UserModel userModel;

    Context context;

    public SignUpAdapter(YuepaiDetailActivity activity, List<UserModel> list) {
        this.signup_userList = list;
        Log.d("adapter中粉丝数", Integer.toString(signup_userList.size()));
        this.activity = activity;

        //  context=temp;
        //   netRequest = new NetRequest(activity,activity);
    }

    @Override
    public int getCount() {
        return signup_userList.size();
    }

    @Override
    public UserModel getItem(int position) {
        return signup_userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.item_following_layout, viewGroup, false);
            viewHolder.usersignatureText = (TextView) view.findViewById(R.id.following_user_signature);
            viewHolder.userNameText = (TextView) view.findViewById(R.id.following_user_name);
            viewHolder.userImageSrc = (CircleImageView) view.findViewById(R.id.following_user_image);
            //viewHolder.follow = (Button) view.findViewById(R.id.followedbtn);
            button1 = (Button) view.findViewById(R.id.followedbtn);
            button1.setText("选择");
            viewHolder.userImageSrc.setOnClickListener(new ImageListener(position));

            if (0 == index) {
                button1.setOnClickListener(new AcceptListener(position));

            } else {
                button1.setVisibility(View.INVISIBLE);
            }

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        UserModel userModel = signup_userList.get(position);

        viewHolder.usersignatureText.setText(userModel.getSign());
        viewHolder.userNameText.setText(userModel.getNickName());
        //获取用户头像
        Glide.with(activity)
                .load(signup_userList.get(position).getHeadImage())
                // .error(R.drawable.holder)
                .into(viewHolder.userImageSrc);


        return view;
    }

    private class ViewHolder {
        CircleImageView userImageSrc;
        TextView userNameText;
        TextView usersignatureText;
        Button follow; //已关注或关注

    }

    class AcceptListener implements View.OnClickListener {
        int position;

        public AcceptListener(int p) {
            position = p;
        }

        @Override
        public void onClick(View v) {

           /* ACache aCache = ACache.get(activity);
            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
            UserModel content = null;
            Map<String, String> map = new HashMap<>();
            content = loginDataModel.getUserModel();
            String userId = content.getId();
            String authkey = content.getAuth_key();
            String followerid = signup_userList.get(position).getId();
            map.put("uid", userId);
            map.put("authkey", authkey);
            map.put("target", followerid);
            map.put("type", Integer.toString(StatusCode.REQUEST_CANCEL_FOLLOWING));

            okHttpUtil.instance.post(activity, CommonUrl.getFollowInfo, map, activity);
            Log.d("BBBBBBBBBBBBBBBBB", "发送了取消关注请求");
            // netRequest.httpRequest(map, CommonUrl.getFollowInfo);*/
            new AlertDialog.Builder(activity).setTitle("确定选择")
                    .setMessage(signup_userList.get(position).getNickName() +"\n为约拍对象吗？一旦设置，不可更改")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//确定按钮响应事件
                            ACache aCache = ACache.get(activity);
                            LoginDataModel loginDataModel = (LoginDataModel) aCache.getAsObject("loginModel");
                            UserModel content = loginDataModel.getUserModel();
                            String myid = content.getId();
                            String authkey = content.getAuth_key();
                            String ap = activity.getApId();
                            Map<String,String> map=new HashMap<String, String>();
                            map.put("apid",ap);
                            map.put("chooseduid",signup_userList.get(position).getId());
                            map.put("authkey",authkey);
                            map.put("uid",myid);
                            map.put("type", Integer.toString(10904) );
                            Log.d("aaaaaaaaaaa", map.toString());
                            dialog.dismiss();
                            /*netRequest.httpRequest(map, CommonUrl.getOrdersInfo);*/
                            okHttpUtil.instance.post(activity, CommonUrl.getOrdersInfo,map,activity);

                            index++;
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

        class ImageListener implements View.OnClickListener {
            int position;
            public ImageListener(int p) {
                position = p;
            }
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(activity, OtherDisplayActivity.class);
                // intent.putExtra("uid",otherId);
                UserModel otherModel = signup_userList.get(position);
                intent1.putExtra("otherID", otherModel.getId());
                activity.startActivity(intent1);

            }
        }

}
