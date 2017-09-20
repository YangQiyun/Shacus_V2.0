package shacus.edu.seu.com.shacus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shacus.edu.seu.com.shacus.R;

/**
 * Created by Mind on 2017/9/3.
 */
public class TestFragment extends BaseFragment {


    /*
    * 这样写是方便从activity中传参数进来
    * */
    public static TestFragment newInstance(int value){
        Bundle args=new Bundle();
        args.putInt("key",value);
        TestFragment fragment=new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView=inflater.inflate(R.layout.fragment_test,container,false);
        initView();
        return  mRootView;
    }

    private void initView(){
        TextView textView=findViewById(R.id.Tv_test);
        textView.setText("这是第"+getArguments().get("key").toString()+"页");
    }
}
