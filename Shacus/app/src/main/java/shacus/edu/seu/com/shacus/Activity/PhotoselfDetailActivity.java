package shacus.edu.seu.com.shacus.Activity;


/**
 * Created by ljh on 2017/2/4.
 * 个人照片详情页可用操作：
 * - 向个人照片添加图片：打开新activity添加图片后回到这个activity（相当于重新加载activity，要向服务器提交新上传的图片并下载新列表）
 * - 删除个人照片的图片：点击编辑后可以从现有列表中删除，再点击由编辑按钮变换而成的完成按钮可以重新加载（只需要向服务器提交新列表，不用下载新列表）
 * 需要intent传入：所属的用户id
 */


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import shacus.edu.seu.com.shacus.R;

public class PhotoselfDetailActivity extends AppCompatActivity {
ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photos_detail_1);
        backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new BackButton());
    }
    class BackButton implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //返回上级界面
          finish();
        }
    }
}