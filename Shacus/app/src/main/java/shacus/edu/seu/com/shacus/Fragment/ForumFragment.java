package shacus.edu.seu.com.shacus.Fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import shacus.edu.seu.com.shacus.Activity.PhotosAddActivity;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Administrator on 2017/9/4.
 */

public class ForumFragment extends BaseFragment implements View.OnClickListener  {

    private static final String TAG = "ForumFragment";

    static final int WHITE = Color.parseColor("#FFFFFF");
    static final int THEME = Color.parseColor("#C1CC65");
    private Button btn_community;
    private Button btn_mine;
    private ImageButton ibtn_publish;
    private ImageButton ibtn_message;


    private ForumCommunityFragment forumCommunityFragment;
    private ForumMineFragment forumMineFragment;
    private FragmentTransaction transaction;


    public static ForumFragment newInstance(int value){
        Bundle args = new Bundle();
        args.putInt(TAG,value);
        ForumFragment forumFragment = new ForumFragment();
        forumFragment.setArguments(args);
        return forumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_forum,container,false);
        initView();

        transaction = getChildFragmentManager().beginTransaction();
        forumCommunityFragment = new ForumCommunityFragment();
        transaction.add(R.id.forumfragment_layout,forumCommunityFragment);
        transaction.commitAllowingStateLoss();

        btn_community.setSelected(true);
        btn_community.setTextColor(THEME);
        btn_mine.setTextColor(WHITE);
        return  mRootView;
    }

    private void initView(){
        btn_community = findViewById(R.id.btn_community);
        btn_mine = findViewById(R.id.btn_mine);
        ibtn_publish = findViewById(R.id.ibtn_publish);
        ibtn_message = findViewById(R.id.ibtn_message);

        btn_community.setOnClickListener(this);
        btn_mine.setOnClickListener(this);
        ibtn_publish.setOnClickListener(this);
        ibtn_message.setOnClickListener(this);
    }


    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        if(forumCommunityFragment==null&&fragment instanceof ForumCommunityFragment){
            forumCommunityFragment = (ForumCommunityFragment) fragment;
        }
        if(forumMineFragment==null&&fragment instanceof ForumMineFragment){
            forumMineFragment = (ForumMineFragment) fragment;
        }
        super.onAttachFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        transaction = getChildFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.btn_community:
                btn_mine.setSelected(false);
                btn_mine.setTextColor(WHITE);
                btn_community.setSelected(true);
                btn_community.setTextColor(THEME);
                transaction.hide(forumMineFragment);
                if(forumCommunityFragment==null){
                    forumCommunityFragment = new ForumCommunityFragment();
                    transaction.add(R.id.forumfragment_layout,forumCommunityFragment);
                }else{
                    transaction.show(forumCommunityFragment);
                }
                break;
            case R.id.btn_mine:
                btn_community.setSelected(false);
                btn_community.setTextColor(WHITE);
                btn_mine.setSelected(true);
                btn_mine.setTextColor(THEME);
                transaction.hide(forumCommunityFragment);
                if(forumMineFragment==null){
                    forumMineFragment = new ForumMineFragment();
                    transaction.add(R.id.forumfragment_layout,forumMineFragment);
                }else{
                    transaction.show(forumMineFragment);
                }
                break;
            case R.id.ibtn_publish:
                Intent intent=new Intent(mActivity, PhotosAddActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
                break;
            case R.id.ibtn_message:
                gotoQQ(getActivity());
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    public static void gotoQQ(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity"));
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "未安装QQ,看个几把", Toast.LENGTH_SHORT).show();
        }
    }

}
