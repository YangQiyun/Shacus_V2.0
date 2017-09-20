package shacus.edu.seu.com.shacus.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ddz.floatingactionbutton.FloatingActionButton;
import com.ddz.floatingactionbutton.FloatingActionMenu;

import cn.bingoogolapple.bgabanner.BGABanner;
import shacus.edu.seu.com.shacus.Activity.CreateYuePaiActivity;
import shacus.edu.seu.com.shacus.Activity.FindcameristActivity;
import shacus.edu.seu.com.shacus.Activity.FindmodelActivity;
import shacus.edu.seu.com.shacus.Activity.OrderfriendsActivity;
import shacus.edu.seu.com.shacus.Data.Manager.YuepaiManager;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.View.CircleImageView;

import static shacus.edu.seu.com.shacus.R.id.user_name;

/**
 * Created by 杨棋允 on 2017/9/5.
 */
public class yuepaiFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener ,View.OnClickListener{
    private static final String TAG = "yuepaiFragment";
    private FloatingActionMenu floatingActionMenu;
    private CircleImageView mProfileImage;
    private AppBarLayout appbarLayout;
    private  ViewPager viewPager;
    private TabLayout tabLayout;
    private BGABanner mBanner;
    private ImageButton startyuepai;
    private FloatingActionButton []floatingActionButtons;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private YuepaiManager DataManager;

    public static yuepaiFragment newInstance(int value){
        Bundle args=new Bundle();
        args.putInt(TAG,value);
        yuepaiFragment fragment=new yuepaiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_yuepai,container,false);
        DataManager=new YuepaiManager(mActivity);
        initView();
        initData();
        return  mRootView;
    }

    void initView(){
        startyuepai=findViewById(R.id.btn_startyuepai);
        floatingActionButtons=new FloatingActionButton[3];
        floatingActionButtons[0]=findViewById(R.id.action_up);
        floatingActionButtons[1]=findViewById(R.id.action_middle);
        floatingActionButtons[2]=findViewById(R.id.action_down);
        for(FloatingActionButton floatingActionButton:floatingActionButtons)
            floatingActionButton.setOnClickListener(this);
        floatingActionMenu=findViewById(R.id.floatingbuttonMenu);
        tabLayout = findViewById(R.id.tably_fgyuepai);
        viewPager =  findViewById(R.id.viewpager_fgyuepai);
        appbarLayout =  findViewById(R.id.appbar_fgyuepai);
        mProfileImage =  findViewById(R.id.profile_fgyuepai);
        mBanner=findViewById(R.id.banner_main_zoom);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        viewPager.setAdapter(new TabsAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    void initData(){
        startyuepai.setOnClickListener(this);
        DataManager.setBanner(mBanner);
        Glide.with(this)
                .load(DataManager.getUserModel()
                .getHeadImage())
                .into(mProfileImage);
        TextView  textview=findViewById(user_name);
        textview.setText(DataManager.getUserModel().getNickName());
        textview=findViewById(R.id.user_qianmin);
        String subtitle=DataManager.getUserModel().getSign();
        if(subtitle.equals(""))
        textview.setText("生活就像海洋，追影到远方");
        else
        {
            if (subtitle.length()>10)
            {
                String a=subtitle.substring(0,9);
                a+="...";
                textview.setText(a);
            }
            else
                textview.setText(subtitle);
        }

    }


    //实现上划效果
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_up:
                floatingActionMenu.collapse();
                Intent intent=new Intent(mActivity, FindcameristActivity.class);
                startActivity(intent);
                break;
            case R.id.action_middle:
                floatingActionMenu.collapse();
                Intent intent1=new Intent(mActivity, FindmodelActivity.class);
                startActivity(intent1);
                break;
            case R.id.action_down:
                floatingActionMenu.collapse();
                Intent intent2=new Intent(mActivity, OrderfriendsActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_startyuepai:
                Intent intent3=new Intent(mActivity, CreateYuePaiActivity.class);
                 startActivity(intent3);
//                Intent intent3=new Intent(mActivity,ShowBigimageActivity.class);
//                Bundle temp=new Bundle();
//                temp.putStringArrayList(ShowBigimageActivity.LIST,DataManager.getTempurl());
//               intent3.putExtras(temp);
//                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    //内部的两个fragment
    private static class TabsAdapter extends FragmentPagerAdapter {
        private  static CollectionFragment collectionFragment=CollectionFragment.newInstance(0);
        private  static DynamicFragment dynamicFragment=DynamicFragment.newInstance(1);
        private static final int TAB_COUNT = 2;

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int i) {
            if(i==1)
                return collectionFragment;
            return dynamicFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(0==position)
                return "动态";
           else
                return "作品集";
        }
    }

    public  void setFloatingActionMenu(int action){
        floatingActionMenu.setVisibility(action);
    }
}
