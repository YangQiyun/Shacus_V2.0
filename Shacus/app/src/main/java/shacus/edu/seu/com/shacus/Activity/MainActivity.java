package shacus.edu.seu.com.shacus.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import shacus.edu.seu.com.shacus.Fragment.BaseFragment;
import shacus.edu.seu.com.shacus.Fragment.ForumFragment;
import shacus.edu.seu.com.shacus.Fragment.MessageFragment;
import shacus.edu.seu.com.shacus.Fragment.SelfDisplayFragment;
import shacus.edu.seu.com.shacus.Fragment.yuepaiFragment;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;
import shacus.edu.seu.com.shacus.swipecards.swipe.CardFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private FragmentManager fragmentManager=this.getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;

    private ForumFragment communityFragment;

    private ImageView imageView;
    private WeakRefHander mHandler;
    private ImageButton[] BottomButton;
    private BaseFragment[] fragmentGroup=new BaseFragment[5];
    private static final String TAG = "MainActivity";
    private static int select=0;//为了进入app后直接进入约拍
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(0==select){
            select=1;
           BottomButton[2].callOnClick();}
        int cuttent=0;
        for(BaseFragment baseFragment:fragmentGroup){
            if(baseFragment.isHidden())
                cuttent++;
        }
        Log.d(TAG, "onResume: "+cuttent);
        if(cuttent<=3){
            hidefragment();
            BottomButton[2].callOnClick();
        }
       }


    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        if(fragmentGroup[0]==null&&fragment instanceof CardFragment)
            fragmentGroup[0]=(BaseFragment) fragment;
        if(fragmentGroup[1]==null&&fragment instanceof ForumFragment)
            fragmentGroup[1]=(BaseFragment) fragment;
        if(fragmentGroup[2]==null&&fragment instanceof yuepaiFragment)
            fragmentGroup[2]=(BaseFragment) fragment;
        if(fragmentGroup[3]==null&&fragment instanceof MessageFragment)
            fragmentGroup[3]=(BaseFragment) fragment;
        if(fragmentGroup[4]==null&&fragment instanceof SelfDisplayFragment)
            fragmentGroup[4]=(BaseFragment) fragment;
        super.onAttachFragment(fragment);
    }


    private void initView(){
        BottomButton=new ImageButton[5];
        BottomButton[0]= (ImageButton) findViewById(R.id.button_List);
        BottomButton[1]= (ImageButton) findViewById(R.id.button_Forum);
        BottomButton[2]= (ImageButton) findViewById(R.id.button_yuepai);
        BottomButton[3]= (ImageButton) findViewById(R.id.button_Message);
        BottomButton[4]= (ImageButton) findViewById(R.id.button_Mine);
        for(ImageButton i:BottomButton)
            i.setOnClickListener(this);
    }
    private void initData(){
        hidefragment();
    }


    @Override
    public void onClick(View v) {
        hidefragment();
        fragmentTransaction=fragmentManager.beginTransaction();
        for(ImageButton i:BottomButton)
            i.setSelected(false);
        switch (v.getId()){
            case R.id.button_List:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_list"));
                fragmentTransaction.commit();
                BottomButton[0].setSelected(true);
                break;
            case R.id.button_Forum:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_forum"));
                fragmentTransaction.commit();
                BottomButton[1].setSelected(true);
                break;
            case R.id.button_yuepai:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_yuepai"));
                fragmentTransaction.commit();
                BottomButton[2].setSelected(true);
                break;

            case R.id.button_Message:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_Message"));
                fragmentTransaction.commit();
                BottomButton[3].setSelected(true);
                break;

            case R.id.button_Mine:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_Mine"));
                fragmentTransaction.commit();
                BottomButton[4].setSelected(true);
                break;
            default:
                Log.d(TAG, "onClick: other click");
                break;
        }
    }
    /*
    * 初始化创建所有的fragment，还有当隐藏frament使用
    * */
    private void hidefragment(){
        fragmentTransaction=fragmentManager.beginTransaction();
        String a[]={"fg_list","fg_forum","fg_yuepai","fg_Message","fg_Mine"};
        for(int i=0;i<5;++i){
            if(fragmentGroup[i]==null){
                if(2==i){
                    fragmentGroup[i]= yuepaiFragment.newInstance(i);
                }
                else
                if(1==i)
                    fragmentGroup[i]=new ForumFragment();
                else
                if(4==i)
                    fragmentGroup[i]=new SelfDisplayFragment();
                else
                if(0==i)
                    fragmentGroup[i]= new CardFragment();
                else
                    fragmentGroup[i]= new MessageFragment();
                fragmentTransaction.add(R.id.fragmentlayout_content,fragmentGroup[i],a[i]);}
            else
                fragmentTransaction.hide(fragmentManager.findFragmentByTag(a[i]));
        } fragmentTransaction.commit();
    }



}
