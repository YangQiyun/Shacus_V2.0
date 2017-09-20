package shacus.edu.seu.com.shacus.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shacus.edu.seu.com.shacus.Data.Model.YuePaiGroupModel;
import shacus.edu.seu.com.shacus.Fragment.PlaceholderFragment;
import shacus.edu.seu.com.shacus.R;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by Mind on 2017/9/9.
 */
public class MyupaiInforActivity extends BaseActivity implements View.OnClickListener{
    private List<YuePaiGroupModel> labtypes=new ArrayList<>();
    private ImageButton btn_return;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_photograph);
        initView();
        initData();
    }

    //这是一个坑，android 5.0以上才有效的，不然在这里也是不会有报错的
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
    private  void initView(){
        TextView textView=(TextView)findViewById(R.id.tv_title);
        textView.setText("我的订单");
        btn_return= (ImageButton) findViewById(R.id.backbtn);
        btn_return.setOnClickListener(this);
        toolbar= (Toolbar) findViewById(R.id.fcactivity_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.zero_black));
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(MODE_SCROLLABLE);
    }

    private void initData(){
        YuePaiGroupModel yuePaiGroupModel=new YuePaiGroupModel();
        yuePaiGroupModel=new YuePaiGroupModel();
        yuePaiGroupModel.setName("报名中");
        yuePaiGroupModel.setType("1");
        labtypes.add(yuePaiGroupModel);
        yuePaiGroupModel=new YuePaiGroupModel();
        yuePaiGroupModel.setName("进行中");
        yuePaiGroupModel.setType("2");
        labtypes.add(yuePaiGroupModel);
        yuePaiGroupModel=new YuePaiGroupModel();
        yuePaiGroupModel.setName("已完成");
        yuePaiGroupModel.setType("3");
        labtypes.add(yuePaiGroupModel);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        btn_return.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.backbtn:
                finish();
                break;
            default:
                break;
        }
    }


    //静态内部类，管理分页

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           PlaceholderFragment frag= PlaceholderFragment.newInstance(position,2);
            return frag;
        }

        @Override
        public int getCount(){
            return labtypes.size()+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==0)
                return "我发布";
            else
                return labtypes.get(position-1).getName();
        }
    }
}
