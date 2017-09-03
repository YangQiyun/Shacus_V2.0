package shacus.edu.seu.com.shacus.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.IOException;

import shacus.edu.seu.com.shacus.Fragment.BaseFragment;
import shacus.edu.seu.com.shacus.Fragment.TestFragment;
import shacus.edu.seu.com.shacus.Network.okHttpUtil;
import shacus.edu.seu.com.shacus.Network.okHttpUtil_BitmapResponse;
import shacus.edu.seu.com.shacus.R;
import shacus.edu.seu.com.shacus.Utils.WeakRefHander;

public class MainActivity extends BaseActivity implements okHttpUtil_BitmapResponse,View.OnClickListener {

    private FragmentManager fragmentManager=this.getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private ImageView imageView;
    private WeakRefHander mHandler;
    private ImageButton[] BottomButton;
    private BaseFragment[] fragmentGroup=new BaseFragment[5];
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initView();
        //initData();
        test();

    }

   private void initView(){
        BottomButton=new ImageButton[5];
       BottomButton[0]= (ImageButton) findViewById(R.id.button_List);
       BottomButton[1]= (ImageButton) findViewById(R.id.button_Message);
       BottomButton[2]= (ImageButton) findViewById(R.id.button_yuepai);
       BottomButton[3]= (ImageButton) findViewById(R.id.button_upload);
       BottomButton[4]= (ImageButton) findViewById(R.id.button_Mine);
       for(ImageButton i:BottomButton)
           i.setOnClickListener(this);
    }
    private void initData(){
        hidefragment();
    }

    @Override
    public void onFailure(final IOException e) {
        Log.d("Main","callback fail");
    }

    @Override
    public void onResponse(Bitmap bitmap) {
        Message msg=Message.obtain(mHandler,1);
        msg.obj=bitmap;
        mHandler.sendMessage(msg);
        Log.d("Main","callback success");
        Log.d("Thread",Integer.toString(msg.what));
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
            case R.id.button_Message:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_Message"));
                fragmentTransaction.commit();
                BottomButton[1].setSelected(true);
                break;
            case R.id.button_yuepai:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_yuepai"));
                fragmentTransaction.commit();
                BottomButton[2].setSelected(true);
                break;
            case R.id.button_upload:
                fragmentTransaction.show(fragmentManager.findFragmentByTag("fg_upload"));
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
        String a[]={"fg_list","fg_Message","fg_yuepai","fg_upload","fg_Mine"};
        for(int i=0;i<5;++i){
            if(fragmentGroup[i]==null){
                fragmentGroup[i]= TestFragment.newInstance(i);
                fragmentTransaction.add(R.id.fragmentlayout_content,fragmentGroup[i],a[i]);}
            else
                fragmentTransaction.hide(fragmentManager.findFragmentByTag(a[i]));
        } fragmentTransaction.commit();
    }

    /*
    * 测试用的
    * */
    private void test(){
        imageView= (ImageView) findViewById(R.id.image);
        mHandler=new WeakRefHander(new WeakRefHander.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        Log.d("Handler","case 0");
                        break;
                    case 1:
                        Bitmap bitmap=(Bitmap)msg.obj;
                        imageView.setImageBitmap(bitmap);
                        break;
                    default:
                        Log.d("Handler","other");
                        break;
                }
                return true;
            }
        });

        Thread mThread=new Thread(){
            @Override
            public void run() {
                super.run();
                okHttpUtil.callBacknei=MainActivity.this;
                okHttpUtil.instance.get(MainActivity.this,"http://ovn2e2iaq.bkt.clouddn.com/2.jpg",null,MainActivity.this);
            }
        };
        mThread.start();
    }

}
