package shacus.edu.seu.com.shacus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shacus.edu.seu.com.shacus.R;

/**
 * Created by iris1211 on 2017/9/5.
 */

public class MineFragment extends BaseFragment {
    public static MineFragment newInstance(int value){
        Bundle args=new Bundle();
        args.putInt("key",value);
        MineFragment fragment=new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_mine,container,false);
        initView();
        return  mRootView;
    }

    private void initView(){
       // TextView textView=findViewById(R.id.Tv_test);
        //textView.setText("这是第"+getArguments().get("key").toString()+"页");
    }
}
